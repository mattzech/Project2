import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * This is the display class for the GUI of the program
 * @author James Stevenson and Matt Zech
 *
 */
public class Display extends Application {

    private FlowPane pane = new FlowPane();
    private Button openBt = new Button("Choose Input File");
    private Button personaBt = new Button ("Find Number of Persona in Play");
    private final TextField speakerTF = new TextField();
    private Button speakerBt = new Button ("Find Number of Lines");
    private final TextField searchLineTF = new TextField();
    private Button searchLineBt = new Button ("Search for Line");
    private final FileChooser fileChooser = new FileChooser();
    private File file = new File("Shakespeare/hamlet.xml");
    private Text fileDisplay = new Text(60, 60, " Input file name: " + file.getName());
    private XMLDocument xmlDocument = new XMLDocument(file);
    private Text output = new Text(60, 60, "");
    private SearchResult searchResult = new SearchResult();
    private TextField replaceTF = new TextField();
    private Button replaceBt = new Button("Replace Line");
    private TextField lineNumTF = new TextField();
    private Button exitReplaceBt = new Button ("Do Not Replace Line");
    private Button yesReplaceBt = new Button ("Yes");
    private Button noReplaceBt = new Button ("No");
    private int replaceNum = 1;
    private File outputFile = new File ("Shakespeare/hamlet.xml");
    private Button chooseOutputBt = new Button ("Choose Output File");
    private Text outputFileDisplay = new Text(60, 60, " Output file name: " + outputFile.getName());
    private Button saveFileBt = new Button ("Save File");
    private Button yesSaveBt = new Button ("Yes");
    private Button noSaveBt = new Button ("No");

