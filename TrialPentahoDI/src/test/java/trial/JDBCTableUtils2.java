package trial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTableUtils2 {

	public static void main(String[] args) {
		String tableName = "app.tb1";

		String url = "jdbc:derby://localhost/testDB;create=true";
		String user = "admin";
		String pass = "admin";

		try (
				Connection connection = DriverManager.getConnection(url, user, pass);
				Statement stmt = connection.createStatement() ){
			ResultSet resultSet = stmt.executeQuery( "select * from " + tableName);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i=0; i<columnCount; i++) {

				System.out.print(metaData.getColumnName(i+1));
				System.out.println("  " + metaData.getColumnClassName(i+1));

			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
