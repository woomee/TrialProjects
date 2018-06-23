import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.FieldHelper;
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
 * ・ktrと同じディレクトリにある"data_text.xls"を読み込んで"col1"列を大文字にして"data_text.csv"として出力
 *
 *
 * @author Eiichiro Umino
 *
 */
public class UpperCase3 extends TransformClassBase {

	public UpperCase3(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// processRowをユーザ定義Javaステップへ追加
	///////////////////////////////////////////////////
	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
	{
		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first){
	        first = false;
	        logBasic("first");

	        /*
	         * パラメータはgetVariable()で取得する。
	         * getParameter()は「ユーザ定義Java」のみで設定されたものとなる。
	         */
	        String param1 = getParameter("param1");
	        logBasic("getParameter(): param1 = " + param1);		// null
	        param1 = getVariable("param1", "defaultValue");
	        logBasic("getVariable(): param1 = " + param1);		// defaultValue or 設定されたパラメータ

	    }

		/* 行データを取得。最後の行はnullとなるので終了処理 */
	    Object[] inputRow = getRow();
	    if (inputRow == null) {
	    	// 標準出力ではPDIで表示されない
	    	//System.out.println("r == null");
	    	logBasic("r==null");
	    	setOutputDone();
	        return false;
	    }

	    /* 出力するサイズの行オブジェクトを生成 */
	    Object[] outputRow = createOutputRow(inputRow, data.outputRowMeta.size());

	    /* 前のステップからの入力フィールド"col1"を得る */
	    FieldHelper field1 =  get(Fields.In, "col1");
	    String test_value = field1.getString(inputRow);

	    /* [変換]
	     * 大文字/小文字にする
	     */
	    String uppercase_value = test_value.toUpperCase();
	    String lowercase_value = test_value.toLowerCase();

	    /*
	     * 変換したデータを"uppercase"列, "lowercase"列として出力へセット
	     *
	     *  "uppercase"列、"lowercase"列はktr側で作成しておく必要あり
	     */
	    FieldHelper outField1 = get(Fields.Out, "uppercase");
	    outField1.setValue(outputRow, uppercase_value);

	    get(Fields.Out, "lowercase").setValue(outputRow, lowercase_value);  // 1ライン化

	    /*
	     * 全ての列を出力
	     */
	    putRow(data.outputRowMeta, outputRow);
	    logBasic("putRow");

	    return true;
	}

}
