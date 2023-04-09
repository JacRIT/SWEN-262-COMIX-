package Api;

import java.util.Map;

import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
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
        this.comixAPI = this.guestComixAPI;
    }

    /**
     * Authenticates a user by allowing access to extra functionality from ComixAPI
     * once successfully authenticated.
     * 
     * @param username
     * @return Success : User
     *         Fail : Null
     * @throws Exception
     */
    public User authenticate(String username) throws Exception {
        User user = userController.getByUsername(username);
        if (user != null) {
            this.comixAPI = this.userComixAPI;
        } else {
            return null;
        }
        return user;
    }

    /**
     * Creates and registers a new user.
     * You can now be authenticated with this new username.
     * @param username
     * @return a User object with the id and username of the newly created user
     * @throws Exception
     */
    public User register(String username) throws Exception {
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
        return this.comixAPI.searchComics(userId, keyword);
    }

    @Override
    public Map<String, String> generateStatistics(User user) throws Exception {
        return this.comixAPI.generateStatistics(user);
    }

    @Override
    public String createComic(int userId, Comic comic) throws Exception {
        return this.comixAPI.createComic(userId, comic);
    }

    @Override
    public Comic[] browsePersonalCollection(int userId) throws Exception {
        return this.comixAPI.browsePersonalCollection(userId);
    }

    @Override
    public Boolean signComic(Signature signature, Comic comic) throws Exception {
        return this.comixAPI.signComic(signature, comic);
    }

    @Override
    public Boolean verifyComic(Signature signature, Comic signedComic) throws Exception {
        return this.comixAPI.verifyComic(signature, signedComic);
    }

    @Override
    public Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade) throws Exception {
        return this.comixAPI.gradeComicInPersonalCollection(user, comic, grade);
    }

    @Override
    public Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception {
        return this.comixAPI.slabGradedComicInPersonalCollection(user, gradedComic);
    }

    @Override
    public Boolean addComicToPersonalCollection(User user, Comic comic) throws Exception {

        System.out.println("");
        System.out.println("Add to Pc api: " + this.comixAPI.getClass());
        System.out.println("");

        return this.comixAPI.addComicToPersonalCollection(user, comic);
    }

    @Override
    public Boolean removeComicFromPersonalCollection(User user, Comic comic) throws Exception {
        return this.comixAPI.removeComicFromPersonalCollection(user, comic);
    }

    @Override
    public Boolean unSignComic(Signature signature, Comic comic) throws Exception {
        return this.comixAPI.unSignComic(signature, comic);
    }

    @Override
    public Boolean unVerifyComic(Signature signature, Comic signedComic) throws Exception {
        return this.comixAPI.unVerifyComic(signature, signedComic);
    }

    @Override
    public Boolean ungradeComicInPersonalCollection(User user, Comic comic) throws Exception {
        return this.comixAPI.ungradeComicInPersonalCollection(user, comic);
    }

    @Override
    public Boolean unslabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception {
        return this.comixAPI.unslabGradedComicInPersonalCollection(user, gradedComic);
    }

    @Override
    public Comic getComic(int comicId) throws Exception {
        return this.comixAPI.getComic(comicId);
    }
}