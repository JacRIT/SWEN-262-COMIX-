package FileAdapters;
import Model.JavaObjects.*;

public interface ComicConverter {
    
    /**
     * This method converts a file to a comic object
     * @param <T>
     * @param filetype
     * @return
     */
    public <T> Comic convertToComic(T filetype);

    /**
     * This method converts the comic object into a File
     * @param <T>
     * @param comic
     * @return
     */
    public <T> T convertToFile(Comic comic);
}
