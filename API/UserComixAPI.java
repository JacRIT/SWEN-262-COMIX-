package Api;

import java.util.Map;

import Controllers.ComicController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.Search.ConcreteSorts.BrowseSort;

public class UserComixAPI implements ComixAPI {
    private ComicController comicController;

    public UserComixAPI() throws Exception {
        this.comicController = new ComicController();
        comicController.setSearch(new PartialKeywordSearch());
    }

    @Override
    public void setSortStrategy(SortAlgorithm sortStrategy) {
        this.comicController.setSort(sortStrategy);
    }

    @Override
    public void setSearchStrategy(SearchAlgorithm searchStrategy) {
        comicController.setSearch(searchStrategy);
    }

    @Override
    public Comic[] searchComics(int userId, String keyword) throws Exception {
        return comicController.search(userId, keyword);
    }

    @Override
    public Map<String, String> generateStatistics(User user) throws Exception {
        return comicController.getStatistics(user.getId());
    }

    @Override
    public String createComic(int userId, Comic comic) {
        comicController.create(userId, comic);
        return comic.getTitle();
    }

    @Override
    public Comic[] browsePersonalCollection(int userId) throws Exception {
        SearchAlgorithm browseSearchAlgorithm = new PartialKeywordSearch();
        browseSearchAlgorithm.setSort(new BrowseSort());

        SearchAlgorithm previousSearchAlgorithm = comicController.getSearch();

        comicController.setSearch(browseSearchAlgorithm);
        Comic[] personalCollection = comicController.search(userId, "");
        comicController.setSearch(previousSearchAlgorithm);

        return personalCollection;
    }

    @Override
    public Boolean signComic(Signature signature, Comic comic) throws Exception {
        comic.addSignature(signature);
        comicController.updateCopy(signature.getId(), comic);
        return true; // TODO : no checks implemented.
    }

    @Override
    public Boolean unSignComic(Signature signature, Comic comic) throws Exception {
        comic.removeSignature(signature);
        comicController.updateCopy(signature.getId(), comic);
        return true; // TODO : no checks implemented.

    }

    @Override
    public Boolean verifyComic(Signature signature, Comic signedComic) throws Exception {
        signedComic.verifyComic(signature);
        comicController.updateCopy(signature.getId(), signedComic);
        return true; // TODO : no checks implemented.
    }

    @Override
    public Boolean unVerifyComic(Signature signature, Comic signedComic) throws Exception {
        signedComic.unVerifyComic(signature);
        comicController.updateCopy(signature.getId(), signedComic);
        return true; // TODO : no checks implemented.
    }

    @Override
    public Boolean gradeComicInPersonalCollection(User user, Comic comic, int grade) throws Exception {
        if (comic.gradeComic(grade)) {
            comicController.updateCopy(user.getId(), comic);
            return true;
        } else {
            return false;
        }
        // TODO : some checks not implemented.
    }

    @Override
    public Boolean ungradeComicInPersonalCollection(User user, Comic comic) throws Exception {
        comic.unGradeComic();
        comicController.updateCopy(user.getId(), comic);
        return true; // TODO : no checks implemented.
    }

    @Override
    public Boolean slabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception {
        gradedComic.slabComic();
        comicController.updateCopy(user.getId(), gradedComic);
        return true; // TODO : no checks implemented.
    }

    @Override
    public Boolean unslabGradedComicInPersonalCollection(User user, Comic gradedComic) throws Exception {
        gradedComic.unSlabComic();
        comicController.updateCopy(user.getId(), gradedComic);
        return true; // TODO : no checks implemented.
    }

    @Override
    public Boolean addComicToPersonalCollection(User user, Comic comic) throws Exception {
        this.comicController.addToCollection(user.getId(), comic);
        return true; // TODO : no checks implemented.
    }

    @Override
    public Boolean removeComicFromPersonalCollection(User user, Comic comic) throws Exception {
        this.comicController.removeFromCollection(user.getId(), comic);
        return true; // TODO : no checks implemented.
    }

    public Comic getComic(int comicId) throws Exception {
        return this.comicController.get(comicId);
    }
}