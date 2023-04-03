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
                            comic_ownership.id
                        FROM 
                            subcollections
                        INNER JOIN 
                            collection_refrence ON collection_refrence.collection_fk = subcollections.subcollect_fk 
                        INNER JOIN 
                            comic_ownership ON collection_refrence.copy_fk = comic_ownership.id
                ) INTERSECT (
                    (
                        SELECT
                            id
                        FROM
                            comic_ownership
                        WHERE grade > 0
                    ) UNION (
                        SELECT
                            copy_fk
                        FROM
                            signature_refrence
                    )
                )

            """
        );

        result.appendToSql(this.sort.sort());

        return result ;
    }
    
}
