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
                SELECT 
                    id 
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
                            ( --get all copy_ids where
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
                                    creator_refrence
                                INNER JOIN 
                                    comic_info ON comic_info.id = creator_refrence.comic_fk
                                INNER JOIN
                                    comic_ownership ON comic_info.id = comic_ownership.comic_fk
                                INNER JOIN
                                    creator_info ON creator_info.id = creator_refrence.creator_fk
                                INNER JOIN
                                    publisher_refrence ON publisher_refrence.comic_fk = comic_info.id
                                INNER JOIN
                                    publisher_info ON publisher_info.id = publisher_refrence.publisher_fk
                                WHERE
                                    creator_info.c_name LIKE (CONCAT('%',?,'%'))
                            ) UNION (
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
                                    character_refrence
                                INNER JOIN 
                                    comic_info ON comic_info.id = character_refrence.comic_fk
                                INNER JOIN
                                    comic_ownership ON comic_info.id = comic_ownership.comic_fk
                                INNER JOIN
                                    character_info ON character_refrence.character_fk = character_info.id
                                INNER JOIN
                                    publisher_refrence ON publisher_refrence.comic_fk = comic_info.id
                                INNER JOIN
                                    publisher_info ON publisher_info.id = publisher_refrence.publisher_fk
                                WHERE
                                    character_info.character_name LIKE (CONCAT('%',?,'%'))
                            ) UNION (
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
                                WHERE comic_info.series LIKE (CONCAT('%',?,'%')) 
                                    OR comic_info.title LIKE (CONCAT('%',?,'%')) 
                                    OR comic_info.descrip LIKE (CONCAT('%',?,'%'))
                            )
                        )
                    ) 
                AS result

            """
        );

        result.appendToSql(this.sort.sort());

        return result ;
    }
    
}
