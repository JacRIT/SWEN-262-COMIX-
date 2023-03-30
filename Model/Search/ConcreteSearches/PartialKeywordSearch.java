package Model.Search.ConcreteSearches;

import Controllers.Utils.PreparedStatementContainer;
import Model.Search.SearchAlgorithm;

public class PartialKeywordSearch extends SearchAlgorithm {

    @Override
    public PreparedStatementContainer search(int userId, String keyword) {

        PreparedStatementContainer result = new PreparedStatementContainer() ;
        result.appendToObjects(userId);
        for (int i = 0; i<6; i++) {
            result.appendToObjects(keyword);
        }

        result.appendToSql( //this took so much brainpower
            """
                (
                    WITH RECURSIVE subcollections AS (                                                                      --find all copy_ids that specified user owns
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
                        INNER JOIN collection_refrence ON collection_refrence.collection_fk = subcollections.subcollect_fk 
                        INNER JOIN comic_ownership ON collection_refrence.copy_fk = comic_ownership.id

                ) INTERSECT (                                                                                               --match them against

                    (                                                                                                       --get all copy_ids where the keyword is in VARCHAR fields (except issue_num)
                        SELECT 
                            comic_ownership.id
                        FROM
                            creator_refrence
                        INNER JOIN 
                            comic_info ON comic_info.id = creator_refrence.comic_fk
                        INNER JOIN
                            comic_ownership ON comic_info.id = comic_ownership.comic_fk
                        INNER JOIN
                            creator_info ON creator_info.id = creator_refrence.creator_fk
                        WHERE
                            creator_info.c_name LIKE (CONCAT('%',?,'%'))
                    ) UNION (
                        SELECT
                            comic_ownership.id
                        FROM
                            character_refrence
                        INNER JOIN 
                            comic_info ON comic_info.id = character_refrence.comic_fk
                        INNER JOIN
                            comic_ownership ON comic_info.id = comic_ownership.comic_fk
                        INNER JOIN
                            character_info ON character_refrence.character_fk = character_info.id
                        WHERE
                            character_info.character_name LIKE (CONCAT('%',?,'%'))
                    ) UNION (
                        SELECT
                            comic_ownership.id
                        FROM
                            publisher_refrence
                        INNER JOIN 
                            comic_info ON comic_info.id = publisher_refrence.comic_fk
                        INNER JOIN
                            comic_ownership ON comic_info.id = comic_ownership.comic_fk
                        INNER JOIN
                            publisher_info ON publisher_info.id = publisher_refrence.publisher_fk
                        WHERE
                            publisher_info.p_name LIKE (CONCAT('%',?,'%'))
                    ) UNION (
                        SELECT
                            comic_ownership.id
                        FROM
                            comic_info
                        INNER JOIN 
                            comic_ownership ON comic_ownership.comic_fk = comic_info.id
                        WHERE 
                                comic_info.series LIKE (CONCAT('%',?,'%')) 
                            OR 
                                comic_info.title LIKE (CONCAT('%',?,'%')) 
                            OR 
                                comic_info.descrip LIKE (CONCAT('%',?,'%'))
                    )
                )
            """
        );

        return result ;
    }
    
}
