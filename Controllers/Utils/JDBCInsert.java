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
        System.out.println("Creating Connection...");

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);) {

            int x = 1;
            System.out.println("Preparing Statement...");
            for (Object obj : prepareds) {
                stmt.setObject(x, obj);
                x++;
                System.out.println("Preparing Statement..." + obj);
            }
            System.out.println("Executing Command...");

            stmt.executeUpdate();
            System.out.println("Command Executed!");

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
        System.out.println("Creating Connection...");

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(statementDetails.getSql());) {

            int x = 1;
            System.out.println("Preparing Statement...");
            for (Object obj : statementDetails.getObjects()) {
                stmt.setObject(x, obj);
                x++;
                System.out.println("Preparing Statement..." + obj);
            }
            System.out.println("Executing Command...");

            stmt.executeUpdate();
            System.out.println("Command Executed!");
            return 1;

        } catch (SQLException e) {
            throw new Exception("Outer Problem", e);
        }

    }

    public int executeSQL(String sql) throws Exception {

        System.out.println("Creating Connection...");

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt = conn.createStatement();) {

            System.out.println("Executing Command...");

            stmt.executeUpdate(sql);
            System.out.println("Command Executed!");
            return 1;

        } catch (SQLException e) {
            throw new Exception("Outer Problem", e);
        }

    }
}
