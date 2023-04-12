package Model.Search.ConcreteSearches;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Controllers.Utils.PreparedStatementContainer;
import Model.Search.RunBin;
import Model.Search.RunGap;
import Model.Search.SearchAlgorithm;

public class RunSearch extends SearchAlgorithm {

    public int RUN_LENGTH = 12 ;

    /*
     * similar to ExactKeywordSearch, but ONLY matches against series names. 
     * Also returns not only the copy_id, but also the issue_num string
     */
    @Override
    public PreparedStatementContainer search(int userId, String keyword) {

        PreparedStatementContainer result = new PreparedStatementContainer() ;
        result.appendToObjects(userId);
        result.appendToObjects(keyword);

        result.appendToSql( //this took so much brainpower
            """
                SELECT 
                    id, issue_num
                FROM 
                    (
                        (
                            WITH RECURSIVE subcollections AS ( --find all collection_ids that specified user owns
                                SELECT 
                                    id,
                                    collect_fk,
                                    subcollect_fk
                                FROM
                                    subcollection_refrence
                                WHERE 
                                    collect_fk = (
                                        SELECT 
                                            collection_fk
                                        FROM
                                            user_info
                                        WHERE 
                                            user_info.id = ?
                                    )
                                UNION
                                    SELECT 
                                        e.id,
                                        e.collect_fk,
                                        e.subcollect_fk
                                    FROM
                                        subcollection_refrence e
                                    INNER JOIN 
                                        subcollections s 
                                            ON s.subcollect_fk = e.collect_fk
                            )
                                SELECT
                                    comic_ownership.id,
                                    comic_info.series,
                                    comic_info.title,
                                    comic_info.issue_num,
                                    comic_info.release_date,
                                    comic_info.release_day,
                                    comic_info.release_month,
                                    comic_info.release_year,
                                    publisher_info.p_name
                                FROM 
                                    subcollections
                                INNER JOIN 
                                    collection_refrence ON collection_refrence.collection_fk = subcollections.subcollect_fk 
                                INNER JOIN 
                                    comic_ownership ON collection_refrence.copy_fk = comic_ownership.id
                                INNER JOIN 
                                    comic_info ON comic_info.id = comic_ownership.comic_fk
                                INNER JOIN
                                    publisher_refrence ON publisher_refrence.comic_fk = comic_info.id
                                INNER JOIN
                                    publisher_info ON publisher_info.id = publisher_refrence.publisher_fk

                        ) INTERSECT (
                            (
                                SELECT
                                    comic_ownership.id,
                                    comic_info.series,
                                    comic_info.title,
                                    comic_info.issue_num,
                                    comic_info.release_date,
                                    comic_info.release_day,
                                    comic_info.release_month,
                                    comic_info.release_year,
                                    publisher_info.p_name
                                FROM
                                    comic_info
                                INNER JOIN 
                                    comic_ownership ON comic_ownership.comic_fk = comic_info.id
                                INNER JOIN
                                    publisher_refrence ON publisher_refrence.comic_fk = comic_info.id
                                INNER JOIN
                                    publisher_info ON publisher_info.id = publisher_refrence.publisher_fk
                                WHERE comic_info.series LIKE (?)
                            )
                        )
                    ) 
                AS result

            ORDER BY issue_num
            """
        );

        return result ;
    }
    

    @Override
    public int[] executeSearch(int userId, String keyword) {
        ArrayList<RunGap> rungaps = getRunGaps(userId, keyword) ;
        ArrayList<RunBin> runbins = new ArrayList<RunBin>() ;
        ArrayList<Integer> copyIds = new ArrayList<Integer>() ;

        // for this algorithm to work, the RunGaps in rungaps MUST BE ORDERED BY ISSUE NUMBER
        // this should be accomplished by the ORDER BY statement in search()

        // for each copy in users personal collection that meets the series keyword
        for (RunGap rg : rungaps) {
            boolean added = false ;

            for (RunBin rb : runbins) {
                // add it to a run if its consecutive (or duplicate) to the last copy in that run
                if ( rb.add(rg) ) {
                    added = true ;
                    break ; //this only helps time, it's not explicity needed
                }
            }
            // if it is not consecutive or duplicate to any of the current runs, add it to a new run
            if (added == false) {
                runbins.add(new RunBin(rg)) ;
            }

        }

        //once all copies have been added to runs
        for (RunBin rb : runbins) {
            // get rid of all the runs that don't meet the specified length
            if (rb.getRunLength() < RUN_LENGTH ) {
                runbins.remove(rb) ;
            } else {
                // if they do meet the length requirement, add all of the copy ids to the master copy id list (copyIds)
                copyIds.add( rb.getLast().getCopyId() ) ;
                for (RunGap rg : rb.getOthers()) {
                    copyIds.add( rg.getCopyId() ) ;
                }
            }
        }

        return copyIds.stream().mapToInt(i -> i).toArray() ;

    }

    /*
     * helper to collect information from sql call into useful container
     */
    private ArrayList<RunGap> getRunGaps(int userId, String keyword) {
        PreparedStatementContainer psc = search(userId, keyword);
        ArrayList<RunGap> rungaps = new ArrayList<RunGap>() ;
        
        //-----------------------------------------------------------------
        //special prepared statement connection for making RunGaps
        //-----------------------------------------------------------------
        System.out.println("Creating Connection...");
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(psc.getSql());) {

            int x = 1;
            System.out.println("Preparing Statement...");
            for (Object obj : psc.getObjects()) {
                stmt.setObject(x, obj);
                x++;
                System.out.println("Preparing Statement..." + obj);
            }
            System.out.println("Executing Command...");

            ResultSet rs = stmt.executeQuery();
            System.out.println("Command Executed!");

            while (rs.next()) {
                int copy_id = rs.getInt(1) ;
                String issue = rs.getString(2) ;
                rungaps.add(new RunGap(copy_id, issue)) ;
            }

            return rungaps ;

        } catch (SQLException e) {
            throw new Error("Outer Problem", e);
        }
    }

    
    
}
