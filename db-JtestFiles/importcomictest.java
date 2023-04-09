import Model.JavaObjects.Comic;

public class importcomictest {
    public static void main(String[] args) throws Exception { // running this will import a new comic into the database!
        ComicImporter ci = new ComicImporter() ;
        CSVComicReader cr = new CSVComicReader("./comicsInput.csv") ;

        Comic target = cr.getNextComic() ;
        while (target != null) {
            ci.changeTarget(target);

            ci.importComic();

            target = cr.getNextComic() ;
        }
    }
}
