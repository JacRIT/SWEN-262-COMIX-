package Controllers.Utils;

import java.util.ArrayList;
import java.util.List;

public class PreparedStatementContainer {
    public String sql;
    public List<Object> objects ;
    
    public PreparedStatementContainer() {
        this.sql = "";
        this.objects = new ArrayList<Object>();
    }

    public void appendToSql(String sql) {
        this.sql = this.sql + sql ;
    }

    public void appendToObjects(Object obj) {
        this.objects.add(obj);
    }
    public void appendToObjects(List<Object> objs) {
        this.objects.addAll(objs);
    }
}
