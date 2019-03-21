import org.w3c.dom.Node;
/**
 * This class searches the document for a given
 * phrase and creates an array of Strings with
 * lines containing the phrase
 * @author James Stevenson and Matt Zech
 *
 */
public class SearchResult {
    private Node[] lineNodeArray;
    private double timeToSearch;
    private int numberOfSentences;

    /**
     * Represents the array to be filled with strings, the
     * time it takes to search, and the count of lines
     */
    SearchResult(){
        lineNodeArray = new Node[0];
        timeToSearch = -1;
        numberOfSentences = 0;
    }
    /**
     * Constructors for the data fields
     * @param lineNodeArray
     * @param timeToSearch
     * @param numberOfSentences
     */
    SearchResult(Node[] lineNodeArray, double timeToSearch, int numberOfSentences) {
        this.lineNodeArray = lineNodeArray;
        this.timeToSearch = timeToSearch;
        this.numberOfSentences = numberOfSentences;
    }

    /**
     * method to retrieve the array
     * @return all sentences
     */
    public Node[] getLineNodeArray(){
        return lineNodeArray;
    }

    /**
     * retrieves the time it takes to search
     * @return time
     */
    public double getTimeToSearch() {
        return timeToSearch;
    }

    /**
     * retrieves the number of times the given phrase is spoken
     * @return number of sentences
     */
    public int getNumberOfSentences() {
        return numberOfSentences;
    }
}
