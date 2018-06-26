package pdi.udjc.extractor;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;

public abstract class AbstractExtractor {
//	/* ktr側で設定されているパラメータ */
//	// TODO: 外部jarにする際にstatic化
//	static protected String PARAM_IN_FILE = "IN_FILE";
//	static protected String PARAM_START_ROW = "START_ROW";
//	static protected String PARAM_DB_TABLE = "DB_TABLE";

	protected TransformClassBase _parent;

	public AbstractExtractor(TransformClassBase parent) {
		_parent = parent;
	}


	/**
	 * 初期化処理
	 * @throws KettleException
	 */
	abstract public void open() throws KettleException;


	/**
	 * ファイルを1行読み込んで配列を返す.
	 *
	 * @return 読み込んだデータ。終端の場合はnul値とする。
	 */
	abstract public String[] readRowData() throws KettleException;

	/**
	 * 終了処理
	 * @throws KettleException
	 */
	abstract public void close() throws KettleException;

	/**
	 * エラーをログ出力してKettleExceptionに変換する。
	 * @param e
	 * @throws KettleException
	 */
	protected void throwAsKettleException(Throwable e) throws KettleException {
		_parent.logError("エラーが発生", e);
		throw new KettleException(e);
	}


}
