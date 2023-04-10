import FileAdapters.CSVComicAdapter;
import Model.JavaObjects.Comic;
import FileAdapters.Adaptees.CSV;

public class importcomictest {
    public static void main(String[] args) throws Exception { // running this will import a new comic into the database!
        ComicImporter ci = new ComicImporter() ;
        CSV csv = new CSV("./comicsInput.csv");
        CSVComicAdapter cr = new CSVComicAdapter(csv);
        Comic target = cr.convertToComic() ;
        while (target != null) {
            ci.changeTarget(target);
            ci.importComic();
            target = cr.convertToComic() ;
        }
    }
}
