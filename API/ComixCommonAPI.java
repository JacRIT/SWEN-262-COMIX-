package Api;

import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

public class ComixCommonAPI implements ComixAPI {
    private UserController userController;
    private GuestComixAPI guestComixAPI;
    private UserComixAPI userComixAPI;
    private ComixAPI comixAPI;

    public ComixCommonAPI() throws Exception {
        this.guestComixAPI = new GuestComixAPI();
        this.userComixAPI = new UserComixAPI();
        this.userController = new UserController();
        this.comixAPI = guestComixAPI;
    }

    /**
     * Authenticates a user by allowing access to extra functionality from ComixAPI
     * once successfully authenticated.
     * 
     * @param username
     * @return Success : User
     *         Fail : Null
     */
    public User authenticate(String username) {
        User user = userController.getByUsername(username);
        if (user != null) {
            this.comixAPI = userComixAPI;
        } else {
            return null;
        }
        return user;
    }

    /**
     * Creates and registers a new user.
     * You can now be authenticated with this new username.
     * 
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
    public Comic[] searchComics(String keyword) throws Exception {
        return comixAPI.searchComics(keyword);
    }

    @Override
    public float[] generateStatistics(User user) {
        return comixAPI.generateStatistics(user);
    }

    @Override
    public String createComic(int userId, Comic comic) {
        return comixAPI.createComic(userId, comic);
    }

    @Override
    public Comic[] browsePersonalCollection(int userId) throws Exception {
        return comixAPI.browsePersonalCollection(userId);
    }
}