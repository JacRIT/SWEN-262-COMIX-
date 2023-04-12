package FileAdapters.Adaptees;
import javax.xml.parsers.*;
import java.io.File;

import org.w3c.dom.Document;

/**
 * This class is meant to represent an XML libary that can handle
 * reading and creating files
 */
public class XML {

    private String filename;
    private Document doc;

    public XML(String filename) throws Exception{
        this.filename = filename;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        this.doc = db.parse(new File(filename));
    }
    public Document readFile() throws Exception{
        return doc;
    }

    public Document createFile() throws Exception{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.newDocument();

    }
}
