package FileAdapters;
import Model.JavaObjects.*;

public interface ComicConverter {
    
    /**
     * Reads the filename and uses the adaptee method of parsing,
     * the method gets the next line and converts it into the comic
     * treat this as a getNextLine that will always return the line as a comic
     * @param <T>
     * @param filetype
     * @return
     */
    public Comic convertToComic(String filename) throws Exception;

    /**
     * This method converts the comic object into the format that is needed,
     * once in the format, the adaptee will read it into a file
     * @param <T>
     * @param comic
     * @return
     */
    public String convertToFile(Comic comic);
}
