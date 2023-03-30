package Controllers;
import Model.Search.SearchAlgorithm;

import org.postgresql.core.Tuple;

import Model.JavaObjects.*;

public class ComicController {

    private SearchAlgorithm searchStrategy;

    public void setSearch(SearchAlgorithm searchStrategy){
    }

    public Comic[] search(int userId, String searchTerm){
        return null;
    }

    public Comic get(int id){
        return null;
    }

    public void update(int userId, Comic comic, Comic updatedComic){
    
    }

    public void delete(int userId, Comic comic){

    }
    public void create(int userId, Comic comic, int collection_id){

    }
    public void addToCollection(int userId, int collectionId, Comic comic){

    }

    public void removeFromCollection(int userId, int collectionId, Comic comic){

    }
    public String getStatistics(int userId){
        return null;
    }

    private Comic[] loadResults(){
        return null;
    }


}
