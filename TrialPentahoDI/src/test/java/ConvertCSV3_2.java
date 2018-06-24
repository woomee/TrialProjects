import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClass;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassData;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassMeta;

/**
 * Pentaho DIにて「ユーザ定義Java」ステップを試すサンプル
 *
 * [利用方法]
 * ・Pentaho DIのlibクラスから以下をクラスパスに追加
 *   kettele-core-xxxx.jar
 *   kettel-engine-xxxx.jar
 *
 * [参考]
 * http://type-exit.org/adventures-with-open-source-bi/2010/10/the-user-defined-java-class-step
 *
 * [処理内容]
 * ・ktrと同じディレクトリにある"data_text.csv"を読み込んで"data_text_2.csv"として出力
 *
 * [トライアルポイント]
 * ・processRow()メソッド内にクラスConverterを追加して実行
 *    ⇒後で外部Jarにすれば良いがトライアルの間は暫定利用
 * ・Convertクラスでファイルの読み込みを行い、Kettle側で入力はしない
 * ・Kettle側で出力するフィールドを指定しないでも実行できるようにする
 * ・テーブル名も出力して指定のテーブルに入力する。出力はtable_name, co1, col2とする。
 *
 * @author Eiichiro Umino
 *
 */
public class ConvertCSV3_2 extends TransformClassBase {

	public ConvertCSV3_2(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	/**
	 * 変換を実行する内部クラス
	 */
	private class Converter {
		/* ktr側で設定されているパラメータ */
		// TODO: 外部jarにする際にstatic化
		private String PARAM_IN_FILE= "IN_FILE";
		private String PARAM_START_ROW = "START_ROW";
		private String PARAM_OUT_TABLE = "OUT_TABLE";

		private BufferedReader _reader = null;
		private String _outTable = "";

		/**
		 * ファイルを読み込み初期化
		 * @throws KettleException
		 */
		public void init() throws KettleException{
			try {
				/* パラメータで指定されたファイルを読み込む */
				String filePathStr = getVariable(PARAM_IN_FILE);
				logBasic("In FilePath=" + filePathStr);
				Path filePath = Paths.get(filePathStr, new String[0]);
				_reader = Files.newBufferedReader(filePath);

				/* パラメータで指定された最初の行まで読み込む */
				int startRow = Integer.parseInt(getVariable(PARAM_START_ROW));
				for(int i = 0; i < startRow; i++) {
					_reader.readLine();
				}

				/* テーブル名を取得 */
				_outTable = getVariable(PARAM_OUT_TABLE);

			} catch (IOException e) {
				throwAsKettleException(e);
			}
		}

		/**
		 * インポート対象のカラム名情報を返却する。
		 * 1列目はテーブル名となる"table_name"にする必要あり
		 *
		 * @return
		 */
		public ValueMetaInterface[] getRowColumns() {

			return new ValueMetaInterface[] {
					new ValueMeta("table_name", ValueMetaInterface.TYPE_STRING),
					new ValueMeta("col1", ValueMetaInterface.TYPE_STRING),
					new ValueMeta("col2", ValueMetaInterface.TYPE_STRING)
			};

		}


		/**
		 * ファイルを1行読み込んで配列を返す.
		 * 先頭のデータは出力先のテーブル名とする。
		 *
		 * @return 読み込んだデータ。終端の場合はnul値とする。
		 */
		public Object[] getRowData() throws KettleException {
			if (_reader == null) {
				return null;
			}
			String line = null;
			try {
				line = _reader.readLine();
			} catch (IOException e) {
				throwAsKettleException(e);
			}
			if (line == null) {
				return null;
			}

			String[] columnDatas = line.split(",");

			/*
			 * 必要に応じて変換処理
			 */
			// 大文字にする
			columnDatas[1] = toUpper(columnDatas[1]);


			/* 先頭にテーブル名を追加して返却する */
			Object[] outDatas = new Object[columnDatas.length + 1];
			System.arraycopy(new String[] {_outTable}, 0, outDatas, 0, 1);
			System.arraycopy(columnDatas, 0, outDatas, 1, columnDatas.length);

			return outDatas;
		}

		public String toUpper(String indata) {
			return indata.toUpperCase();
		}

		public void close() throws KettleException {
			if (_reader != null) {
				try {
					_reader.close();
				} catch (IOException e) {
					throwAsKettleException(e);
				}
			}
		}

		private void throwAsKettleException(Throwable e) throws KettleException {
			logError("エラーが発生", e);
			throw new KettleException(e);
		}
	}

	Converter converter = new Converter();

	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first) {
			first = false;
			logBasic("first");

			/* ファイルの読み込みと初期化 */
			converter.init();


			/* 出力する列データcol1を設定 */
			// https://forums.pentaho.com/threads/82155-Java-Plugin-for-Kettle-How-to-get-Row-values-and-create-new-columns/
			// data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();  // エラーになるため不要
			RowMetaInterface newFields = new RowMeta();
			ValueMetaInterface[] columns = converter.getRowColumns();
			for (int i = 0; i < columns.length; i++) {
				newFields.addValueMeta(columns[i]);
			}
			data.outputRowMeta.addRowMeta(newFields);
		}

		/* 行データを取得。最後の行はnullとなるので終了処理 */
		Object[] inputRow = converter.getRowData();
		if (inputRow == null) {
			logBasic("inputRow==null");
			setOutputDone();
			converter.close();
			return false;
		}

		/*
		 * 1行のデータを出力
		 */
		putRow(data.outputRowMeta, inputRow);
		logBasic("putRow");

		return true;
	}

}
