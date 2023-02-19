package JDBChelpers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


@Deprecated
public class JDBCRun {

    private final String URL ;
    private final String USER ;
    private final String PASS ;

    public JDBCRun() {
        this.URL = "jdbc:postgresql://jdb1.c4qx1ly4rhvr.us-east-2.rds.amazonaws.com:5432/postgres" ;
        this.USER = "swen262" ;
        this.PASS = "bubbles" ;

    } public JDBCRun( String url, String username, String password) {
        this.URL = url ;
        this.USER = username ;
        this.PASS = password ;
    }

    public ArrayList<String> executeSQL(String SQL_stmt) {

        ArrayList<String> hw = new ArrayList<String>();
        
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement()
        ) {

            try {
                ResultSet rs = stmt.executeQuery(SQL_stmt);

                while (rs.next()) {

                    hw.add(rs.getString("string") ) ;
                }
            } catch (Exception e) {
                throw new Error("Inner Problem", e);
            }

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        } 

        return hw ;
    }

    public static void main(String[] args) {
        try {
            JDBCRun test = new JDBCRun() ;
            System.out.println( test.executeSQL("SELECT * FROM exampletest") );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}