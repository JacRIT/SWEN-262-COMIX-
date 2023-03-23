import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

public class UserComixAPI implements ComixAPI {

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSortStrategy'");
    }
    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSearchStrategy'");
    }
    @Override
    public User authenticate(String username) {
        // TODO Auto-generated method stub
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
    @Override
    public User unAuthenticate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unAuthenticate'");
    }


}
