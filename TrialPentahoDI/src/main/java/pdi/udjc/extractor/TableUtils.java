package pdi.udjc.extractor;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

public class TableUtils {


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
	static public ValueMetaInterface[] getTableColumns(String url, String user, String pass, String table)
			throws SQLException, IOException {
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
	static public ValueMetaInterface getValueMetaInterface(String colName, String typeName)
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
