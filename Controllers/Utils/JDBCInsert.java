package Controllers.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBCInsert extends JDBC {

    public int executePreparedSQLGetId(String SQL, ArrayList<Object> prepareds) throws Exception {

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);) {

            int x = 1;
            for (Object obj : prepareds) {
                stmt.setObject(x, obj);
                x++;
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (int) generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Outer Problem", e);
        }

    }

    public int executePreparedSQL(PreparedStatementContainer statementDetails) throws Exception {

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(statementDetails.getSql());) {

            int x = 1;
            for (Object obj : statementDetails.getObjects()) {
                stmt.setObject(x, obj);
                x++;
            }

            stmt.executeUpdate();
            return 1;

        } catch (SQLException e) {
            throw new Exception("Outer Problem", e);
        }

    }

    public int executeSQL(String sql) throws Exception {

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt = conn.createStatement();) {

            stmt.executeUpdate(sql);
            return 1;

        } catch (SQLException e) {
            throw new Exception("Outer Problem", e);
        }

    }
}
