package FileAdapters.Adaptees;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;
/**
 * This class is meant to represent a CSV libary that can handle
 * reading and creating files
 */
public class CSV{
    /**
     * 
     * @param filename The filename of the file being read
     * @return CSVReaderHeaderAware, the reader
     * @throws Exception
     */
    private String filename;
    private CSVReaderHeaderAware CSVReader;

    public CSV(String filename) throws Exception{
        this.filename = filename;
        this.CSVReader = new CSVReaderHeaderAware(new FileReader(filename));
    }

    public CSVReaderHeaderAware readFile() throws Exception{
        return this.CSVReader;
    }

    public CSVWriter createFile(String filename) throws Exception{
        File file = new File(filename);
        FileWriter outputfile = new FileWriter(file);
        return new CSVWriter(outputfile);
    }
}