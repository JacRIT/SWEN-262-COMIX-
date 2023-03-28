package Controllers.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCRead extends JDBC {
    public ResultSet executePreparedSQL(String SQL, ArrayList<Object> prepareds) {
        System.out.println("Creating Connection...");
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(SQL) ;
        ) {

            int x = 1 ;
            System.out.println("Preparing Statement...");
            for (Object obj :prepareds) {
                stmt.setObject(x, obj);
                x++ ;
                System.out.println("Preparing Statement..." + obj);
            }
            System.out.println("Executing Command...");

            ResultSet rs = stmt.executeQuery();
            System.out.println("Command Executed!");
            return rs;

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        } 

    }
    // public int executeSQL(String SQL) {

    //     System.out.println("Creating Connection...");

    //     try (
    //         Connection conn = DriverManager.getConnection(URL, USER, PASS);
    //         Statement stmt = conn.createStatement() ;
    //     ) {

    //         System.out.println("Executing Command...");

    //         stmt.executeUpdate(SQL);
    //         System.out.println("Command Executed!");
    //         return 1;

    //     } catch (SQLException e) {
    //         throw new Error("Outer Problem", e);
    //     } 

    // }
}