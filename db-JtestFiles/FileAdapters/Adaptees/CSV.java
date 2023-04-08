package FileAdapters.Adaptees;
import java.io.FileReader;

import com.opencsv.CSVReaderHeaderAware;
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
    public CSVReaderHeaderAware readFile(String filename) throws Exception{
        return new CSVReaderHeaderAware( new FileReader(filename) ) ;
    }

    public void createFile(String filename){

    }
}