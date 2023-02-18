import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import org.postgresql.util.PSQLException;


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

    public ResultSet executeSQL(String SQL_stmt) {
        
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement()
        ) {

            ResultSet rs = stmt.executeQuery(SQL_stmt);

            return rs ;

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } 
    }

    public static void main(String[] args) {
        try {
            System.out.println( new JDBCRun().executeSQL("SELECT * FROM exampletest").getString("string") );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}