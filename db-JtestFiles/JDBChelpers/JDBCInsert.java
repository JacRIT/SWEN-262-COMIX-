package JDBChelpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCInsert extends JDBC {


    @Override
    public void executeSQL(String SQL) {

        
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement()
        ) {

        stmt.executeUpdate(SQL);

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        } 


    }
}
