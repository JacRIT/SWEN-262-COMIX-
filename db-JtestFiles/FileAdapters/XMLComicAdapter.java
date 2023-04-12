package FileAdapters;

import Model.JavaObjects.*;
import Model.JavaObjects.Character;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import FileAdapters.Adaptees.XML;

public class XMLComicAdapter implements ComicConverter {
    private XML adaptee;
    private int nodeIndex = 0;

    public XMLComicAdapter(XML adapteeXml){
        this.adaptee = adapteeXml;
    }
    @Override
    public String convertToFile(String filename, Comic[] comics) throws Exception{
        FileOutputStream output = new FileOutputStream(filename);
        Document doc = adaptee.createFile();
        Element rootElement = doc.createElement("Comics");
        for(Comic comicObj : comics){ //For each comic in the array, creates an XML element 
        doc.appendChild(rootElement);
        Element comic = doc.createElement("comic");
        rootElement.appendChild(comic);

        Element series = doc.createElement("series");
        series.setTextContent(comicObj.getSeries());
        comic.appendChild(series);

        Element title = doc.createElement("title");
        title.setTextContent(comicObj.getTitle());
        comic.appendChild(title);

        Element vol = doc.createElement("vol");
        vol.setTextContent(String.valueOf(comicObj.getVolumeNumber()));
        comic.appendChild(vol);

        Element issue = doc.createElement("issue");
        issue.setTextContent(comicObj.getIssueNumber());
        comic.appendChild(issue);

        Element desc = doc.createElement("desc");
        desc.setTextContent(comicObj.getDescription());
        comic.appendChild(desc);

        Element release = doc.createElement("release");
        release.setTextContent(comicObj.getPublicationDate());
        comic.appendChild(release);

        Element pub = doc.createElement("pub");
        pub.setTextContent(formatString(comicObj.getPublisher()));
        comic.appendChild(pub);

        Element creator = doc.createElement("creator");
        creator.setTextContent(formatString(comicObj.getCreators()));
        comic.appendChild(creator);
        }
        writeToFile(doc, output);
        output.close();
        return null;

    }

    private void writeToFile(Document doc, OutputStream output) throws Exception{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //This indents the child <>'s'
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);

    }

    private <T> String formatString(ArrayList<T> unformattedArray){
        String unformattedString = unformattedArray.toString();
        return unformattedString.replace("[", "").replace("]", "").replace(", ", " | ");
    }

    @Override
    public Comic convertToComic() throws Exception{
        Document doc = adaptee.readFile();
        doc.getDocumentElement().normalize();
        NodeList list = doc.getElementsByTagName("comic");
        if(nodeIndex == list.getLength()){return null;}
        if(nodeIndex < list.getLength()){
            Node node = list.item(nodeIndex);

            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                
                String      series              = element.getElementsByTagName("series").item(0).getTextContent() ;
                String      title               = element.getElementsByTagName("title").item(0).getTextContent() ;
                int         volume_number       = 1 ;
                String      issue_number        = element.getElementsByTagName("vol").item(0).getTextContent() ;
                float       initial_value       = 9.99f ; 
                String      description         = element.getElementsByTagName("desc").item(0).getTextContent() ;
                String      release_date        = element.getElementsByTagName("release").item(0).getTextContent() ;
        
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
                String xml_pub = element.getElementsByTagName("pub").item(0).getTextContent() ;;
                String[] pubs = xml_pub.split(" [|] ") ;
        
                for (String pub : pubs) {
        
                    if (pub == "") {continue ;}
        
                    publishers.add( new Publisher(1, pub)) ; // 1 is placeholder number, wont actually be INSERTed into the DB with id 1
                }
        
                //compute creators
                String xml_cre = element.getElementsByTagName("creator").item(0).getTextContent() ;;
                String[] cres = xml_cre.split("( [|] )") ;
        
                for (String cre : cres) {
        
                    if (cre == "") {continue ;}
                    else {creators.add(new Creator(1,cre)) ;}
        
                }
        
                //---------------------------------------------------------
                // CREATE COMIC OBJECT FROM VALUES
                //---------------------------------------------------------
                nodeIndex++;
                return new Comic(1, 1, publishers, series, title, volume_number, issue_number, release_date, creators, characters, description, initial_value, signatures, value, grade, isSlabbed) ;
        
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
    try {
        XML xml = new XML("./comicsInput.xml");
        XMLComicAdapter x = new XMLComicAdapter(xml);
        Comic test = x.convertToComic() ;

        while (test != null) {

            System.out.println(test);
            System.out.println();
            test = x.convertToComic() ;

        }


    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