    /**
     * Represents the setup of the stage including text fields and 
     * buttons. It also includes the lay out and operattions of each
     * action within the stage 
     */
    public void start(Stage primaryStage) throws Exception {
        pane.setPadding(new Insets(11, 12, 13, 14));
        pane.setHgap(5);
        pane.setVgap(5);

        speakerTF.setPromptText("Insert name of speaker (Default: 'HAMLET')");
        speakerTF.setPrefColumnCount(25); //sets width of text field

        searchLineTF.setPromptText("Insert line (Default: 'To be, or not to be')");
        searchLineTF.setPrefColumnCount(25); //sets width of text field

        replaceTF.setPromptText("Insert replacement for line");
        replaceTF.setPrefColumnCount(20);

        lineNumTF.setPromptText("Insert line number to replace (Default: '1')");
        lineNumTF.setPrefColumnCount(20);

        pane.getChildren().add(openBt);
        pane.getChildren().add(fileDisplay);
        pane.getChildren().add(chooseOutputBt);
        pane.getChildren().add(outputFileDisplay);
        pane.getChildren().add(saveFileBt);
        pane.getChildren().add(personaBt);
        pane.getChildren().add(speakerTF);
        pane.getChildren().add(speakerBt);
        pane.getChildren().add(searchLineTF);
        pane.getChildren().add(searchLineBt);
        pane.getChildren().add(output);

        Scene scene = new Scene(pane, 500, 300);
        primaryStage.setTitle("MyJavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();


        openBt.setOnAction(e -> chooseInputFile(primaryStage));
        chooseOutputBt.setOnAction(e -> chooseOutputFile(primaryStage));
        personaBt.setOnAction(e -> findNumberOfPersona());
        speakerBt.setOnAction(e -> findNumberOfSpeaker());
        searchLineBt.setOnAction(e -> findLine());
        replaceBt.setOnAction(e -> replaceLine());
        exitReplaceBt.setOnAction(e -> exitReplace());
        yesReplaceBt.setOnAction(e -> yesReplaceLine());
        noReplaceBt.setOnAction(e -> noReplaceLine());
        saveFileBt.setOnAction(e -> checkSaveFile());
        yesSaveBt.setOnAction(e -> yesSaveFile());
        noSaveBt.setOnAction(e -> noSaveFile());
    }

    /**
     * Names the input file
     * @param primaryStage
     */
    public void chooseInputFile(Stage primaryStage){
        file = fileChooser.showOpenDialog(primaryStage);
        fileDisplay.setText(" Input file name: " + file.getName());
        xmlDocument.setXMLDocument(file);
    }

    /**
     * Names the file when outputting the changed XML
     * @param primaryStage
     */
    public void chooseOutputFile(Stage primaryStage) {
        outputFile = fileChooser.showOpenDialog(primaryStage);
        outputFileDisplay.setText(" Output file name: " + outputFile.getName());
    }

    /**
     * Outputs the total number of persona in the play
     */
    public void findNumberOfPersona(){
        output.setText("There are " + xmlDocument.findNumberOfPersona() + " personae in " + file.getName());
    }

    /**
     * Searches for a speaker and outputs how many
     * lines he/she has in the play
     */
    public void findNumberOfSpeaker() {
        String speaker;
        if (speakerTF.getText().equals(""))
            speaker = "HAMLET";
        else
            speaker = speakerTF.getText().toUpperCase();
        int count = xmlDocument.findNumberOfSpeaker(speaker);
        output.setText(speaker + " speaks " + count + " times");
    }

    /**
     * Searches for a word or phrase within the play and outputs
     * the different lines it appears in
     */
    public void findLine() {
        searchResult = xmlDocument.findLine(searchLineTF.getText());
        if (searchResult.getNumberOfSentences() == 0)
            output.setText("Sorry, fragment not found.\n");
        else {
            output.setText("The fragment has been found in the following sentence(s):\n");
            for (int i = 0; i < searchResult.getNumberOfSentences(); i++)
                output.setText(output.getText() + (i + 1) + ". '" + searchResult.getLineNodeArray()[i].getTextContent() + "'\n");
        }
        output.setText(output.getText() + "Search performed in " + searchResult.getTimeToSearch() + " seconds\n");
        if (searchResult.getNumberOfSentences() > 0) {
            pane.getChildren().addAll(replaceTF, replaceBt, exitReplaceBt, lineNumTF);
        }
    }

    /** 
     * Asks user to replace the lines with a new phrase and then
     * requests a save after replacing
     */
    public void replaceLine(){
        if (!lineNumTF.getText().equals("") && Integer.parseInt(lineNumTF.getText()) <= searchResult.getNumberOfSentences() &&
                Integer.parseInt(lineNumTF.getText()) > 0)
            replaceNum = Integer.parseInt(lineNumTF.getText());
        String previousLine = searchResult.getLineNodeArray()[replaceNum - 1].getTextContent();
        output.setText("'" + previousLine + "' will be replaced with: \n'" + replaceTF.getText() + "'\n" +
                "Do you want to save the changes? (Y/N)");
        pane.getChildren().addAll(yesReplaceBt, noReplaceBt);
        pane.getChildren().removeAll(replaceTF, replaceBt, exitReplaceBt, lineNumTF);
    }

    /** 
     * Cleans up the stage after saving changes
     */
    public void exitReplace(){
        pane.getChildren().removeAll(replaceTF, replaceBt, exitReplaceBt, lineNumTF);
        output.setText("");
    }

    /**
     * If the user selects yes to change the line
     */
    public void yesReplaceLine(){
        searchResult.getLineNodeArray()[replaceNum - 1].setTextContent(replaceTF.getText());
        pane.getChildren().removeAll(yesReplaceBt, noReplaceBt);
        output.setText("");
    }

    /**
     * If the user selects not to change the line
     */
    public void noReplaceLine(){
        pane.getChildren().removeAll(yesReplaceBt, noReplaceBt);
        pane.getChildren().addAll(replaceTF, replaceBt, exitReplaceBt, lineNumTF);
        findLine();
    }

    /**
     * Method to check for overwriting possibility so if the names
     * are the same, it can still be saved and replaced
     */ 
    
    public void checkSaveFile(){
        // if same, open yes no and output choice
    	if (file.getName().equals(outputFile.getName()))
        {
            output.setText("Would you like to overwrite file: " + file.getName());
            pane.getChildren().addAll(yesSaveBt, noSaveBt);
        }
        // else saveFile
        else
            saveFile();
    }
    /**
     * Runs the saveFile method and cleans up the stage
     */
    public void yesSaveFile(){
        saveFile();
        pane.getChildren().removeAll(yesSaveBt, noSaveBt);
    }

    /**
     * Only makes more room on the stage
     */
    public void noSaveFile(){
        pane.getChildren().removeAll(yesSaveBt, noSaveBt);
        output.setText("");
    }

    /** 
     * Uses transformers to save the file within the computer
     * Will output a message if successful
     */
    public void saveFile() {
        boolean success = true;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(outputFile);
            Source input = new DOMSource(xmlDocument.document);
            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
            success = false;
        }
        if (success)
            output.setText("File saved successfully!");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
