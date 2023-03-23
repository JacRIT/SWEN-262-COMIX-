import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

interface ComixAPI
{
    /**
     * Sets the sort strategy of the current search strategy.
     * 
     * IMPORTANT : this sort strategy may be set somewhere else meaning that this will not need
     * to be called
     * @param strategy the strategy being used bt the user.
     */
    void setSortStrategy (SortAlgorithm sortStrategy);

    /**
     * Sets search strategy of Comic Controller allowing it to use the proper search strategy 
     * to search comics.
     * @param strategy the strategy being used by the user.
     */
    void setSearchStrategy (SearchAlgorithm searchStrategy);


    /**
     * Executes a given search for a user given a keyword.
     * The implementation for search methods is within each search strategy.
     * @param userId id of user.
     * @param keyword word being searched.
     * @return
     */
    Comic[] executeSearch(int userId, Comic[] keyword);

    /**
     * Executes a search of the Comic Database given any of the parameters and returns
     * results based on the strategy of search and sort.
     * TODO : Contact J about a search/sort implementation that takes in these parameters?
     * @param publisher 
     * @param series
     * @param volume
     * @param issue
     * @return
     */
    Comic[] browse (String publisher, String series, String volume, String issue);

    /**
     * TODO : Figure out specific requirements on what statistics need to be generated.
     * @param user
     * @return
     */
    float[] generateStatistics(User user);

    /**
     * Creates a comic in the database.
     * @param comic
     * @return
     */
    String createComic (Comic comic);
    
}