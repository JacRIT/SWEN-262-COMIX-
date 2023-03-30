package Api ;
import Model.JavaObjects.Comic;
import Model.JavaObjects.User;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

interface ComixAPI
{
    /**
     * Sets the sort strategy of the current search strategy.
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
     * Executes a search on all comics in database given a keyword.
     * 
     * @param keyword word being searched.
     * @return list of comics that match the search executed
     */
    Comic[] searchComics(String keyword);

    /**
     * 
     * @param userId Determines whether a personal collection is being searched within the database
     *          if userId == 1 then it well search all comics
     *          if userId > 1 then it will search the personal collection of the user with that id.
     * @param publish
     * @param series
     * @param volumes
     * @param issue
     * @return All the comics in a given personal collection from the given parameters sorted by publisher, series, volumes, and issue#
     */
    Comic[]  browsePersonalCollection(int userId, String publisher, String series, String volume,  String issue);

    /**
     * TODO : Figure out specific requirements on what statistics need to be generated.
     * @param user
     * @return
     */
    float[] generateStatistics(User user);

    /**
     * Creates a comic in the database.
     * @param comic comic being created
     * @return Name of comic created.
     */
    String createComic (Comic comic);
    
}