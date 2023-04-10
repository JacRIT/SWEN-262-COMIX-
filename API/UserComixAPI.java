package Api;

import java.util.HashMap;
import java.util.Map;

import Controllers.ComicController;
import Controllers.UserController;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Signature;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.Search.ConcreteSorts.BrowseSort;

public class UserComixAPI implements ComixAPI {
    private ComicController comicController;
    private UserController userController;

    public UserComixAPI() throws Exception {
        this.comicController = new ComicController();
        this.userController = new UserController();
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
    public Map<String, Float> generateStatistics(User user) throws Exception {
        if (userExists(user)) {
            Comic[] comicsInPersonalCollection = this.browsePersonalCollection(user.getId());
            Map<String, Float> statistics = new HashMap<String, Float>();
            float personalCollectionValue = 0;
            for (Comic comic : comicsInPersonalCollection) {
                float calculatedComicValue = comic.getValue();
                personalCollectionValue += calculatedComicValue;
            }
            statistics.put("Number Of Issues", (float) (Integer) comicsInPersonalCollection.length);
            statistics.put("value", personalCollectionValue);
            return statistics;
        } else {
            System.out.println("\n======\n USER DOES NOT EXIST \n======\n");
            return null;
        }
    }

    @Override
    public Integer createComic(int userId, Comic comic) throws Exception {
        return comicController.create(userId, comic);
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
    public Signature signComic(Signature signature, Comic comic) throws Exception {
        comic.addSignature(signature);
        return comicController.addSignature(comic.getCopyId(), signature);
    }

    @Override
    public Boolean unSignComic(Signature signature, Comic comic) throws Exception {
        if (signatureExists(signature, comic)) {
            comicController.removeSignature(signature);
        } else {
            return false;
        }
        return false;
    }

    @Override
    public Signature verifyComic(Signature signature, Comic signedComic) throws Exception {
        if (signatureExists(signature, signedComic)) {
            signedComic.verifyComic(signature);
            comicController.updateComic(signedComic);
            signature.setAuthenticated(true);
            return signature;
        }
        return null;
    }

    @Override
    public Boolean unVerifyComic(Signature signature, Comic signedComic) throws Exception {
        if (signatureExists(signature, signedComic)) {
            signedComic.unVerifyComic(signature);
            comicController.removeSignature(signature);
            return true; 
        }
        return false;
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

    /**
     * @param user user being tested
     * @return True : User Exists
     *         False : User Does Not Exist
     */
    private Boolean userExists(User user) throws Exception {
        return userController.get(user.getId()) != null;
    }

    /**
     * @param comic copy being tested
     * @return True : Comic Exists
     *         False : Comic Does not exist
     */
    private Boolean copyExists(Comic copy) throws Exception {
        if (comicController.get(copy.getCopyId()) != null ) {
            return true;
        } else {
            System.out.println("\n=======\nUSERAPI : The passed in SIGNATURE does not exist\n======\n");
            return false;
        }
    }

    /**
     * 
     * @param signature signature being tested
     * @param comic that signature is on
     * @return  True : Signature exists
     *          False : signature or copy does not exist
     * @throws Exception
     */
    private Boolean signatureExists(Signature signature, Comic comic) throws Exception {
        if (copyExists(comic)) {
            if (comic.getSignatures().contains(signature)) {
                return true;
            } else {
                System.out.println("\n=======\nUSERAPI : The passed in SIGNATURE does not exist\n======\n");
                return false;
            }
        } 
        return false;
    }
}