package Model.JavaObjects;

import java.util.ArrayList;

public class Comic {
   
    private  int                       id;
    private  int                       copyId;
    private  ArrayList<Publisher>      publisher;
    private  String                    series;
    private  String                    title;
    private  int                       volumeNumber;
    private  String                    issueNumber;
    private  String                    publicationDate;
    private  ArrayList<Creator>        creators;
    private  ArrayList<Character>      principlCharacters;
    private  String                    description;
    private  float                     initialValue;
    private  ArrayList<Signature>      signatures;
    private  float                     value;
    private  int                       grade;
    private  boolean                   isSlabbed;

    public Comic(int id, int copyId, ArrayList<Publisher> publisher, String series, String title, int volumeNumber,
            String issueNumber, String publicationDate, ArrayList<Creator> creators,
            ArrayList<Character> principlCharacters, String description, float initialValue, ArrayList<Signature> signatures, float value, int grade,
            boolean isSlabbed) {
        this.id = id;
        this.copyId = copyId;
        this.publisher = publisher;
        this.series = series;
        this.title = title;
        this.volumeNumber = volumeNumber;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
        this.creators = creators;
        this.principlCharacters = principlCharacters;
        this.description = description;
        this.initialValue = initialValue;
        this.signatures = signatures;
        this.value = value;
        this.grade = grade;
        this.isSlabbed = isSlabbed;
    }

    @Override
    public String toString() {
        return "Comic [\n\tid=                 " + id 
                    + "\n\tcopyId=             " + copyId
                    + "\n\tpublisher=          " + publisher 
                    + "\n\tseries=             " + series 
                    + "\n\ttitle=              " + title
                    + "\n\tvolumeNumber=       " + volumeNumber 
                    + "\n\tissueNumber=        " + issueNumber 
                    + "\n\tpublicationDate=    " + publicationDate 
                    + "\n\tcreators=           " + creators 
                    + "\n\tprinciplCharacters= " + principlCharacters
                    + "\n\tdescription=        " + description 
                    + "\n\tinitialValue=       " + initialValue 
                    + "\n\tsignatures=         " + signatures
                    + "\n\tvalue=              " + value 
                    + "\n\tgrade=              " + grade 
                    + "\n\tisSlabbed=          " + isSlabbed 
                    + "\n]";

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public ArrayList<Publisher> getPublisher() {
        return publisher;
    }

    public void setPublisher(ArrayList<Publisher> publisher) {
        this.publisher = publisher;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(int volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public ArrayList<Creator> getCreators() {
        return creators;
    }

    public void setCreators(ArrayList<Creator> creators) {
        this.creators = creators;
    }

    public ArrayList<Character> getPrinciplCharacters() {
        return principlCharacters;
    }

    public void setPrinciplCharacters(ArrayList<Character> principlCharacters) {
        this.principlCharacters = principlCharacters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(float initialValue) {
        this.initialValue = initialValue;
    }

    public ArrayList<Signature> getSignatures() {
        return signatures;
    }

    public void addSignature(Signature signature) {
        this.signatures.add(signature);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isSlabbed() {
        return isSlabbed;
    }

    public void setSlabbed(boolean isSlabbed) {
        this.isSlabbed = isSlabbed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + copyId;
        result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
        result = prime * result + ((series == null) ? 0 : series.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + volumeNumber;
        result = prime * result + ((issueNumber == null) ? 0 : issueNumber.hashCode());
        result = prime * result + ((publicationDate == null) ? 0 : publicationDate.hashCode());
        result = prime * result + ((creators == null) ? 0 : creators.hashCode());
        result = prime * result + ((principlCharacters == null) ? 0 : principlCharacters.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + Float.floatToIntBits(initialValue);
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
        if (copyId != other.copyId)
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
        if (Float.floatToIntBits(initialValue) != Float.floatToIntBits(other.initialValue))
            return false;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
            return false;
        if (grade != other.grade)
            return false;
        if (isSlabbed != other.isSlabbed)
            return false;
        return true;
    }

}