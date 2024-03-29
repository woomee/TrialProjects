import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClass;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassData;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassMeta;

/**
 * Pentaho DIにてCSV変換のTransformをループして繰り返し処理ができるか試す
 *
 * processRow()の中で一つのテーブルに対する処理を行うようにした。
 * 　⇒ただ、1つの大きいファイルではDBに入れるのが後になってしまうのが問題
 * 　　⇒変換途中でprocessRowを抜ける処理を入れる  (ConverterCSVLoop03)
 *
 * @author Eiichiro Umino
 *
 */
public class ConvertCSVLoop05_2 extends TransformClassBase {

	public ConvertCSVLoop05_2(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	/* ktr側で設定されているパラメータ */
	static private String PARAM_IN_FILE = "IN_FILE";


	/* 前のステップから渡されたフィールド */
	static private String FIELD_DB_TABLE = "db_table";
	static private String FIELD_DB_USER = "db_user";
	static private String FIELD_DB_PASS = "db_pass";
	static private String FIELD_START_ROW = "start_row";

	static private String[] FIELDS = {
			FIELD_DB_TABLE,
			FIELD_DB_USER,
			FIELD_DB_PASS,
			FIELD_START_ROW
	};

	/**
	 * 変換を実行する内部クラス
	 */
	private class Extractor {

		private BufferedReader _reader = null;

		/**
		 * ファイルを読み込み初期化
		 * @throws KettleException
		 */
		public void init(int startRow) throws KettleException {
			try {
				/* パラメータで指定されたファイルを読み込む */
				String filePathStr = getVariable(PARAM_IN_FILE);
				logBasic("In FilePath=" + filePathStr);
				Path filePath = Paths.get(filePathStr, new String[0]);
				_reader = Files.newBufferedReader(filePath);

				/* 最初の行まで読み込む */
				for (int i = 0; i < startRow; i++) {
					_reader.readLine();
				}
			} catch (IOException e) {
				throwAsKettleException(e);
			}
		}

		/**
		 * インポート対象のカラム名情報を返却する。
		 *
		 * @return
		 */
		public ValueMetaInterface[] getRowColumns() {
			return new ValueMetaInterface[] {
					new ValueMetaString("col1"),
					new ValueMetaString("col2")
			};
		}

		/**
		 * ファイルを1行読み込んで配列を返す.
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

			return columnDatas;
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

	Extractor extractor = new Extractor();

	String _tableName = "";
	static int PROCESS_COUNT = 2;
	private boolean _process_contiue = false;

	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first) {
			first = false;
			logBasic("first");
		}

		/* processの途中ではない場合(_process_contiue == false)の場合は
		 * テーブル名を前のステップから取得。
		 * null値になった場合は終了 */
		if (!_process_contiue) {

			/* 前のステップからのフィールド値を取得 */
			Object[] rowFromPrivStep = getRow();
			if (rowFromPrivStep == null) {
				logBasic("inputRow==null");
				setOutputDone();
				return false;
			}
			String[] fieldValues = getAllField(FIELDS, rowFromPrivStep);

			/* 必要なフィールド値を変数に保存 */
			_tableName = fieldValues[0];
			int startRow = Integer.parseInt(fieldValues[3]);

			/* コンバータの初期化 */
			extractor.init(startRow);

		}
		_process_contiue = false;

		/*
		 * テーブルに対する変換を実行
		 *
		 * PROCESS_COUNT毎に処理を分割する
		 */
		Object[] rowData = null;
		int count = 0;
		while ((rowData = extractor.getRowData()) != null) {
			/* データの先頭にテーブル名を入れる */
			Object[] rowDataWithTable = new Object[rowData.length + 1];
			System.arraycopy(new String[] { _tableName }, 0, rowDataWithTable, 0, 1);
			System.arraycopy(rowData, 0, rowDataWithTable, 1, rowData.length);

			/*
			 * 出力する列データを設定
			 * クリアしてから再設定することで途中でカラム構成が変わっても対応する。
			 * 1列目はテーブル名を入れる
			 * */
			RowMetaInterface newFields = new RowMeta();
			newFields.addValueMeta(new ValueMetaString(FIELD_DB_TABLE));
			ValueMetaInterface[] columns = extractor.getRowColumns();
			for (int i = 0; i < columns.length; i++) {
				newFields.addValueMeta(columns[i]);
			}
			data.outputRowMeta.clear();
			data.outputRowMeta.addRowMeta(newFields);

			/*
			 * 1行のデータを出力
			 */
			putRow(data.outputRowMeta, rowDataWithTable);
			logBasic("putRow");

			count++;
			if (count >= PROCESS_COUNT) {
				_process_contiue = true;
				break;
			}
		}

		/* processが終了した時(_process_contiue==true)のみ
		 *  コンバータのクローズ */
		if (!_process_contiue) {
			extractor.close();
		}

		return true;

	}

	private String[] getAllField(String[] fieldKeys, Object[] rowData) throws KettleException {
		String[] values = new String[fieldKeys.length];

		for (int i = 0; i < fieldKeys.length; i++) {
			values[i] = get(Fields.In, fieldKeys[i]).getString(rowData);
			logDebug("Get Field: " + fieldKeys[i] + "=" + values[i]);
		}

		return values;
	}

	//// ここまで

}
