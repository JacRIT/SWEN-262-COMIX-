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
    ComixAPI comixAPI;
    
    public ComixAPIFacade() {
        
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
            user = userController.create(username);
            this.comixAPI = new UserComixAPI(user.getId());
        }
        return user;
    }
    
    public void unAuthenticate() {
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