package Controllers;
import Model.JavaObjects.User;

public class UserController{

    public void create (User newUser){

    }

    public User get(int id){
        return null;
    }

    public User getByUsername(String username) {
        // get all user information where the username matches, we are having usernames be unique
        // put that information into a User instance and return it

        // mock return data
        return new User(2, username);
    }

}