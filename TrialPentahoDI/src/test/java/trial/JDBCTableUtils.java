package trial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 以下を参考に試す。
 * http://j-link123-devstart.hatenablog.com/entry/2016/05/20/220235
 *
 * ⇒動作しないのでボツ
 *
 * @author umino
 *
 */
public class JDBCTableUtils {

	public static void main(String[] args) {
		String tableName = "app.tb1";

		String url = "jdbc:derby://localhost/testDB;create=true";
		String user = "admin";
		String pass = "admin";
		Connection connection;
		try {
			connection = DriverManager.getConnection(url, user, pass);

			Statement stmt = connection.createStatement();

		    if( stmt.execute( "describe " + tableName)) {
		        StringBuilder sb = new StringBuilder(  );
		        ResultSet describe = stmt.getResultSet( );
		        while( describe.next( ) ){
		            for( int i = 1; i <= describe.getMetaData( ).getColumnCount( ); i++ ){
		                sb.append( describe.getString( i ) ).append( "\t|\t" );
		            }
		            sb.append( "<br>\n" );
		        }
		        describe.close( );
		    }

		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
