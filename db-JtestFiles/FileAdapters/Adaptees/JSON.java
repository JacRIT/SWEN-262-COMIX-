package FileAdapters.Adaptees;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.FileWriter;

/**
 * This class is meant to represent a JSON libary that can handle
 * reading and creating files
 */
public class JSON {
    private String filename;
    private JSONObject parser;

    public JSON(String filename) throws Exception{
        this.filename = filename;
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(filename);
        this.parser = (JSONObject) jsonParser.parse(reader);
    }

    public JSONObject readFile() throws Exception{
        return this.parser;

    }

    public FileWriter createFile(String file) throws Exception{
        FileWriter createdFile = new FileWriter(file);
        return createdFile;
    }
}
