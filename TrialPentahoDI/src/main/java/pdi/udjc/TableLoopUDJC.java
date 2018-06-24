package pdi.udjc;
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
 * Pentaho DIの「ユーザ定義Java」ステップを使ってデータ変換
 *
 * パラメータ中に指定された複数の出力先を分割して次のステップへ渡す
 *
 * @author Eiichiro Umino
 *
 */
public class TableLoopUDJC extends TransformClassBase {

	public TableLoopUDJC(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。

	/* ktrのパラメータとして受け取る */
	static public String PARAM_DB_TABLES = "DB_TABLES";

	/* 次のステップへフィールドとして渡す */
	static public String FIELD_DB_TABLE = "db_table";


	private String[] _db_table_array;
	private int _currentIdx = 0;

	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first) {
			first = false;

			String db_tables = getVariable(PARAM_DB_TABLES);
			_db_table_array = db_tables.split(",");
			logBasic("db_tables=" + db_tables);
		}


		String db_table = _db_table_array[_currentIdx];
		Object[] outputData = RowDataUtil.allocateRowData(1);
		get(Fields.Out, FIELD_DB_TABLE).setValue(outputData, db_table);
		_currentIdx++;
		logBasic("current db_table=" + db_table);

		putRow(data.outputRowMeta, new Object[] {db_table});


		if (_currentIdx >= _db_table_array.length) {
			logBasic("Finish");
			setOutputDone();
			return false;
		}

		return true;
	}

	///////////////////////////////////////////////////
	// ここまでユーザ定義Javaステップへコピー

}
