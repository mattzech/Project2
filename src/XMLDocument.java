import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;


/**
 * This class creates a new XML document of a
 * Shakespeare play and parses it, asking for user input
 * and rewriting it into a new file
 * @author James Stevenson and Matt Zech
 *
 */
public class XMLDocument {
    Document document;
    Element root;
	private String file;

	
	/**
	 * Importing XML Doc
	 * @param file
	 */
    XMLDocument(File file) {
        setXMLDocument(file);
    }
    /**
     * Creates new XML document
     * @param file
     */
    public void setXMLDocument(File file) {
    	this.file = "hamlet.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(this.file));
            root = document.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Finds number of different characters in play
     * @return integer representing speakers in the play
     */

    public int findNumberOfPersona() {
        NodeList personaList = root.getElementsByTagName("PERSONA");
        int numPersona = personaList.getLength();
        return numPersona;
    }
	/**
	 * Finds how many times the user-specified speaker
	 * talks during the play
	 * @param checkedName automatically converts it to upper case
	 * @return number of lines in the play
	 */
    public int findNumberOfSpeaker(String checkedName) {
        int count = 0;
        NodeList speakerList = root.getElementsByTagName("SPEAKER");
        Element speakerElement;
        for (int i = 0; i < speakerList.getLength(); i++) {
            speakerElement = (Element) speakerList.item(i);
            if (checkedName.equals(speakerElement.getTextContent()))
                count++;
        }
        return count;
    }
    /**
     * Searches the play for a user-specified word or phrase
     * @param fragment
     * @return list of lines where the phrase is spoken
     */
    public SearchResult findLine(String fragment) {
        if (fragment.equals(""))
            fragment = "To be, or not to be";
        int numberOfSentences = 0;
        NodeList lineList = root.getElementsByTagName("LINE");
        Element lineElement;
        Node[] lineNodeArray = new Node[lineList.getLength()];

        long lStartTime = System.nanoTime();
        for (int i = 0; i < lineList.getLength(); i++) {
            lineElement = (Element) lineList.item(i);
            if (lineElement.getTextContent().contains(fragment)) {
                lineNodeArray[numberOfSentences] = lineList.item(i);
                numberOfSentences++;
            }
        }
        long lEndTime = System.nanoTime();
        SearchResult result = new SearchResult(lineNodeArray, (lEndTime - lStartTime) / 1000000000.0, numberOfSentences);
        return result;
    }
    /**
     * Asks user what to replace these lines with
     * @param lineNodeArray
     * @param lineNumber
     * @param replacement
     */
    public void replaceLine(Node[] lineNodeArray, int lineNumber, String replacement) {
        System.out.println("Enter number of line you want to replace");
        System.out.println("Insert new line");
        lineNodeArray[lineNumber - 1].setTextContent(replacement);
        System.out.println("The sentence has been replaced as follows:\n" +
                lineNodeArray[lineNumber - 1].getTextContent() + "\nDo you want to save changes? (Y/N)");
    
	}
    /**
     * Overwrites current file with the play
     * containing new, replaced lines
     * @param newFilename
     * @return true
     */
    public boolean saveFile(String newFilename) {
        boolean success = true;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(newFilename));
            Source input = new DOMSource(this.document);
            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
            success = false;
        }
        if (success) {
            this.file = newFilename;
        }

        return success;

    }
}
