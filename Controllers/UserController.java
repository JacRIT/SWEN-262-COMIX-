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

    public User create(String username){
        // add a row to collection_info (SERIAL id, nickname)
        // get the id of the new collection
        String sql = "INSERT INTO collection_info (nickname) VALUES (?);";
        ArrayList<Object> vars = new ArrayList<>();
        vars.add(username+"'s Personal Collection");
        int collection_id = jdbcInsert.executePreparedSQLGetId(sql, vars);
        System.out.println("collection done");
        // add a row to user_info (SERIAL id, collection_fk, last_name(?), first_name(?), username)
        sql = "INSERT INTO user_info (collection_fk, username) VALUES (?, ?);";
        vars.clear();
        vars.add(collection_id);
        vars.add(username);
        int user_id = jdbcInsert.executePreparedSQLGetId(sql, vars);
        // get the id of the new user, put it into a User, return it
        return new User(user_id, username);
    }

    public User get(int id){
        /*Get a user using the id */
        return null;
    }

    public User getByUsername(String username) {
        // get the id where the username matches, we are having usernames be unique
        // put that information into a User instance and return it

        // String sql = "SELECT id FROM user_info WHERE username = ?;";
        // ArrayList<Object> var = new ArrayList<>();
        // var.add("username");
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

    public static void main(String[] args) {
        UserController uc = new UserController();
        // User user = uc.create("fred");
        User user = uc.getByUsername("fred");
        if (user != null)
            System.out.println(user.getId()+" "+user.getName());
        else
            System.out.println("User is null");
    }
}