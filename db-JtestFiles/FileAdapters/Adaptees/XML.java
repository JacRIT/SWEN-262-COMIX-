package FileAdapters.Adaptees;
import javax.xml.parsers.*;
import java.io.File;
import org.w3c.dom.Document;

/**
 * This class is meant to represent an XML libary that can handle
 * reading and creating files
 */
public class XML {
    public Document readFile(String filename) throws Exception{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(filename));
        return doc;
    }

    public void createFile(String filename){
        
    }
}
