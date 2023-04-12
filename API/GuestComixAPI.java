package Api;

import java.util.Map;

import Controllers.ComicController;
import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.PartialKeywordSearch;

public class GuestComixAPI implements ComixAPI {
    private ComicController comicController;
    private UserController userController;
    private UserComixAPI userComixAPI;
    private Boolean isAuthenticated;

    public GuestComixAPI() throws Exception {
        this.comicController = new ComicController();
        this.userController = new UserController();
        this.userComixAPI = new UserComixAPI();
        this.comicController.setSearch(new PartialKeywordSearch());
        this.isAuthenticated = false;
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
            this.isAuthenticated = true;
        } else {
            this.isAuthenticated = false;
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
        this.isAuthenticated = false;
    }
    
    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.comicController.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        this.comicController.setSearch(searchStrategy);
    }

    @Override
    public Comic[] searchComics(int userId, String keyword) throws Exception {
        return comicController.search(1, keyword);
    }
    
    @Override
    public Integer createComic(int userId, Comic comic) throws Exception {
        return comicController.create(userId, comic);
    }


    @Override
    public Map<String, Float> generateStatistics(User user) throws Exception{
        if (isAuthenticated) {
            return userComixAPI.generateStatistics(user);
        } else {
            return null;
        }
    }


    @Override
    public Comic[] browsePersonalCollection(int userId) throws Exception {
        if (isAuthenticated) {
            return userComixAPI.browsePersonalCollection(userId);
        } else {
            return new Comic[0];
        }
    }

    
    
    @Override
    public Signature signComic(Signature signature, Comic comic) throws Exception {
        if(isAuthenticated) {
            return userComixAPI.signComic(signature, comic);
        } else {
            return null;
        }
    }
    @Override
    public Boolean unSignComic(Signature signature, Comic comic) throws Exception{
        if (isAuthenticated) {
            return userComixAPI.unSignComic(signature, comic);
        } else {
            return false;
        }
    }

    @Override
    public Signature verifyComic(Signature signature, Comic signedComic) throws Exception {
        if (isAuthenticated) {
            return userComixAPI.verifyComic(signature, signedComic);
        } else {
            return null;
        }
    }
    @Override
    public Signature unVerifyComic(Signature signature, Comic signedComic) throws Exception{
        if (isAuthenticated) {
            return userComixAPI.unVerifyComic(signature, signedComic);
        } else {
            return null;
        }
    }
    
	@Override
	public Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade) throws Exception {
        if (isAuthenticated) {
            return userComixAPI.gradeComicInPersonalCollection(user, comic, grade);
        } else {
            return false;
        }
	}
    
	@Override
	public Boolean ungradeComicInPersonalCollection(User user, Comic comic) throws Exception{
        if (isAuthenticated) {
            return userComixAPI.ungradeComicInPersonalCollection(user, comic);
        } else {
            return false;
        }
	}
    
	@Override
	public Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception{
        if (isAuthenticated) {
            return userComixAPI.slabGradedComicInPersonalCollection(user, gradedComic);
        } else {
            return false;
        }
	}

	@Override
	public Boolean unslabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception {
        if (isAuthenticated) {
            return userComixAPI.unslabGradedComicInPersonalCollection(user, gradedComic);
        } else {
            return false;
        }
	}
    
	@Override
	public Integer addComicToPersonalCollection(User user, Comic comic) throws Exception {
        if (isAuthenticated) {
            return userComixAPI.addComicToPersonalCollection(user, comic);
        } else {
            return null;
        }
	}
    
	@Override
	public Boolean removeComicFromPersonalCollection(User user, Comic comic) throws Exception {
		if (isAuthenticated) {
            return userComixAPI.removeComicFromPersonalCollection(user, comic);
        } else {
            return false;
        }
	}

    public Comic getComic(int comicId) throws Exception {
        return this.comicController.get(comicId);
    }

}