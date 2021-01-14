/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author erics
 */
public class MainMenuController extends AbstractController implements Initializable,UserFriendly {
    
    public Scene mainMenuScene;
    public Scene newsDisplayScene;
    public static NewsDisplayController newsDisplayController;
    private final String ABOUT = "NewsApi is used to obtain the news feed.  Developer information is available at https://newsapi.org.\n\n"
                + "Press \"Search\" to navigate to start searching news!\n"
                + "Press \"Bookmarks\" to view your bookmarked urls and load them into the search menu.\n"
                + "Press \"Settings\" to change your search settings. You will have 2 options: change language and specify the site to get results from (choose from a list)\n"
                + "You may also use the \"Navigation\" tab to get to either of the above buttons";
    public final String filePath = System.getProperty("user.dir")+"/saveData.json";
    
    @FXML
    private MenuItem bookmarksMenuButton;
    @FXML
    private Button bookmarksButton;
    
    public void ready(Stage stage){
        this.stage = stage;
        mainMenuScene = stage.getScene();
        System.out.println("MainMenu Ready()");
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("In shutdown hook");
            saveToJSON();
        }, "Shutdown-thread"));
        initFromJSON();
    }
    
    private void initFromJSON() {
        String operation = BookmarkModel.initJSONBookmarks(filePath);
        System.out.println("initFromJson() was a "+operation);
    }
    
    @FXML
    private void handleAboutAlert(ActionEvent event) {
        displayAboutAlert(ABOUT);
    }
    
    public void toSearchScene() {
        System.out.println("Going to Search Page");
        
        try {
            if(newsDisplayController == null) {
                System.out.println("newsDisplayController is null");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("NewsDisplay.fxml"));
                Parent newsDisplayRoot = (Parent)loader.load();
                newsDisplayController = loader.getController();
                newsDisplayController.mainMenuScene = mainMenuScene;
                newsDisplayController.mainMenuController = this;
                newsDisplayScene = new Scene(newsDisplayRoot);
                System.out.println("SearchScene Initialized");
            }
        } catch (Exception ex) {
            System.out.println("Exception thrown in toSearchScene()");
            return;
        }
        
        stage.setScene(newsDisplayScene);
        newsDisplayController.ready(stage);
    }
    
    @FXML
    private void handleSearchButton(ActionEvent event) {
        bookmarksButton.setDisable(false);
        bookmarksMenuButton.setDisable(false);
        toSearchScene();
    }

    @FXML
    private void handleBookmarksButton(ActionEvent event) {
        tryToBookmarksScene();
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
        tryToSettingsScene();
    }

    @Override
    public void displayAboutAlert(String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Main Menu - About");
        alert.setHeaderText("News Tracker");
        alert.setContentText("Hello! This is the developer, Eric Stranquist. I created this application to search political news articles without Google's \"Personalized Search Algorithms\". This is also my final project for CS3330 taught by Professor Wergles. All credits to him.");
        
        TextArea textArea = new TextArea(ABOUT);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
            
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        
        alert.showAndWait();
    }
    
    public void saveToJSON() {
        File file = new File(filePath);
        try{
            if (!file.exists()){
                file.createNewFile();
            }
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(BookmarkModel.sendToJSON());
                    writer.flush();
                }
        }catch(IOException e){
            System.out.println("saveToJSON failed");
        }
        System.out.println("saveToJSON success!!");
    }
    
}
