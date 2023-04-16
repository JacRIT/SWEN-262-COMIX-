package Controllers.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class JDBCUpdate extends JDBC {

    public int executePreparedSQL(PreparedStatementContainer statementDetails) {
        
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(statementDetails.getSql()) ;
        ) {

            int x = 1 ;
            for (Object obj :statementDetails.getObjects()) {
                stmt.setObject(x, obj);
                x++ ;
            }

            stmt.executeUpdate();
            return 1;

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        } 

    }
}
