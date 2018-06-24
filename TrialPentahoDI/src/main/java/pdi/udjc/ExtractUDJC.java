package pdi.udjc;
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

import pdi.udjc.extractor.AbstractExtractor;
import pdi.udjc.extractor.ExtractorCSV;

/**
 * Pentaho DIの「ユーザ定義Java」ステップを使ってデータ変換
 *
 * [利用方法]
 * ・Pentaho DIのlibクラスから以下をクラスパスに追加
 *   kettele-core-xxxx.jar
 *   kettel-engine-xxxx.jar
 *
 * @author Eiichiro Umino
 *
 */
public class ExtractUDJC extends TransformClassBase {

	public ExtractUDJC(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	static public String PARAM_FILEPATH = "filePath";
	static public String PARAM_STARTROW = "start";

	static public String PARAM_DB_URL = "url";
	static public String PARAM_DB_TABLE = "db_table";

	private AbstractExtractor _extracter;

	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first) {
			first = false;
			logBasic("first");

			_extracter = new ExtractorCSV(this);


			/* ファイルの読み込みと初期化 */
			_extracter.open();
		}

		/* 行データを取得。最後の行はnullとなるので終了処理 */
		Object[] rowData = _extracter.readRowData();
		if (rowData == null) {
			logBasic("inputRow==null");
			setOutputDone();
			_extracter.close();
			return false;
		}

		/* テーブル名を取得して行データの先頭に入れる */
		String tableName = _extracter.getTableName();
		Object[] rowDataWithTable = new Object[rowData.length + 1];
		System.arraycopy(new String[] {tableName}, 0, rowDataWithTable, 0, 1);
		System.arraycopy(rowData, 0, rowDataWithTable, 1, rowData.length);

		/*
		 * 出力する列データを設定
		 * クリアしてから再設定することで途中でカラム構成が変わっても対応する。
		 * 1列目はテーブル名を入れる
		 * */
		RowMetaInterface newFields = new RowMeta();
		newFields.addValueMeta(new ValueMetaString("table_name"));
		ValueMetaInterface[] columns = _extracter.getRowColumns();
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

		return true;
	}



}
