import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClass;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassData;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassMeta;

/**
 * Pentaho DIにてCSV変換のTransformをループして繰り返し処理ができるか試す
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
 * ・環境変数DB_TABLESを取得。カンマ区切りで分割して次の出力とする。
 * ・全てのDB_TABLESを出力したら処理は終了
 *
 * [トライアルポイント]
 * ・DB_TABLESを次のステップに渡すことができるか？
 * ・渡されたテーブルに対してTransformを実行できているか？
 *   ⇒実行できるが、それぞれのステップが独立で動くため、TB1に1レコード、TB2に残りの全レコードが入ってしまう
 *
 * @author Eiichiro Umino
 *
 */
public class ConvertCSVLoop07_1_Loop extends TransformClassBase {

	public ConvertCSVLoop07_1_Loop(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	/* ktrのパラメータとして受け取る */
	static private String PARAM_DB_DRIVERS = "DB_DRIVERS";
	static private String PARAM_DB_URLS = "DB_URLS";
	static private String PARAM_DB_SCHEMAS = "DB_SCHEMAS";
	static private String PARAM_DB_TABLES = "DB_TABLES";
	static private String PARAM_DB_USERS = "DB_USERS";
	static private String PARAM_DB_PASSWORDS = "DB_PASSWORDS";
	static private String PARAM_START_ROWS = "START_ROWS";

	/* 次のステップへフィールドとして渡す */
	static private String FIELD_DB_DRIVER = "db_driver";
	static private String FIELD_DB_URL = "db_url";
	static private String FIELD_DB_SCHEMA = "db_schema";
	static private String FIELD_DB_TABLE = "db_table";
	static private String FIELD_DB_USER = "db_user";
	static private String FIELD_DB_PASS = "db_pass";
	static private String FIELD_START_ROW = "start_row";


	/* PARAMとFIELDをまとめたもの。個数と順序で対応していることが前提 */
	static private String[] PARAMS = {
			PARAM_DB_DRIVERS,
			PARAM_DB_URLS,
			PARAM_DB_SCHEMAS,
			PARAM_DB_TABLES,
			PARAM_DB_USERS,
			PARAM_DB_PASSWORDS,
			PARAM_START_ROWS
	};
	static private String[] FIELDS = {
			FIELD_DB_DRIVER,
			FIELD_DB_URL,
			FIELD_DB_SCHEMA,
			FIELD_DB_TABLE,
			FIELD_DB_USER,
			FIELD_DB_PASS,
			FIELD_START_ROW
	};

	private String[][] _allParams = null;
	private int _currentIdx = 0;


	// Kettleでエラーとなる
	// @Override
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		super.dispose(smi, sdi);
		logBasic("TableLoop: dispose()");
	}
	public void stopRunning(StepMetaInterface stepMetaInterface, StepDataInterface stepDataInterface)
			throws KettleException {
		super.stopRunning(stepMetaInterface, stepDataInterface);
		logBasic("TableLoop: stopRunning()!");
	}



	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first) {
			first = false;
			logDebug("first");

			/* パラメータをカンマ区切りして取得 */
			_allParams = getAllParams(PARAMS);
		}

		/* カレントのデータを取得してouputDataに設定 */
		String currentParams[] = getParams(_allParams, _currentIdx);
		Object[] outputField = RowDataUtil.allocateRowData(FIELDS.length);
		for (int i = 0; i < FIELDS.length; i++) {
			logDebug("Set Field: " + FIELDS[i] + "=" + currentParams[i]);
			get(Fields.Out, FIELDS[i]).setValue(outputField, currentParams[i]);
		}

		/* 次のステップへフィールドで渡す */
		putRow(data.outputRowMeta, outputField);

		/* 全てのdb_table(_paramVariables[0])を渡したら終了する */
		_currentIdx++;
		if (_currentIdx >= _allParams[0].length) {
			logDebug("Finish");
			setOutputDone();
			return false;
		}

		return true;
	}

	private String[][] getAllParams(String[] paramKeys) {
		String[][] variables = new String[paramKeys.length][];
		for (int i = 0; i < paramKeys.length; i++) {
			String[] vals = getVariable(paramKeys[i]).split(",");
			variables[i] = vals;
		}

		for (int i = 0; i < variables.length; i++) {
			for (int j = 0; j < variables[i].length; j++) {
				logDebug("AllVariables[" + i + "][" + j + "]: " + variables[i][j]);
			}
		}


		return variables;
	}

	private String[] getParams(String[][] allParams, int index) {
		String[] variables = new String[allParams.length];
		for (int i = 0; i < variables.length; i++) {
			variables[i] = allParams[i][index];
			logDebug("CurrentVariable[" + i + "]: " + variables[i]);
		}
		return variables;
	}

	////////////////////////////////////////////////////////////
	/// ここまで

}
