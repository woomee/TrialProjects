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
public class ConvertCSVLoop03_1 extends TransformClassBase {

	public ConvertCSVLoop03_1(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	static private String PARAM_DB_TABLES = "DB_TABLES";

	static private String FIELD_DB_TABLE = "db_table";

	private String[] _db_table_array;
	private int _currentIdx = 0;

	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first) {
			first = false;
			logBasic("first");

			String db_tables = getVariable(PARAM_DB_TABLES);
			_db_table_array = db_tables.split(",");
			logBasic("db_tables=" + db_tables);
		}

		/* 次のステップへフィールドで渡す */
		String db_table = _db_table_array[_currentIdx];
		Object[] outputData = RowDataUtil.allocateRowData(1);
		get(Fields.Out, FIELD_DB_TABLE).setValue(outputData, db_table);
		logBasic("current db_table=" + db_table);

		_currentIdx++;

		putRow(data.outputRowMeta, outputData);


		if (_currentIdx >= _db_table_array.length) {
			logBasic("Finish");
			setOutputDone();
			return false;
		}

		return true;
	}

	////////////////////////////////////////////////////////////
	/// ここまで

}
