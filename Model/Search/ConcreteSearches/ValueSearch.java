package Model.Search.ConcreteSearches;

import Controllers.Utils.PreparedStatementContainer;
import Model.Search.SearchAlgorithm;

public class ValueSearch extends SearchAlgorithm {

    @Override
    public PreparedStatementContainer search(int userId, String keyword) {

        PreparedStatementContainer result = new PreparedStatementContainer() ;
        result.appendToObjects(userId);

        result.appendToSql(
            """
            SELECT id FROM
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
                            comic_info.release_year
                        FROM 
                            subcollections
                        INNER JOIN 
                            collection_refrence ON collection_refrence.collection_fk = subcollections.subcollect_fk 
                        INNER JOIN 
                            comic_ownership ON collection_refrence.copy_fk = comic_ownership.id
                        INNER JOIN 
                            comic_info ON comic_info.id = comic_ownership.comic_fk
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
                                comic_info.release_year
                            FROM
                                comic_ownership
                            INNER JOIN 
                                comic_info ON comic_info.id = comic_ownership.comic_fk
                            WHERE
                                comic_ownership.grade > 0
                        ) UNION (
                            SELECT
                                comic_ownership.id,
                                comic_info.series,
                                comic_info.title,
                                comic_info.issue_num,
                                comic_info.release_date,
                                comic_info.release_day,
                                comic_info.release_month,
                                comic_info.release_year
                            FROM
                                signature_refrence
                            INNER JOIN
                                comic_ownership ON comic_ownership.id = signature_refrence.copy_fk
                            INNER JOIN 
                                comic_info ON comic_info.id = comic_ownership.comic_fk
                        )
                    )
                ) AS result

            """
        );

        result.appendToSql(this.sort.sort());

        return result ;
    }
    
}
