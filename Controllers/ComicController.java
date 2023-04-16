package Controllers;

import java.util.ArrayList;

import Controllers.Utils.ComicImporter;
import Controllers.Utils.JDBCComicExtractor;
import Controllers.Utils.JDBCInsert;
import Controllers.Utils.JDBCRead;
import Controllers.Utils.JDBCUpdate;
import Controllers.Utils.PreparedStatementContainer;
import Controllers.Utils.FileAdapters.CSVComicAdapter;
import Controllers.Utils.FileAdapters.ComicConverter;
import Controllers.Utils.FileAdapters.JSONComicAdapter;
import Controllers.Utils.FileAdapters.XMLComicAdapter;
import Controllers.Utils.FileAdapters.Adaptees.CSV;
import Controllers.Utils.FileAdapters.Adaptees.JSON;
import Controllers.Utils.FileAdapters.Adaptees.XML;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Creator;
import Model.JavaObjects.Publisher;
import Model.JavaObjects.Signature;
import Model.Search.SearchAlgorithm;
import Model.Search.SortAlgorithm;

public class ComicController {
    JDBCRead jdbcRead;
    JDBCInsert jdbcInsert;
    JDBCComicExtractor jdbcComicExtractor;

    public ComicController() throws Exception {
        this.jdbcRead = new JDBCRead();
        this.jdbcInsert = new JDBCInsert();
        this.jdbcComicExtractor = new JDBCComicExtractor();
    }

    private SearchAlgorithm searchStrategy;

