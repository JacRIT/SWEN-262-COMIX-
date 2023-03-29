package Api;

import java.util.ArrayList;
import java.util.HashMap;

import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

public class ComixAPIFacade implements ComixAPI{
    // ComicController ComixController;
    private UserController userController;
    private GuestComixAPI guestComixAPI;
    private ComixAPI comixAPI;

    
    public ComixAPIFacade() {
        guestComixAPI = new GuestComixAPI();
        userComixAPI = new UserComixAPI();
        comixAPI = guestComixAPI;
        this.userController = new UserController();
    }
    

    /**
     * Authenticates a user by allowing access to extra functionality from ComixAPI once successfully authenticated.
     * @param username
     * @return  Success : User
     *          Fail    : Null
     */
    public User authenticate(String username) { 
        User user = userController.getByUsername(username);
        if (user != null)
        {
            this.comixAPI = new UserComixAPI(user.getId());
        }
        else
        {
           return null; 
        }
        return user;
    }

    /**
     * Creates and registers a new user.
     * You can now be authenticated with this new username.
     * @param username
     * @return a User object with the id and username of the newly created user
     */
    public User register(String username) {
        return userController.create(username);
    }
    
    public void logout() {
        comixAPI = guestComixAPI;
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.comixAPI.setSortStrategy(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        this.comixAPI.setSearchStrategy(searchStrategy);
    }

    @Override
    public Comic[] searchComics(Boolean isSearchingPersonalCollection, String keyword) {
        return comixAPI.searchComics(isSearchingPersonalCollection, keyword);
    }

    @Override
    public HashMap<String, HashMap<String, HashMap<String, ArrayList<Comic>>>> browsePersonalCollectionHierarchy() {
        return comixAPI.browsePersonalCollectionHierarchy();
    }

    @Override
    public float[] generateStatistics(User user) {
        return comixAPI.generateStatistics(user);
    }
    
    @Override
    public String createComic(Comic comic) {
        return comixAPI.createComic(comic);
    }
}