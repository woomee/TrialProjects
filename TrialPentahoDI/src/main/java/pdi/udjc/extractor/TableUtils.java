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
import java.sql.Timestamp;

import javax.activation.UnsupportedDataTypeException;

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
	 * テーブルのカラムクラス
	 */
	public static class TableColumn {
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
	public static TableColumn[] getTableColumn(String url, String user, String pass, String table)
			throws SQLException, IOException {
		TableColumn[] columns = null;

		try (Connection connection = DriverManager.getConnection(url, user, pass);
				Statement stmt = connection.createStatement()) {
			ResultSet resultSet = stmt.executeQuery("select * from " + table);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			columns = new TableColumn[columnCount];
			for (int i = 0; i < columnCount; i++) {
				String colName = metaData.getColumnName(i + 1);
				String typeName = metaData.getColumnClassName(i + 1);
				columns[i] = new TableColumn(colName, typeName);
			}
		}

		return columns;
	}

	/**
	 *
	 * @param columns
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static ValueMetaInterface[] getTableValueMetaInterfaces(TableColumn[] columns)
			throws SQLException, IOException {
		ValueMetaInterface[] vmis = new ValueMetaInterface[columns.length];
		for (int i = 0; i < vmis.length; i++) {
			vmis[i] = getValueMetaInterface(columns[i].name, columns[i].type);
		}

		return vmis;
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
		} else if (Short.class.getName().equals(typeName) || Integer.class.getName().equals(typeName)
				|| Long.class.getName().equals(typeName)) {
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

	/**
	 * String型のデータ値から型にあったオブジェクトを生成
	 *
	 * 対応するJavaの型は{@link ValueMetaInterface}のjavadocを参照
	 *
	 * @param value
	 * @param typeName
	 * @return
	 * @throws UnsupportedDataTypeException
	 */
	public static Object strintToObject(String value, String typeName) throws UnsupportedDataTypeException {
		/*
		 * ValueMetaInterfaceの対応と合わせる
		 */
		if (Double.class.getName().equals(typeName) ||
				Float.class.getName().equals(typeName)) {
			return Double.valueOf(value);
		} else if (String.class.getName().equals(typeName)) {
			return value;
		} else if (Date.class.getName().equals(typeName)) {
			return Date.valueOf(value);
		} else if (Boolean.class.getName().equals(typeName)) {
			return Boolean.valueOf(value);
		} else if (Short.class.getName().equals(typeName) || Integer.class.getName().equals(typeName)
				|| Long.class.getName().equals(typeName)) {
			/* PDIでは整数型は全てLong型となる */
			return Long.valueOf(value);
		} else if (BigDecimal.class.getName().equals(typeName)) {
			return new BigDecimal(value);
		} else if (Blob.class.getName().equals(typeName) ||
				Clob.class.getName().equals(typeName)) {
			//TODO: 要確認
			return value.getBytes();
		} else if (java.sql.Timestamp.class.getName().equals(typeName)) {
			return Timestamp.valueOf(value);
		}


		throw new UnsupportedDataTypeException("Value=" + value + ", Type=" + typeName);

	}
}
