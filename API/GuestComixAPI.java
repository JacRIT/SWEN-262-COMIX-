package Api;

import java.util.HashMap;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.PartialKeywordSearch;

public class GuestComixAPI implements ComixAPI {
    private ComicController comicController;

    public GuestComixAPI() throws Exception {
        this.comicController = new ComicController();
        this.comicController.setSearch(new PartialKeywordSearch());
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
    public Comic[] searchComics(String keyword) throws Exception {
        return comicController.search(1, keyword);
    }

    @Override
    public HashMap<String, Float> generateStatistics(User user) {
        return null;
    }

    @Override
    public String createComic(int userId, Comic comic) {
        comicController.create(userId, comic);
        return comic.getTitle();
    }

    @Override
    public Comic[] browsePersonalCollection(int userId) {
        return null;
    }

	@Override
	public Boolean signComic(Comic comic) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'signComic'");
	}

	@Override
	public Boolean verifyComic(Comic signedComic) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'verifyComic'");
	}
}