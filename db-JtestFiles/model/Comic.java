package model;

import java.util.ArrayList;

public class Comic {
   
    private final int                       id;
    private final ArrayList<Publisher>      publisher;
    private final String                    series;
    private final String                    title;
    private final int                       volumeNumber;
    private final String                    issueNumber;
    private final String                    publicationDate;
    private final ArrayList<Creator>        creators;
    private final ArrayList<Character>      principlCharacters;
    private final String                    description;
    private final float                     initial_value;
    private final float                     value;
    private final int                       grade;
    private final boolean                   isSlabbed;

    public Comic(
                int id, ArrayList<Publisher> publisher, String series, 
                String title, int volumeNumber, String issueNumber, 
                String publicationDate, ArrayList<Creator> creators, 
                ArrayList<Character> principlCharacters, String description,
                float initial_value, float value, int grade, boolean isSlabbed) 
    {
        this.id = id ;
        this.publisher = publisher ;
        this.series = series ;
        this.title = title ;
        this.volumeNumber = volumeNumber ;
        this.issueNumber = issueNumber ;
        this.publicationDate = publicationDate ;
        this.creators = creators ;
        this.principlCharacters = principlCharacters ;
        this.description = description ;
        this.initial_value = initial_value ;
        this.value = value ;
        this.grade = grade ;
        this.isSlabbed = isSlabbed ;
    }

    @Override
    public String toString() {
        return "Comic [\n\tid=                 " + id 
                    + "\n\tpublisher=          " + publisher 
                    + "\n\tseries=             " + series 
                    + "\n\ttitle=              " + title
                    + "\n\tvolumeNumber=       " + volumeNumber 
                    + "\n\tissueNumber=        " + issueNumber 
                    + "\n\tpublicationDate=    " + publicationDate 
                    + "\n\tcreators=           " + creators 
                    + "\n\tprinciplCharacters= " + principlCharacters
                    + "\n\tdescription=        " + description 
                    + "\n\tinitial_value=      " + initial_value 
                    + "\n\tvalue=              " + value 
                    + "\n\tgrade=              " + grade 
                    + "\n\tisSlabbed=          " + isSlabbed 
                    + "\n]";

    }

}
