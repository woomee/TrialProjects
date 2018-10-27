package trial;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * テーブルのメタ情報取得する
 *
 * @author umino
 *
 */
public class JDBCTableUtils3 {

	public static void main(String[] args) {
		String schemaName = "public";
		String tableName = "tb1";

		String url = "jdbc:postgresql://10.230.42.55:5432/mi_test";
		String user = "admin";
		String pass = "miap";

		try (
				Connection connection = DriverManager.getConnection(url, user, pass);
				Statement stmt = connection.createStatement()) {

			DatabaseMetaData dbMeta = connection.getMetaData();
			ResultSet rs =  dbMeta.getColumns(null, schemaName, tableName, "%");
			while (rs.next()) {
				String colName = rs.getString("COLUMN_NAME");
				String colDefValue = rs.getString("COLUMN_DEF");
				String colType = rs.getString("DATA_TYPE");
				System.out.println(colName + ", DEF=" + colDefValue + ", TYPE=" + colType);
			}

			ResultSet resultSet = stmt.executeQuery("select * from " + tableName);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				System.out.print(metaData.getColumnName(i + 1));
				System.out.println("  " + metaData.getColumnClassName(i + 1));

			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
