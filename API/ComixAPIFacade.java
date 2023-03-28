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
        comixAPI = guestComixAPI;
    }
    
    
    public User authenticate(String username) { 
        User user = userController.getByUsername(username);
        if (user != null)
        {
            this.comixAPI = new UserComixAPI(user.getId());
        }
        else
        {
            user = new User(-1, username);
            userController.create(user);
            this.comixAPI = new UserComixAPI(user.getId());
        }
        return user;
    }
    
    public void unAuthenticate() {
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