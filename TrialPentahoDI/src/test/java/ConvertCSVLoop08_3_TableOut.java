import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
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
 * ・ktrと同じディレクトリにある"data_text.csv"を読み込んで"data_text_2.csv"として出力
 *
 * [トライアルポイント]
 * ・processRow()メソッド内にクラスExtractorを追加して実行
 *    ⇒後で外部Jarにすれば良いがトライアルの間は暫定利用
 * ・Convertクラスでファイルの読み込みを行い、Kettle側のファイル入力は使用しない
 * ・Table outputで出力するフィールドを指定しないでも実行できるようにする
 *    ⇒data.outputRowMeta.addRowMeta()によってフィールドを動的に設定
 * ・JDBCの接続をパラメータで行う。
 *    ⇒ktrファイルを分割することで解決
 *
 * @author Eiichiro Umino
 *
 */
public class ConvertCSVLoop08_3_TableOut extends TransformClassBase {

	public ConvertCSVLoop08_3_TableOut(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta,
			UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	/* ktrのパラメータとして受け取る */
	//	static private String PARAM_IN_FILE = "IN_FILE";
	//	static private String PARAM_START_ROW = "START_ROW";
	static private String PARAM_DB_URL = "DB_URL";
	static private String PARAM_DB_SCHEMA = "DB_SCHEMA";
	static private String PARAM_DB_TABLE = "DB_TABLE";
	static private String PARAM_DB_USER = "DB_USER";
	static private String PARAM_DB_PASSWORD = "DB_PASSWORD";

	private Connection _connecion;
	private int _prevColumnNum = 0;
	private TableColumn[] _tableColumns = null;
	private PreparedStatement _prepStatement = null;
	private String _schemaTableName;

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
	 * 停止させた際に呼ばれる
	 */
	public void stopRunning(StepMetaInterface stepMetaInterface, StepDataInterface stepDataInterface)
			throws KettleException {
		super.stopRunning(stepMetaInterface, stepDataInterface);
		logBasic("Extract: stopRunning()!");
		closeConnections(false);
	}

	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		try {
			/* 最初の1回目(1行目)はfirst == trueとなる */
			if (first) {
				first = false;
				logBasic("first");

				/* Connectionの確立 */
				String url = getVariable(PARAM_DB_URL);
				String user = getVariable(PARAM_DB_USER);
				String pass = getVariable(PARAM_DB_PASSWORD);
				String schema = getVariable(PARAM_DB_SCHEMA);
				String table = getVariable(PARAM_DB_TABLE);
				_schemaTableName = schema + "." + table;

				_tableColumns = getTableColumn(url, user, pass, _schemaTableName);
				_connecion = getConnection(url, user, pass);

			}

			Object[] rowData = getRow();
			if (rowData == null) {
				logBasic("inputRow==null");
				setOutputDone();
				closeConnections(true);
				return false;
			}

			if (_prevColumnNum != rowData.length) {
				if (_prepStatement != null) {
					_prepStatement.close();
				}
				_prepStatement = createPreparedStatement(_schemaTableName, _tableColumns, rowData.length, _connecion);
			}
			for (int i = 0; i < rowData.length; i++) {
				logBasic("rowData[" + i + "]=" + rowData[i]);
				_prepStatement.setObject(i + 1, rowData[i]);
			}
			_prepStatement.executeUpdate();

		} catch (Exception e) {
			logError("getTableColumns()にてエラー", e);
			closeConnections(false);
			throw new KettleException(e);
		}

		return true;
	}

	private Connection getConnection(String url, String user, String pass) throws SQLException {
		Connection connection = DriverManager.getConnection(url, user, pass);
		connection.setAutoCommit(false);
		return connection;
	}

	private void closeConnections(boolean doCommit) {
		if (_prepStatement != null) {
			try {
				_prepStatement.close();
			} catch (SQLException e) {
				logError("close中にエラー", e);
			}
		}
		if (_connecion != null) {
			if (doCommit) {
				try {
					_connecion.commit();
				} catch (SQLException e) {
					logError("close中にエラー", e);
				}
			}
			else {
				try {
					_connecion.rollback();
				} catch (SQLException e) {
					logError("close中にエラー", e);
				}
			}
			try {
				_connecion.close();
			} catch (SQLException e) {
				logError("close中にエラー", e);
			}
		}
	}

	/**
	 * テーブルのカラムクラス
	 */
	private class TableColumn {
		public String name;
		public String type;

		public TableColumn(String name, String type) {
			this.name = name;
			this.type = type;
		}
	}

	/**
	 * 指定されたテーブルの属性項目を返します。
	 *
	 * JDBCでDBMSへ接続して型情報から生成しますので、多用しないようにする必要あり。
	 *
	 * @param url
	 * @param user
	 * @param pass
	 * @param table
	 * @return
	 */
	private TableColumn[] getTableColumn(String url, String user, String pass, String table)
			throws SQLException, IOException {
		TableColumn[] columns = null;
		// try()はKettleでエラーとなるためNG
		//		try (Connection connection = DriverManager.getConnection(url, user, pass);
		//				Statement stmt = connection.createStatement()) {

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, pass);
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery("select * from " + table);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			columns = new TableColumn[columnCount];
			for (int i = 0; i < columnCount; i++) {
				String colName = metaData.getColumnName(i + 1);
				String typeName = metaData.getColumnClassName(i + 1);
				logBasic("TableColumn[" + i + "]=" + colName + ", type=" + typeName);
				columns[i] = new TableColumn(colName, typeName);
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return columns;
	}

	private PreparedStatement createPreparedStatement(String table, TableColumn[] allColumns, int columnNum,
			Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO " + table + "(");
		for (int i = 0; i < columnNum; i++) {
			if (i != 0) {
				sql.append(",");
			}
			sql.append(allColumns[i].name);
		}
		sql.append(") VALUES(");
		for (int i = 0; i < columnNum; i++) {
			if (i != 0) {
				sql.append(",");
			}
			sql.append("?");
		}
		sql.append(")");
		logBasic("SQL=" + sql.toString());

		PreparedStatement prep = con.prepareStatement(sql.toString());

		return prep;
	}

	//// ここまで
}
