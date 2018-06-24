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
 * @author e.umino
 *
 */
public class LoopTrans01 extends TransformClassBase {

	public LoopTrans01(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta, UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	private int _loopMaxCount = 0;
	private int _loopCount = 0;
	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

		/* 最初の1回目(1行目)はfirst == trueとなる */
		if (first) {
			first = false;
			logBasic("first");

			String loopMaxCount = getVariable("LOOP_MAX");
			_loopMaxCount = Integer.parseInt(loopMaxCount);
			logBasic("loopMaxCount=" + _loopMaxCount);
		}

		logBasic("loopCount=" + _loopCount);
		_loopCount++;

		if (_loopCount > _loopMaxCount) {
			logBasic("Finish");
			setOutputDone();
			return false;
		}
		
		

		return true;
	}

}
