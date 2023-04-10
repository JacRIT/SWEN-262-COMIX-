package FileAdapters.Adaptees;
import java.io.FileReader;
import org.json.simple.parser.*;
import java.io.FileWriter;

/**
 * This class is meant to represent a JSON libary that can handle
 * reading and creating files
 */
public class JSON {
    private String filename;

    public JSON(String filename){
        this.filename = filename;
    }

    public Object readFile() throws Exception{
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(filename);
        Object obj = jsonParser.parse(reader);
        return obj;

    }

    public FileWriter createFile(String file) throws Exception{
        FileWriter createdFile = new FileWriter(file);
        return createdFile;
    }
}
