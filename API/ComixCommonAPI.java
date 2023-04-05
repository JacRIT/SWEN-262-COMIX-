package Api;

import java.util.Map;

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
    public Comic[] searchComics(int userId, String keyword) throws Exception {
        return comixAPI.searchComics(userId, keyword);
    }

    @Override
    public Map<String, String> generateStatistics(User user) {
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

    @Override
    public Boolean signComic(Comic comic) {
        return this.comixAPI.signComic(comic);
    }

    @Override
    public Boolean verifyComic(Comic signedComic) {
        return this.comixAPI.verifyComic(signedComic);
    }

    @Override
    public Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gradeComicInPersonalCollection'");
    }

    @Override
    public Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'slabGradedComicInPersonalCollection'");
    }

    @Override
    public Boolean addComicToPersonalCollection(User user, Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addComicToPersonalCollection'");
    }

    @Override
    public Boolean removeComicFromPersonalCollection(User user, Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeComicFromPersonalCollection'");
    }
}