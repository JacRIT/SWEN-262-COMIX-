package Api;

import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

public class ComixAPIFacade implements ComixAPI{
    // ComicController ComixController;
    private UserController userController;
    private GuestComixAPI guestComixAPI;
    private UserComixAPI userComixAPI;
    private ComixAPI comixAPI;

    private SearchAlgorithm searchStrategy; 
    
    public ComixAPIFacade() {
        comixAPI = guestComixAPI;
    }
    
    
    public User authenticate(String username) { 
        User user = userController.getByUsername(username);
        if (user != null)
        {
            this.comixAPI = userComixAPI;
        }
        else
        {
            user = new User(-1, username);
            userController.create(user);
            this.comixAPI = userComixAPI;
        }
        return user;
    }
    
    public void unAuthenticate() {
        comixAPI = guestComixAPI;
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.searchStrategy.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        this.searchStrategy = searchStrategy;
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