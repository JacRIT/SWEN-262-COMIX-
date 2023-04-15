package Controllers.Utils.FileAdapters;

import Model.JavaObjects.*;
import Model.JavaObjects.Character;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Controllers.Utils.FileAdapters.Adaptees.JSON;

import java.io.FileWriter;

public class JSONComicAdapter implements ComicConverter {
    private JSON adaptee;
    private int arrayIndex;

    public JSONComicAdapter(JSON adapteeJson){
        this.adaptee = adapteeJson;
        this.arrayIndex = 0;

    }
    @SuppressWarnings("unchecked")
    //used bc JSONSimple has weird issue with trying to be a hashmap
    @Override
    public String convertToFile(String filename, Comic[] comics) throws Exception {
        //Current plan: use JDBCComicExtractor in ComicController to get All comics, 
        //then insert that array into here to be parsed (with maybe helper method),
        //this method will put it all into file
        FileWriter file = adaptee.createFile(filename);
        for(Comic comic : comics){
            //each comic is translated to a JSON object
            if(comic == null){continue;}
            JSONObject comicDetail = new JSONObject();
            comicDetail.put("series", comic.getSeries());
            comicDetail.put("title", comic.getTitle());
            comicDetail.put("volume_number", comic.getVolumeNumber());
            comicDetail.put("issue_number", comic.getIssueNumber());
            comicDetail.put("description", comic.getDescription());
            comicDetail.put("release_date", comic.getPublicationDate());
            //need to convert Publishers into | concat
            comicDetail.put("publisher", formatString(comic.getPublisher()));
            comicDetail.put("creators", formatString(comic.getCreators()));
            JSONObject comicObj = new JSONObject();
            comicObj.put("comic", comicDetail);
            JSONArray comicList = new JSONArray();
            comicList.add(comicObj);
            file.write(comicList.toJSONString());

        }
        file.flush();
        return null;
    }

    private <T> String formatString(ArrayList<T> unformattedArray){
        String unformattedString = unformattedArray.toString();
        return unformattedString.replace("[", "").replace("]", "").replace(", ", " | ");
    }

    @Override
    public Comic convertToComic() throws Exception{
        Object readAdaptee = adaptee.readFile();
        JSONArray fileContents = (JSONArray) readAdaptee; //Array vers of read file //JSON obj vers of file
        if(arrayIndex == (fileContents.size())){return null;}
        return makeComic((JSONObject) fileContents.get(arrayIndex));
    }

    private Comic makeComic(JSONObject comic){
        JSONObject adapteeObj = (JSONObject) comic.get("comic");

        String      series              = (String) adapteeObj.get("series") ;
        String      title               = (String) adapteeObj.get("title");
        int         volume_number       = 1 ;
        String      issue_number        = String.valueOf(adapteeObj.get("issue_number")) ;
        float       initial_value       = 9.99f ; 
        String      description         = (String) adapteeObj.get("description") ;
        String      release_date        = (String) adapteeObj.get("release_date") ;

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
        String json_pub = (String) adapteeObj.get("publisher") ;
        String[] pubs = json_pub.split(" [|] ") ;

        for (String pub : pubs) {

            if (pub == "") {continue ;}

            publishers.add( new Publisher(1, pub)) ; // 1 is placeholder number, wont actually be INSERTed into the DB with id 1
        }

        //compute creators
        String json_cre = (String) adapteeObj.get("creators") ;
        String[] cres = json_cre.split("( [|] )") ;

        for (String cre : cres) {

            if (cre == "") {continue ;}
            else {creators.add(new Creator(1,cre)) ;}

        }

        //---------------------------------------------------------
        // CREATE COMIC OBJECT FROM VALUES
        //---------------------------------------------------------
        arrayIndex++;
        return new Comic(1, 1, publishers, series, title, volume_number, issue_number, release_date, creators, characters, description, initial_value, signatures, value, grade, isSlabbed) ;

    }
    
    public static void main(String[] args) {

        try {
            ArrayList<Comic> comicsToExport = new ArrayList<Comic>();
            JSON json = new JSON("./comicsInput.json");
            JSONComicAdapter x = new JSONComicAdapter(json);
            Comic test = x.convertToComic() ;
            comicsToExport.add(test);
            System.out.println("Now importing...");
            while (test != null) {

                System.out.println(test);
                System.out.println();
                test = x.convertToComic() ;
                comicsToExport.add(test);

            }
            System.out.println("Now exporting...");
            x.convertToFile("comicsExport.json", comicsToExport.toArray(new Comic[0]));
            System.out.println("*Ding!* Check your files!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
