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
     * Executes a search given a keyword.
     * Note : The user must be logged in to search a Personal Collection
     * Note : The implementation for search methods is within each search strategy.
     * 
     * @param isSearchingPersonalCollection whether or not the personal Collection is being searched. 
     * If the personal collection is not being searched, all comics will be searched.
     * 
     * @param keyword word being searched.
     * @return list of comics that match the search executed
     */
    Comic[] searchComics(int userId, String keyword);

    /**
     * Constructs a hierarchy for the personal collection.
     * Ex: ArrayList<Comic> comicsInPersonalCollection = personalCollectionHierarchy.get("publisher").get("series").get("volume");
     * Note : instead of issue being the last tier, a Comic is the last tier, but the issue can be gotten from a comic.
     * @param publisher publisher in personal collection.
     * @param series series in personal collection.
     * @param volume volume in personal collection.
     * @param issue issue in personal collection.
     * @return  A personal collection.
     */
    Comic[]  browsePersonalCollection(int userId, String publish, String series, String volumes,  String issue);

    /**
     * TODO : Figure out specific requirements on what statistics need to be generated.
     * @param user
     * @return
     */
    float[] generateStatistics(User user);

    /**
     * Creates a comic in the database.
     * @param comic comic being created
     * @return TODO : why is this returning a string?
     */
    String createComic (Comic comic);
    
}