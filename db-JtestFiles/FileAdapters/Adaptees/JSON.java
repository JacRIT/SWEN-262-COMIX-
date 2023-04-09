package FileAdapters.Adaptees;
import java.io.FileReader;
import org.json.simple.parser.*;

/**
 * This class is meant to represent a JSON libary that can handle
 * reading and creating files
 */
public class JSON {
    public Object readFile(String filename) throws Exception{
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(filename);
        Object obj = jsonParser.parse(reader);
        return obj;

    }

    public void createFile(String filename){
        
    }
}
