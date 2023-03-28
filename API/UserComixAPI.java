package Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.JavaObjects.Comic;
import Model.JavaObjects.Publisher;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;

public class UserComixAPI implements ComixAPI {
    private SearchAlgorithm searchStrategy; 
    private ComicController comicController;
    private int noUser = -1;
    private int userId;

    public UserComixAPI(int userId) {
        this.searchStrategy = new ExactKeywordSearch();
        this.userId = userId;
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
    public Comic[] searchComics(Boolean isSearchingPersonalCollection, String keyword) {
        if (isSearchingPersonalCollection) {
            return comicController.search(this.userId, keyword);
        } else {
            return comicController.search(noUser, keyword);
        }
    }
    @Override
    public HashMap<String, HashMap<String, HashMap<String, ArrayList<Comic>>>> browsePersonalCollectionHierarchy() {
        Comic[] personalCollection = comicController.search(this.userId, "");

        HashMap<String, HashMap<String, HashMap<String, ArrayList<Comic>>>> 
            personalCollectionHierarchy = comicsArrayToHierarchy(personalCollection);
        
        return personalCollectionHierarchy;
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

    /**
     * Converts a list of comics into the hierarchy described below.
     * @param comics
     * @return hierarchy of comics
     * Publisher
     *   - Series
     *     - Volume
     *       - Issue
     */
    private HashMap<String, HashMap<String, HashMap<String, ArrayList<Comic>>>> comicsArrayToHierarchy(Comic[] comics) {
        HashMap<String, HashMap<String, HashMap<String, ArrayList<Comic>>>> hierarchyPublishers = new HashMap<String, HashMap<String, HashMap<String, ArrayList<Comic>>>>();
        for (Comic comic : comics) {
            ArrayList<Publisher> comicPublishers = comic.getPublisher();
            String comicSeries = comic.getSeries();
            String comicVolume = Integer.toString(comic.getVolumeNumber());
            String comicIssue = comic.getIssueNumber();

            for (Publisher comicPublisher : comicPublishers) {
                hierarchyPublishers.put(comicPublisher.getName(), new HashMap<String, HashMap<String, ArrayList<Comic>>>());
                HashMap<String, HashMap<String, ArrayList<Comic>>> hierarchySeries = hierarchyPublishers.get(comicPublisher.getName());

                hierarchySeries.put(comicSeries, new HashMap<String, ArrayList<Comic>>());
                HashMap<String, ArrayList<Comic>> hierarchyVolumes = hierarchySeries.get(comicSeries);

                hierarchyVolumes.put(comicVolume, new ArrayList<Comic>());
                ArrayList<Comic> hieararchyComics = hierarchyVolumes.get(comicVolume);

                hieararchyComics.add(comic);
            }
        }
        return hierarchyPublishers;
    }
}
