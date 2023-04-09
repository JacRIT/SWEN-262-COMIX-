package FileAdapters;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import FileAdapters.Adaptees.CSV;
import Model.JavaObjects.Comic;
import Model.JavaObjects.Character;
import Model.JavaObjects.Creator;
import Model.JavaObjects.Publisher;
import Model.JavaObjects.Signature;

public class CSVComicAdapter implements ComicConverter {
    private CSV adaptee;

    public CSVComicAdapter(CSV adaptee){
        this.adaptee = adaptee;
    }
    
    @Override
    public String convertToFile(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToFile'");
    }

    @Override
    public Comic convertToComic(String filename) throws Exception{
        //Code from CSVComicReader
        Map<String,String> values = adaptee.readFile(filename).readMap();

        if (values == null) {return null ;} // if CSVReader reaches the end of the file, it returns null

        //instantiate all the values corresponding to the database
        String      series              = values.get("Series") ;
        String      title               = values.get("Full Title");
        int         volume_number       = 1 ;
        String      issue_number        = values.get("Issue") ;
        float       initial_value       = 9.99f ; 
        String      description         = values.get("Variant Description") ;
        String      release_date        = values.get("Release Date") ;

        float       value               = 0 ; 
        int         grade               = 0 ;
        boolean     isSlabbed           = false ;

        ArrayList<Publisher> publishers = new ArrayList<Publisher>() ;
        ArrayList<Creator>   creators   = new ArrayList<Creator>();
        ArrayList<Character> characters = new ArrayList<Character>() ; //currently unused D:
        ArrayList<Signature> signatures = new ArrayList<Signature>() ;

        //---------------------------------------------------------
        // COMPUTE SPECIAL VALUES
        //---------------------------------------------------------

        //compute volume number if "Vol. " is in series, otherwise leave volume number as '1'
        if ( series.contains("Vol. ") ) {

            Pattern find_volnum = Pattern.compile("(Vol. )([0-9]+)") ;
            Matcher match_volnum = find_volnum.matcher(series) ;

            while (match_volnum.find()) {
                volume_number = Integer.parseInt( match_volnum.group().split(" ")[1] ) ;
            }

            // alternative implementation

            // String[] series_volnum = series.split("(Vol. )") ;
            // volume_number = Integer.parseInt(series_volnum[1].split(" ")[0]);
            
        }

        //compute publishers
        String csv_pub = values.get("Publisher") ;
        String[] pubs = csv_pub.split(" [|] ") ;

        for (String pub : pubs) {

            if (pub == "") {continue ;}

            publishers.add( new Publisher(1, pub)) ; // 1 is placeholder number, wont actually be INSERTed into the DB with id 1
        }

        //compute creators
        String csv_cre = values.get("Creators") ;
        String[] cres = csv_cre.split("( [|] )") ;

        for (String cre : cres) {

            if (cre == "") {continue ;}
            else {creators.add(new Creator(1,cre)) ;}

        }

        //compute characters
        // theres nothing here...
        // :(


        //---------------------------------------------------------
        // CREATE COMIC OBJECT FROM VALUES
        //---------------------------------------------------------

        return new Comic(1, 1, publishers, series, title, volume_number, issue_number, release_date, creators, characters, description, initial_value, signatures, value, grade, isSlabbed) ;

    }
}
