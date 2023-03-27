package Controllers.Utils;

import Model.JavaObjects.Comic;

public class JDBCComicExtractor extends JDBC {
    /*
     * This class is meant to take sql input and return Comic or Comic[]
     */

    public Comic[] getComic(String sql) {
        throw new UnsupportedOperationException("Unimplemented method 'getComic'");
    }
    public Comic[] getComic(PreparedStatementContainer statementDetails) {
        throw new UnsupportedOperationException("Unimplemented method 'getComic'");
    }
    
}
