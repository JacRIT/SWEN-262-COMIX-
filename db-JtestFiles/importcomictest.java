import Controllers.Utils.ComicImporter;
import Controllers.Utils.FileAdapters.CSVComicAdapter;
import Controllers.Utils.FileAdapters.Adaptees.CSV;
import Model.JavaObjects.Comic;

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
