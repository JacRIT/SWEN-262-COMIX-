package Controllers.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCRead extends JDBC {

    public ArrayList<ArrayList<Object>> readListofLists(String SQL, ArrayList<Object> prepareds, int numCols) {
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(SQL);) {

            int x = 1;
            for (Object obj : prepareds) {
                stmt.setObject(x, obj);
                x++;
            }

            ResultSet rs = stmt.executeQuery();

            ArrayList<ArrayList<Object>> results = new ArrayList<>();
            while (rs.next()) {
                ArrayList<Object> entry = new ArrayList<>();
                for (int col = 1; col <= numCols; col++) {
                    entry.add(rs.getObject(col));
                }
                results.add(entry);
            }

            return results;

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        }

    }

    public ArrayList<Object> executePreparedSQL(String SQL, ArrayList<Object> prepareds) {
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(SQL);) {

            int x = 1;
            for (Object obj : prepareds) {
                stmt.setObject(x, obj);
                x++;
            }

            ResultSet rs = stmt.executeQuery();

            ArrayList<Object> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getObject(1));
            }

            return result;

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        }

    }
    // public int executeSQL(String SQL) {

    // System.out.println("Creating Connection...");

    // try (
    // Connection conn = DriverManager.getConnection(URL, USER, PASS);
    // Statement stmt = conn.createStatement() ;
    // ) {

    // System.out.println("Executing Command...");

    // stmt.executeUpdate(SQL);
    // System.out.println("Command Executed!");
    // return 1;

    // } catch (SQLException e) {
    // throw new Error("Outer Problem", e);
    // }

    // }
}
