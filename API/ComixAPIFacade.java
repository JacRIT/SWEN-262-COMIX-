package Api;

import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

public class ComixAPIFacade implements ComixAPI{
    // ComicController ComixController;
    UserController userController;
    private GuestComixAPI guestComixAPI;
    private UserComixAPI userComixAPI;
    ComixAPI comixAPI;
    
    public ComixAPIFacade() {
        guestComixAPI = new GuestComixAPI();
        userComixAPI = new UserComixAPI();
        comixAPI = guestComixAPI;
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
            this.comixAPI = userComixAPI;
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
     */
    public void register(String username) {
        User user = new User(-1, username);
        userController.create(user);
    }
    
    public void logout() {
        comixAPI = guestComixAPI;
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        comixAPI.setSearchStrategy(null);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        comixAPI.setSearchStrategy(searchStrategy);
    }

    @Override
    public Comic[] executeSearch(int userId, Comic[] keyword) {
        return comixAPI.executeSearch(userId, keyword);
    }

    @Override
    public Comic[] browse(String publisher, String series, String volume, String issue) {
        return comixAPI.browse(publisher, series, volume, issue);
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