package Api;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;

public class UserComixAPI implements ComixAPI {
    private SearchAlgorithm searchStrategy;
    private ComicController comicController;

    public UserComixAPI() throws Exception {
        this.comicController = new ComicController();
        this.searchStrategy = new ExactKeywordSearch();
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.searchStrategy.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        comicController.setSearch(searchStrategy);
    }

    @Override
    public Comic[] searchComics(String keyword) throws Exception {
        return comicController.search(1, keyword);
    }

    @Override
    public float[] generateStatistics(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateStatistics'");
    }

    @Override
    public String createComic(int userId, Comic comic) {
        comicController.create(userId, comic);
        return comic.getTitle();
    }

    @Override
    public Comic[] browsePersonalCollection(int userId, String publisher, String series, String volumes,
            String issue) throws Exception {
        //TODO : Use a special browse search so multiple fields are able to be searched at the same time.
        return comicController.search(userId, publisher);
    }
}