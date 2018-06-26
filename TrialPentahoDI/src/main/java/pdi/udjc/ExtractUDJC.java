package pdi.udjc;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClass;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassData;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassMeta;

import pdi.udjc.extractor.AbstractExtractor;
import pdi.udjc.extractor.ExtractorCSV;
import pdi.udjc.extractor.TableUtils;

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

	/* ktrのパラメータとして受け取る */
	static public String PARAM_EXTRACTOR = "EXTRACTOR";
	static public String PARAM_IN_FILE = "IN_FILE";
	static public String PARAM_START_ROW = "START_ROW";
	static public String PARAM_DB_URL = "DB_URL";
	static public String PARAM_DB_SCHEMA = "DB_SCHEMA";
	static public String PARAM_DB_TABLE = "DB_TABLE";
	static public String PARAM_DB_USER = "DB_USER";
	static public String PARAM_DB_PASSWORD = "DB_PASSWORD";

	static private String EXTRACTOR_AUTO = "auto";
	static private String EXTRACTOR_CSV = "csv";
	//TODO: 他の拡張子を追加

	static private String ERR_NO_EXTENSION = "ファイル名に拡張子が無いため種類を判別できません";

	private AbstractExtractor   _extractor;
	private TableUtils.TableColumn[] _tableColumns;

	// Kettleでエラーとなる
	// @Override
	/**
	 * 一時停止の際に呼ばれる。
	 */
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		super.dispose(smi, sdi);
		logBasic("Extract: dispose()");
	}
	/**
	 * キャンセルにて停止させた際に呼ばれる
	 */
	public void stopRunning(StepMetaInterface stepMetaInterface, StepDataInterface stepDataInterface)
			throws KettleException {
		super.stopRunning(stepMetaInterface, stepDataInterface);
		logBasic("Extract: stopRunning()!");
		if (_extractor != null) {
			try {
				_extractor.close();
			} catch (KettleException e) {
			}
		}
	}



	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		try {
			/* 最初の1回目(1行目)はfirst == trueとなる */
			if (first) {
				//first = false;
				logBasic("first");

				/* Extractorの生成 */
				String extractor = getVariable(PARAM_EXTRACTOR);
				if (EXTRACTOR_AUTO.equals(extractor)) {
					String filePath = getVariable(PARAM_IN_FILE);
					int dotIndex = filePath.lastIndexOf(".");
					if (dotIndex < 0) {
						throw new KettleException(ERR_NO_EXTENSION);
					}
					extractor = filePath.substring(dotIndex);
				}
				if (EXTRACTOR_CSV.equals(extractor)) {
					_extractor = new ExtractorCSV(this);
				}

				/* ファイルの読み込みと初期化 */
				_extractor.open();
			}

			/* 行データを取得。最後の行はnullとなったら終了処理 */
			String[] rowData = _extractor.readRowData();
			if (rowData == null) {
				logBasic("inputRow==null");
				setOutputDone();
				_extractor.close();
				return false;
			}

			if (first) {
				first = false;

				/*
				 * 出力する列データを設定
				 *
				 * ここで出力する列データ数はinsertする列数と同じでないとエラーとなる。
				 * そのため、1行目のデータを取得時に処理を行う。
				 *
				 */
				String url = getVariable(PARAM_DB_URL);
				String user = getVariable(PARAM_DB_USER);
				String pass = getVariable(PARAM_DB_PASSWORD);
				String schema = getVariable(PARAM_DB_SCHEMA);
				String table = getVariable(PARAM_DB_TABLE);
				String schemaTable = schema + "." + table;
				_tableColumns = TableUtils.getTableColumn(url, user, pass, schemaTable);
				ValueMetaInterface[] valueMetaInterfaces = TableUtils.getTableValueMetaInterfaces(_tableColumns);

				/* rowDataの数だけtableColumnsを追加する */
				RowMetaInterface newFields = new RowMeta();
				for (int i = 0; i < rowData.length; i++) {
					logBasic("tableColunns[" + i + "]=" + valueMetaInterfaces);
					newFields.addValueMeta(valueMetaInterfaces[i]);
				}
				data.outputRowMeta.addRowMeta(newFields);
			}


			/* rowDataをテーブルの型に合わせたオブジェクトに変換する */
			Object[] typedRowData = new Object[rowData.length];
			for (int i = 0; i < rowData.length; i++) {
				typedRowData[i] = TableUtils.strintToObject(rowData[i], _tableColumns[i].type);
			}

			/*
			 * 1行のデータを出力
			 */
			putRow(data.outputRowMeta, typedRowData);
			logBasic("putRow");
		} catch (Exception e) {
			logError("getTableColumns()にてエラー", e);
			throw new KettleException(e);
		}

		return true;
	}


	///////////////////////////////////////////////////
	// ここまでユーザ定義Javaステップへコピー


}
