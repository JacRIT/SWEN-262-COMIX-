package JDBChelpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCInsert extends JDBC {


    
    public int executePreparedSQL(String SQL, Object[] preapreds) {

        
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(SQL) ;
        ) {

        int x = 1 ;
        for (Object obj :preapreds) {
            stmt.setObject(x, obj);
            x++ ;
        }

        return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        } 

    }
    public int executeSQL(String SQL) {

        
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement() ;
        ) {


        return stmt.executeUpdate(SQL);

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        } 

    }
}