    /**
     * Sets the search strategy with the given param
     * 
     * @param searchStrategy The strategy chosen for a search
     */
    public void setSearch(SearchAlgorithm searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public SearchAlgorithm getSearch() {
        return this.searchStrategy;
    }

    /**
     * Sets the search strategy with the given param
     * 
     * @param sortStrategy The strategy chosen for a search
     */
    public void setSort(SortAlgorithm sortStrategy) {
        this.searchStrategy.setSort(sortStrategy);
    }

    /**
     * Searches the database for all comics matching the search term owned by the
     * given user.
     * Based of the search set on the instance of ComicController, and the sort set
     * on that search instance.
     * 
     * @param userId     the user id to identify the collection
     * @param searchTerm the search term to look for
     * @return an array of Comics matching the search request, sorted
     */
    public Comic[] search(int userId, String searchTerm) throws Exception {
        int[] copy_ids = this.searchStrategy.executeSearch(userId, searchTerm);
        Comic[] comics = new Comic[copy_ids.length];
        int i = 0;

        for (Object o : copy_ids) {
            int copy_id = (int) o;
            Comic c = get(copy_id);
            comics[i++] = c;
        }
        return comics;
    }

    /**
     * Gets the comic from the the database
     * 
     * @param id the id of the COPY
     * @return the comic
     */
    public Comic get(int id) throws Exception {
        return jdbcComicExtractor.getComicFromCopyId(id);
    }

    /**
     * Updates the comic (comic_info table) to reflect the given updated version of
     * the comic.
     * The updated version of the comic should not have any id fields changed from
     * the original.
     * Any information in fields specific to a copy will be ignored.
     * Only manually created comics can be edited.
     * 
     * @param updatedComic the new comic replacing the old data, must have correct
     *                     comic id
     * @return true if the comic was updated, false if the comic was not manually
     *         created and not updated
     * @throws Exception
     */
    public boolean updateComic(Comic updatedComic) throws Exception {
        // check to make sure comic is not in the database-> if it is, return false
        String sql = """
                SELECT comic_ownership.id FROM comic_ownership
                INNER JOIN collection_refrence ON collection_refrence.copy_fk = comic_ownership.id
                WHERE comic_ownership.comic_fk = ? AND collection_refrence.collection_fk = ?""";
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(updatedComic.getId());
        obj.add(getCollectionIdFromUser(1));
        ArrayList<Object> results = jdbcRead.executePreparedSQL(sql, obj);
        if (results.size() > 0)
            return false;
        // otherwise, update comic_info fields
        sql = """
                UPDATE comic_info
                SET
                    series = ?,
                    title = ?,
                    volume_num = ?,
                    issue_num = ?,
                    initial_value = ?,
                    descrip = ?,
                    release_date = ?,
                    release_day = ?,
                    release_month = ?,
                    release_year = ?
                WHERE id = ?
                """;
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(updatedComic.getSeries());
        psc.appendToObjects(updatedComic.getTitle());
        psc.appendToObjects(updatedComic.getVolumeNumber());
        psc.appendToObjects(updatedComic.getIssueNumber());
        psc.appendToObjects(updatedComic.getInitialValue());
        psc.appendToObjects(updatedComic.getDescription());
        psc.appendToObjects(updatedComic.getPublicationDate());
        psc.appendToObjects(updatedComic.getReleaseDay());
        psc.appendToObjects(updatedComic.getReleaseMonth());
        psc.appendToObjects(updatedComic.getReleaseYear());
        psc.appendToObjects(updatedComic.getId());
        jdbcInsert.executePreparedSQL(psc);
        return true;
    }

    /**
     * Updates the copy (grade, slabbed) to reflect the given updated
     * version of the copy.
     * This method does not update signatures.
     * The updated version of the comic should not have any id fields changed from
     * the original.
     * Any information in fields not specific to a copy will be ignored.
     * 
     * @param userId      the id of the user who owns the copy to be updated
     * @param updatedCopy a Comic instance containing the copy data to overwrite the
     *                    current data
     * @return true if the user owns a copy with the given copy id and false if they
     *         did not and no changes were made
     * @throws Exception
     */
    public boolean updateCopy(int userId, Comic updatedCopy) throws Exception {
        // check to make sure the copy is owned by the given user by getting the
        // collection id
        // copy id should
        String sql = """
                SELECT COUNT(*) FROM comic_ownership
                INNER JOIN collection_refrence ON collection_refrence.copy_fk = comic_ownership.id
                WHERE collection_refrence.collection_fk = ? AND comic_ownership.id = ?;
                """;
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(getCollectionIdFromUser(userId));
        obj.add(updatedCopy.getCopyId());
        ArrayList<Object> result = jdbcRead.executePreparedSQL(sql, obj);
        if ((long) result.get(0) == 0l)
            return false;
        // update sql calls
        // grade and slabbed are in comic_ownership
        sql = """
                UPDATE comic_ownership
                SET grade = ?, slabbed = ?
                WHERE id = ?
                """;
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(updatedCopy.getGrade());
        psc.appendToObjects(updatedCopy.isSlabbed());
        psc.appendToObjects(updatedCopy.getCopyId());
        jdbcInsert.executePreparedSQL(psc);
        return true;
    }

    /**
     * Adds a signature to a copy of a comic.
     * 
     * @param copyId the id of the copy the signature is being added to
     * @param s      the Signature being added to the copy
     * @return the Signature that was added with the updated id
     * @throws Exception
     */
    public Signature addSignature(int copyId, Signature s) throws Exception {
        // signatures split between signature_refrence and signature_info
        // add entry to signature_info
        String sql = """
                INSERT INTO signature_info (s_name, authenticated)
                VALUES (?, ?)
                """;
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(s.getName());
        obj.add(s.isAuthenticated());
        int id = jdbcInsert.executePreparedSQLGetId(sql, obj);
        System.out.println("\tinfo insert done");
        // add entry to signature_refrence
        sql = """
                INSERT INTO signature_refrence (signature_fk, copy_fk)
                VALUES (?, ?)
                """;
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(id);
        psc.appendToObjects(copyId);
        jdbcInsert.executePreparedSQL(psc);
        System.out.println("\trefrence insert done");
        s.setId(id);
        return s;
    }

    /**
     * Removes the given signature from the database and its copy.
     * 
     * @param s the Signature being removed (id must be correct)
     * @throws Exception
     */
    public void removeSignature(Signature s) throws Exception {
        // remove from signature_refrence
        String sql = "DELETE FROM signature_refrence WHERE signature_fk = ?";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(s.getId());
        jdbcInsert.executePreparedSQL(psc);
        // remove fro signature_info
        sql = "DELETE FROM signature_info WHERE id = ?";
        psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(s.getId());
        jdbcInsert.executePreparedSQL(psc);
    }

    /**
     * 
     * Deletes the copy, collection reference, AND comic info.
     * Will not allow deletion of a comic in the database (not manually created).
     * Does not delete now unused entries in publisher_info or creator_info.
     * 
     * @param userId the userId of the collection the comic is in
     * @param comic  The comic to be deleted
     * @throws Exception
     */
    public boolean delete(int userId, Comic comic) throws Exception {
        // checks if the comic is in the database
        String sql = """
                SELECT comic_ownership.id FROM comic_ownership
                INNER JOIN collection_refrence ON collection_refrence.copy_fk = comic_ownership.id
                WHERE comic_ownership.comic_fk = ? AND collection_refrence.collection_fk = ?""";
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(comic.getId());
        obj.add(getCollectionIdFromUser(1));
        ArrayList<Object> results = jdbcRead.executePreparedSQL(sql, obj);
        if (results.size() > 0)
            return false;
        // otherwise it is manually created and can be deleted
        // deletes singatures
        for (Signature s : comic.getSignatures()) {
            removeSignature(s);
        }
        // deletes collection_refrence
        String deleteRefSql = "DELETE FROM collection_refrence WHERE copy_fk = ?;";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(deleteRefSql);
        psc.appendToObjects(comic.getCopyId());
        jdbcInsert.executePreparedSQL(psc);
        // now deletes copy
        String deleteCopySql = "DELETE FROM comic_ownership WHERE id = ?";
        psc = new PreparedStatementContainer();
        psc.appendToSql(deleteCopySql);
        psc.appendToObjects(comic.getCopyId());
        jdbcInsert.executePreparedSQL(psc);
        // deletes comic_info
        String deleteComicSql = "DELETE FROM comic_info WHERE id = ?";
        psc = new PreparedStatementContainer();
        psc.appendToSql(deleteComicSql);
        psc.appendToObjects(comic.getId());
        jdbcInsert.executePreparedSQL(psc);
        return true;
    }

    public void importCollection(int userId, String filename, Boolean isPersonal) throws Exception {
        // run an addToCollection() call for each comic in the Array IF PERSONAL=True
        ComicConverter x = null;
        ComicImporter importer = new ComicImporter();
        if (filename.endsWith(".xml")) {
            XML xml = new XML(filename);
            x = new XMLComicAdapter(xml);

        } else if (filename.endsWith(".csv")) {
            CSV csv = new CSV(filename);
            x = new CSVComicAdapter(csv);

        } else if (filename.endsWith(".json")) {
            JSON json = new JSON(filename);
            x = new JSONComicAdapter(json);
        }
        Comic target = x.convertToComic();
        if (isPersonal == true) {
            addToCollection(userId, target);
        }

        while (target != null) {
            importer.changeTarget(target);
            importer.importComic();
            target = x.convertToComic();
            if (isPersonal == true) {
                addToCollection(userId, target);
            }
        }

    }

    public void exportCollection(int userId, String filename) throws Exception {
        // takes a collection and then exports, doesn't matter who is calling
        Comic[] collectionComics = getAllCollectionComics(userId);
        ComicConverter x = null;
        if (filename.endsWith(".xml")) {
            XML xml = new XML(filename);
            x = new XMLComicAdapter(xml);

        } else if (filename.endsWith(".csv")) {
            CSV csv = new CSV(filename);
            x = new CSVComicAdapter(csv);

        } else if (filename.endsWith(".json")) {
            JSON json = new JSON(filename);
            x = new JSONComicAdapter(json);
        }
        x.convertToFile(filename, collectionComics);
    }

    private Comic[] getAllCollectionComics(int userId) throws Exception {
        JDBCComicExtractor comicExtractor = new JDBCComicExtractor();
        String sql = """
                SELECT copy_fk FROM collection_reference
                INNER JOIN user_info ON user_info.collection_fk = collection_refrence.collection_fk
                WHERE user_info.id = ?
                """;
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(userId);
        Comic[] comics = comicExtractor.getComic(psc);
        return comics;
    }

    /**
     * Creates a comic and adds it to the user's personal collection.
     * 
     * @param userId the userId of the collection the comic will be in
     * @param comic  The comic to be inserted
     * @return the copy id of the initial copy the user owns of the new comic
     */
    public int create(int userId, Comic comic) throws Exception {
        // adding it to comic_info (comic)
        String sql = """
                INSERT INTO comic_info(series, title, volume_num, issue_num, initial_value, descrip, release_date, release_day, release_month, release_year)
                VALUES (?,?,?,?,?,?,?,?,?,?)
                """;
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(comic.getSeries());
        obj.add(comic.getTitle());
        obj.add(comic.getVolumeNumber());
        obj.add(comic.getIssueNumber());
        obj.add(comic.getInitialValue());
        obj.add(comic.getDescription());
        obj.add(comic.getPublicationDate());
        obj.add(comic.getReleaseDay());
        obj.add(comic.getReleaseMonth());
        obj.add(comic.getReleaseYear());
        int comicId = jdbcInsert.executePreparedSQLGetId(sql, obj);
        comic.setId(comicId);

        // publishers
        for (Publisher p : comic.getPublisher()) {
            // check if publisher exists in publisher_info already
            sql = """
                    SELECT id FROM publisher_info WHERE p_name = ?
                    """;
            obj = new ArrayList<>();
            obj.add(p.getName());
            ArrayList<Object> result = jdbcRead.executePreparedSQL(sql, obj);
            // if they don't exist, add to publisher_info and set the correct id
            if (result.size() == 0) {
                sql = """
                        INSERT INTO publisher_info (p_name) VALUES (?)
                        """;
                obj = new ArrayList<>();
                obj.add(p.getName());
                p.setId(jdbcInsert.executePreparedSQLGetId(sql, obj));
            }
            // add to publisher_refrence
            sql = """
                    INSERT INTO publisher_refrence (publisher_fk, comic_fk)
                    VALUES (?,?)
                    """;
            PreparedStatementContainer psc = new PreparedStatementContainer();
            psc.appendToSql(sql);
            psc.appendToObjects(p.getId());
            psc.appendToObjects(comic.getId());
            jdbcInsert.executePreparedSQL(psc);
        }

        // creators
        for (Creator c : comic.getCreators()) {
            // check if creator exists in creator_info already
            sql = """
                    SELECT id FROM creator_info WHERE c_name = ?
                    """;
            obj = new ArrayList<>();
            obj.add(c.getName());
            ArrayList<Object> result = jdbcRead.executePreparedSQL(sql, obj);
            // if they don't exist, add to creator_info and set the correct id
            if (result.size() == 0) {
                sql = """
                        INSERT INTO creator_info (c_name) VALUES (?)
                        """;
                obj = new ArrayList<>();
                obj.add(c.getName());
                c.setId(jdbcInsert.executePreparedSQLGetId(sql, obj));
            }
            // add to creator_refrence
            sql = """
                    INSERT INTO creator_refrence (creator_fk, comic_fk)
                    VALUES (?,?)
                    """;
            PreparedStatementContainer psc = new PreparedStatementContainer();
            psc.appendToSql(sql);
            psc.appendToObjects(c.getId());
            psc.appendToObjects(comic.getId());
            jdbcInsert.executePreparedSQL(psc);
        }

        // creates a new copy (comic_ownership) and adds it to collection_refrence
        return addToCollection(userId, comic);
    }

    /**
     * Changes all signatures that were linked to an old copy id to be linked to the new copy id.
     * Used when a copy's id has changed due to commands being undone/redone.
     * @param oldCopyId the previous copy id
     * @param newCopyId the new copy id
     */
    public void updateSignaturesForNewCopyId(int oldCopyId, int newCopyId) throws Exception {
        String sql = """
                UPDATE signature_refrence
                SET copy_fk = ?
                WHERE copy_fk = ?;
                """;
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(newCopyId);
        psc.appendToObjects(oldCopyId);
        jdbcInsert.executePreparedSQL(psc);
    }

    /**
     * Adds a comic copy to a collection.
     * 
     * @param userId the userId of the collection the comic will be in
     * @param comic  the comic to be added (assumed that the copy has not been
     *               created, copy id not used)
     * @return the new copy's id
     */
    public int addToCollection(int userId, Comic comic) throws Exception {
        // create a new copy of a comic (comic_ownership)
        String sql = "INSERT INTO comic_ownership(comic_fk, comic_value, grade, slabbed) VALUES(?, ?, ?, ?)";
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(comic.getId());
        obj.add(comic.getValue());
        obj.add(comic.getGrade());
        obj.add(comic.isSlabbed());
        int copyId = jdbcInsert.executePreparedSQLGetId(sql, obj);
        // add new entry to collection_refrence
        sql = "INSERT INTO collection_refrence (collection_fk, copy_fk) VALUES (?,?)";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(getCollectionIdFromUser(userId));
        psc.appendToObjects(copyId);
        jdbcInsert.executePreparedSQL(psc);
        return copyId;
    }

    /**
     * Removes a comic from a collection, deletes the copy's info.
     * 
     * @param userId the userId of the collection the comic will be removed from
     * @param comic  the comic to be removed (copy id must be set)
     * @throws Exception
     */
    public void removeFromCollection(int userId, Comic comic) throws Exception {
        String sql = "DELETE FROM collection_refrence WHERE collection_fk = ? AND copy_fk = ?";
        PreparedStatementContainer psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(getCollectionIdFromUser(userId));
        psc.appendToObjects(comic.getCopyId());
        jdbcInsert.executePreparedSQL(psc);
        sql = "DELETE FROM comic_ownership WHERE id = ?";
        psc = new PreparedStatementContainer();
        psc.appendToSql(sql);
        psc.appendToObjects(comic.getCopyId());
        jdbcInsert.executePreparedSQL(psc);
    }

    /**
     * Gets the collection id in the database for the given user.
     * 
     * @param userId - the id of the user to get the personal collection id of
     * @return - the id of the top-level personal collection owned by the given user
     * @throws Exception
     */
    private int getCollectionIdFromUser(int userId) throws Exception {
        String sql = "SELECT collection_fk FROM user_info WHERE id = ?";
        ArrayList<Object> obj = new ArrayList<>();
        obj.add(userId);
        ArrayList<Object> results = jdbcRead.executePreparedSQL(sql, obj);
        return (int) results.get(0);
    }

}
