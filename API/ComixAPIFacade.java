import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

public class ComixAPIFacade implements ComixAPI{
    // ComicController ComixController;
    UserController UserController;
    private GuestComixAPI guestComixAPI;
    private UserComixAPI userComixAPI;
    ComixAPI comixAPI;

    public ComixAPIFacade() {
        comixAPI = guestComixAPI;
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        // TODO Auto-generated method stub
        comixAPI.setSearchStrategy(null);
        throw new UnsupportedOperationException("Unimplemented method 'setSortStrategy'");
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSearchStrategy'");
    }

    @Override
    public User authenticate(String username) {
        // TODO 
        // Use User Controller to see if user Exists if does then switch to userComixAPI with a user??
        // if user does not exist then create one and switch to userComixAPI with the new user created.

        //Notes :
        // Adding an un-authenticate/logout
        throw new UnsupportedOperationException("Unimplemented method 'authenticate'");
    }

    @Override
    public Comic[] executeSearch(int userId, Comic[] keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'executeSearch'");
    }

    @Override
    public Comic[] browse(String publisher, String series, String volume, String issue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'browse'");
    }

    @Override
    public float[] generateStatistics(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateStatistics'");
    }

    @Override
    public String createComic(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createComic'");
    }

    
}