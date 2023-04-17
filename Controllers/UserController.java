package Controllers;

import java.util.ArrayList;

import Controllers.Utils.JDBCInsert;
import Controllers.Utils.JDBCRead;
import Model.JavaObjects.User;

public class UserController {
    JDBCRead jdbcRead;
    JDBCInsert jdbcInsert;

    public UserController() {
        this.jdbcRead = new JDBCRead();
        this.jdbcInsert = new JDBCInsert();
    }

    /**
     * Creates a new user in the database with the given username.
     * Involves creating a new row in user_info and in collection_info.
     * Does not check for the username already existing in the database.
     * 
     * @param username the username of the user being added to the database
     * @return a User object with the id of the new user entry in the database and
     *         the given username
     * @throws Exception
     */
    public User create(String username) throws Exception {
        // add a row to collection_info (SERIAL id, nickname)
        // get the id of the new collection
        String sql = "INSERT INTO collection_info (nickname) VALUES (?);";
        ArrayList<Object> vars = new ArrayList<>();
        vars.add(username + "'s Personal Collection");
        int collection_id = jdbcInsert.executePreparedSQLGetId(sql, vars);
        // System.out.println("collection done");

        // add the new collection to its own subcollection (for reursive searching)
        vars.clear();
        vars.add(collection_id);
        vars.add(collection_id);
        sql = "INSERT INTO subcollection_refrence(collect_fk, subcollect_fk) VALUES (?, ?)";
        jdbcInsert.executePreparedSQLGetId(sql, vars);

        // add a row to user_info (SERIAL id, collection_fk, last_name(?),
        // first_name(?), username)
        sql = "INSERT INTO user_info (collection_fk, username) VALUES (?, ?);";
        vars.clear();
        vars.add(collection_id);
        vars.add(username);
        int user_id = jdbcInsert.executePreparedSQLGetId(sql, vars);
        // get the id of the new user, put it into a User, return it
        return new User(user_id, username);
    }

    /**
     * Creates a User object for the user with the given id.
     * 
     * @param id the id in the database of the user whose information is being
     *           retrieved
     * @return a User oject with the id and username for the user with the given id
     * @throws Exception
     */
    public User get(int id) throws Exception {
        /* Get a user using the id */
        String sql = "SELECT id, username FROM user_info WHERE id = ?;";
        ArrayList<Object> var = new ArrayList<>();
        var.add(id);
        ArrayList<ArrayList<Object>> result = jdbcRead.readListofLists(sql, var, 2); // should be [id, username]
        if (result.size() == 1)
            return new User((int) result.get(0).get(0), (String) result.get(0).get(1));
        else
            return null;
    }

    /**
     * Creates a User object for the user with the given username.
     * Usernames are assumed to be unique.
     * 
     * @param username the username of the user being retrieved from the database
     * @return a User object with the matching id for the username given
     * @throws Exception
     */
    public User getByUsername(String username) throws Exception {
        String sql = "SELECT id FROM user_info WHERE username = ?;";
        ArrayList<Object> var = new ArrayList<>();
        var.add(username);
        ArrayList<Object> result = jdbcRead.executePreparedSQL(sql, var); // should be [id]
        if (result.size() == 1)
            return new User((int) result.get(0), username);
        else
            return null;
    }
}
