package FileAdapters;

import FileAdapters.Adaptees.CSV;
import Model.JavaObjects.Comic;

public class CSVComicAdapter implements ComicConverter {
    private CSV adaptee;
    
    @Override
    public <T> T convertToFile(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToFile'");
    }

    @Override
    public <T> Comic convertToComic(T filetype) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToComic'");
    }
}
