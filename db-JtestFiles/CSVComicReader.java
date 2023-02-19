import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVReaderHeaderAware;

import model.Comic;
import model.Creator;
import model.Publisher;
import model.Character;


/*
 * This class is meant to take a CSV file, with headers that include the following:
 *  1. Series
 *  2. Full Title
 *  3. Issue
 *  4. Description
 *  5. Publication Date
 * 
 *  The getNextComic() function returns one comic 
 *  while reading through the CSV file one line at a time
 * 
 *  At the end of the file is a main method which decribes proper use of the class.
 *  It generates each comic from the CSV file and prints them to terminal.
 * 
 */
public class CSVComicReader {

    private final CSVReaderHeaderAware reader ;

    public CSVComicReader(String filename) throws Exception {
        reader = new CSVReaderHeaderAware( new FileReader(filename) ) ;
    }

    public Comic getNextComic() throws Exception {

        //grab map where key is header, value is parsed
        Map<String,String> values = reader.readMap() ;

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

            String[] first_last = cre.split(" ") ;
            if (first_last.length == 1) { creators.add( new Creator(1, first_last[0], "")); }
            else                        { creators.add( new Creator(1, first_last[0], first_last[1])); } // 1 is placeholder number, wont actually be INSERTed into the DB with id 1
        }

        //compute characters
        // theres nothing here...
        // :(


        //---------------------------------------------------------
        // CREATE COMIC OBJECT FROM VALUES
        //---------------------------------------------------------

        return new Comic(1, publishers, series, title, volume_number, issue_number, release_date, creators, characters, description, initial_value, value, grade, isSlabbed) ;

    }

    public static void main(String[] args) {

        try {
            
            CSVComicReader x = new CSVComicReader("comics.csv") ;
            Comic test = x.getNextComic() ;

            while (test != null) {

                System.out.println(test);
                System.out.println();
                test = x.getNextComic() ;

            }


        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
