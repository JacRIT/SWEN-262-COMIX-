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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
        result = prime * result + ((series == null) ? 0 : series.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + volumeNumber;
        result = prime * result + ((issueNumber == null) ? 0 : issueNumber.hashCode());
        result = prime * result + ((publicationDate == null) ? 0 : publicationDate.hashCode());
        result = prime * result + ((creators == null) ? 0 : creators.hashCode());
        result = prime * result + ((principlCharacters == null) ? 0 : principlCharacters.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + Float.floatToIntBits(initial_value);
        result = prime * result + Float.floatToIntBits(value);
        result = prime * result + grade;
        result = prime * result + (isSlabbed ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comic other = (Comic) obj;
        if (id != other.id)
            return false;
        if (publisher == null) {
            if (other.publisher != null)
                return false;
        } else if (!publisher.equals(other.publisher))
            return false;
        if (series == null) {
            if (other.series != null)
                return false;
        } else if (!series.equals(other.series))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (volumeNumber != other.volumeNumber)
            return false;
        if (issueNumber == null) {
            if (other.issueNumber != null)
                return false;
        } else if (!issueNumber.equals(other.issueNumber))
            return false;
        if (publicationDate == null) {
            if (other.publicationDate != null)
                return false;
        } else if (!publicationDate.equals(other.publicationDate))
            return false;
        if (creators == null) {
            if (other.creators != null)
                return false;
        } else if (!creators.equals(other.creators))
            return false;
        if (principlCharacters == null) {
            if (other.principlCharacters != null)
                return false;
        } else if (!principlCharacters.equals(other.principlCharacters))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (Float.floatToIntBits(initial_value) != Float.floatToIntBits(other.initial_value))
            return false;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
            return false;
        if (grade != other.grade)
            return false;
        if (isSlabbed != other.isSlabbed)
            return false;
        return true;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Publisher> getPublisher() {
        return publisher;
    }

    public String getSeries() {
        return series;
    }

    public String getTitle() {
        return title;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public ArrayList<Creator> getCreators() {
        return creators;
    }

    public ArrayList<Character> getPrinciplCharacters() {
        return principlCharacters;
    }

    public String getDescription() {
        return description;
    }

    public float getInitial_value() {
        return initial_value;
    }

    public float getValue() {
        return value;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isSlabbed() {
        return isSlabbed;
    }

}
