package FileAdapters;

import Model.JavaObjects.*;
import Model.JavaObjects.Character;

import java.util.ArrayList;

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
    public String convertToFile(String filename, Comic[] comics) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToFile'");
    }



    @Override
    public Comic convertToComic(String filename) throws Exception{
        Document doc = adaptee.readFile(filename);
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
        
                return new Comic(1, 1, publishers, series, title, volume_number, issue_number, release_date, creators, characters, description, initial_value, signatures, value, grade, isSlabbed) ;
        
            }
        }
        return null;
    }
    
}
