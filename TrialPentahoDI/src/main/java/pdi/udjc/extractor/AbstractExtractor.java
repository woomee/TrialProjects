package pdi.udjc.extractor;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.activation.UnsupportedDataTypeException;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBigNumber;
import org.pentaho.di.core.row.value.ValueMetaBinary;
import org.pentaho.di.core.row.value.ValueMetaBoolean;
import org.pentaho.di.core.row.value.ValueMetaDate;
import org.pentaho.di.core.row.value.ValueMetaInteger;
import org.pentaho.di.core.row.value.ValueMetaNumber;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.row.value.ValueMetaTimestamp;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;

public abstract class AbstractExtractor {
	/* ktr側で設定されているパラメータ */
	// TODO: 外部jarにする際にstatic化
	static protected String PARAM_IN_FILE = "IN_FILE";
	static protected String PARAM_START_ROW = "START_ROW";
	static protected String PARAM_DB_TABLE = "DB_TABLE";

	protected String _outTable = "";

	private TransformClassBase _parent;

	public AbstractExtractor(TransformClassBase parent) {
		_parent = parent;

		/* テーブル名を取得 */
		_outTable = _parent.getVariable(PARAM_DB_TABLE);
	}

	/**
	 * 出力先のテーブル名
	 *
	 * @return
	 */
	public String getTableName() {
		return _outTable;
	}

	/**
	 * 初期化処理
	 * @throws KettleException
	 */
	abstract public void open() throws KettleException;

	/**
	 * インポート対象のカラム名情報を返却する。
	 *
	 * @return
	 */
	abstract public ValueMetaInterface[] getRowColumns();

	/**
	 * ファイルを1行読み込んで配列を返す.
	 *
	 * @return 読み込んだデータ。終端の場合はnul値とする。
	 */
	abstract public Object[] readRowData() throws KettleException;

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
	 * @throws KettleException
	 */
	protected ValueMetaInterface[] getTableColumns(String url, String user, String pass, String table)
			throws KettleException {
		ValueMetaInterface[] columns = null;
		try (Connection connection = DriverManager.getConnection(url, user, pass);
				Statement stmt = connection.createStatement()) {
			ResultSet resultSet = stmt.executeQuery("select * from " + table +" limit 1");
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			columns = new ValueMetaInterface[columnCount];
			for (int i = 0; i < columnCount; i++) {
				String colName = metaData.getColumnName(i + 1);
				String typeName = metaData.getColumnClassName(i + 1);
				columns[i] = getValueMetaInterface(colName, typeName);
			}
		} catch (Exception e) {
			throwAsKettleException(e);
		}

		return columns;
	}

	/**
	 * 指定されたカラム名とJava型情報から{@link ValueMetaInterface}を返します。
	 *
	 * Java型との対応は{@link ValueMetaInterface}のJavaDocを参照。
	 *
	 * @param colName
	 * @param typeName
	 * @return
	 * @throws UnsupportedDataTypeException
	 */
	protected ValueMetaInterface getValueMetaInterface(String colName, String typeName)
			throws UnsupportedDataTypeException {
		if (Double.class.getName().equals(typeName) ||
				Float.class.getName().equals(typeName)) {
			return new ValueMetaNumber(colName);
		} else if (String.class.getName().equals(typeName)) {
			return new ValueMetaString(colName);
		} else if (Date.class.getName().equals(typeName)) {
			return new ValueMetaDate(colName);
		} else if (Boolean.class.getName().equals(typeName)) {
			return new ValueMetaBoolean(colName);
		} else if (Long.class.getName().equals(typeName) || Integer.class.getName().equals(typeName)) {
			/* Long型もValueMetaIntegerとなる */
			return new ValueMetaInteger(colName);
		} else if (BigDecimal.class.getName().equals(typeName)) {
			return new ValueMetaBigNumber(colName);
		} else if (Blob.class.getName().equals(typeName) ||
				Clob.class.getName().equals(typeName)) {
			return new ValueMetaBinary(colName);
		} else if (java.sql.Timestamp.class.getName().equals(typeName)) {
			return new ValueMetaTimestamp(colName);
		}

		throw new UnsupportedDataTypeException("Column=" + colName + ", Type=" + typeName);
	}

}
