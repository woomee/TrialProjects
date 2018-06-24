import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClass;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassData;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassMeta;

/**
 * Pentaho DIにてTransformをループして繰り返し処理ができるか試す
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
 * ・環境変数LOOP_MAXを取得。現在のループ番号をインクリメントして次への出力とする。
 * ・ループ番号がLOOP_MAXに達したら終了
 *
 * [トライアルポイント]
 * ・ループ番号を次のステップに渡すことができるか？
 *
 * @author Eiichiro Umino
 *
 */
public class LoopTrans02_2 extends TransformClassBase {

	public LoopTrans02_2(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////
	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		Object[] row=getRow();
		if (row == null) {
			logBasic("Loop2: Finish");
			setOutputDone();
			return false;
		}

		long loopCount = get(Fields.In, "loopCount").getInteger(row);

		logBasic("Loop2: loopCount=" + loopCount);


		return true;
	}

}
