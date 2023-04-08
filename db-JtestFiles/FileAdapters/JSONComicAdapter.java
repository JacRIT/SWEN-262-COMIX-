package FileAdapters;

import Model.JavaObjects.Comic;
import FileAdapters.Adaptees.JSON;

public class JSONComicAdapter implements ComicConverter {
    private JSON adaptee;

    @Override
    public <T> Comic convertToComic(T filetype) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToComic'");
    }

    @Override
    public <T> T convertToFile(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToFile'");
    }
    
}
