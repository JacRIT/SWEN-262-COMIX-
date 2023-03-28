package Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Controllers.Utils.JDBCInsert;
import Controllers.Utils.JDBCRead;
import Model.JavaObjects.User;

public class UserController{
    JDBCRead jdbcRead;
    JDBCInsert jdbcInsert;

    public UserController() {
        this.jdbcRead = new JDBCRead();
        this.jdbcInsert = new JDBCInsert();
    }

    public User create (User newUser){  // change to just take username?
        // add a row to collection_info (SERIAL id, nickname)
        // get the id of the new collection (how?)
        // add a row to collection_reference (SERIAL id, collection_fk, copy_fk(?))   
        // add a row to user_info (SERIAL id, collection_fk, last_name(?), first_name(?), username)
        // get the id of the new user, put it into a User, return it
        return null;
    }

    public User get(int id){
        return null;
    }

    public User getByUsername(String username) {
        // get all user information where the username matches, we are having usernames be unique
        // put that information into a User instance and return it

        // String sql = "SELECT id FROM user_info WHERE username = %s;";
        // ArrayList<Object> var = new ArrayList<>();
        // var.add(username);
        // ResultSet rs = jdbcRead.executePreparedSQL(sql, var);
        // try {
        //     return new User(rs.getInt("id"), username);
        // } catch (SQLException e) {
        //     System.out.println(e);
        //     return null;
        // }

        // mock return data
        return new User(2, username);
    }
}