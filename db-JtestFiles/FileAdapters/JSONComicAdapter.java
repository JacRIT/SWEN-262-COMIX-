package FileAdapters;

import Model.JavaObjects.*;
import Model.JavaObjects.Character;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;

import FileAdapters.Adaptees.JSON;
import org.json.simple.JSONObject;

public class JSONComicAdapter implements ComicConverter {
    private JSON adaptee;

    public JSONComicAdapter(JSON adapteeJson){
        this.adaptee = adapteeJson;
    }

    @Override
    public String convertToFile(Comic comic) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToFile'");
    }


    @Override
    public Comic convertToComic(String filename) throws Exception{
        Object readAdaptee = adaptee.readFile(filename);
        JSONArray fileContents = (JSONArray) readAdaptee; //Array vers of read file
        JSONObject adapteeObj = (JSONObject) readAdaptee; //JSON obj vers of file
        Iterator<?> iterator = fileContents.iterator();
        if (iterator.hasNext() == false) {return null;} //iterator will return null if there's no next val, uses JSON Array

         //instantiate all the values corresponding to the database, same for all adapters
        String      series              = (String) adapteeObj.get("series") ;
        String      title               = (String) adapteeObj.get("title");
        int         volume_number       = 1 ;
        String      issue_number        = (String) adapteeObj.get("Issue") ;
        float       initial_value       = 9.99f ; 
        String      description         = (String) adapteeObj.get("Variant Description") ;
        String      release_date        = (String) adapteeObj.get("Release Date") ;

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


        //compute publishers
        String json_pub = (String) adapteeObj.get("Publisher") ;
        String[] pubs = json_pub.split(" [|] ") ;

        for (String pub : pubs) {

            if (pub == "") {continue ;}

            publishers.add( new Publisher(1, pub)) ; // 1 is placeholder number, wont actually be INSERTed into the DB with id 1
        }

        //compute creators
        String json_cre = (String) adapteeObj.get("Creators") ;
        String[] cres = json_cre.split("( [|] )") ;

        for (String cre : cres) {

            if (cre == "") {continue ;}
            else {creators.add(new Creator(1,cre)) ;}

        }

        //---------------------------------------------------------
        // CREATE COMIC OBJECT FROM VALUES
        //---------------------------------------------------------

        return new Comic(1, 1, publishers, series, title, volume_number, issue_number, release_date, creators, characters, description, initial_value, signatures, value, grade, isSlabbed) ;

    }
    
}
