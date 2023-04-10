package FileAdapters;
import Model.JavaObjects.*;

public interface ComicConverter {
    
    /**
     * Reads the filename and uses the adaptee method of parsing,
     * the method gets the next line and converts it into the comic
     * treat this as a getNextLine that will always return the line as a comic
     * IMPORTING EDIT: will have a "toggle", if importing into a personal collection
     * THEN it will run an addToCollection() call for each comic in the Array
     */
    public Comic convertToComic() throws Exception;

    /**
     * This method converts the comic object into the format that is needed,
     * once in the format, the adaptee will read it into a file
     * @throws Exception
     */
    public String convertToFile(String filename, Comic[] comics) throws Exception;
}
