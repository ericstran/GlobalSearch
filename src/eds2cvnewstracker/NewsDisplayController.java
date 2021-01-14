/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author erics
 */
public class NewsDisplayController extends AbstractController implements Initializable,UserFriendly {
    
    private Stage stage;
    public Scene mainMenuScene;
    public MainMenuController mainMenuController;
    
    private NewsManager newsManager;
    private ArrayList<NewsStory> stories;
    
    private static String currentWebUrl;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private ListView newsListView;
    
    @FXML
    private WebView webView;
    
    public static String loadedBookmark = "";
    private String searchString = "Bitcoin";
    private static WebEngine webEngine;
    private ObservableList<String> newsListItems;
    
    private final String ABOUT = "Welcome to the Search page!\n\n"
            + "There is a search bar where you can type in any word or phrase.\n"
            + "In order to execute the search you must press the \"Search\" button.\n"
            + "The search results will be listed out below the search bar. Click on one to load the web page.\n\n"
            + "If you want to bookmark a site to read another time, press the \"Bookmark\" button next to \"Search\".\n\n"
            + "It is advised to not try to login to websites or otherwise interact with the websites due to unresponsiveness."
            + "The website may load a different screen, but it will not work like if you were searching on Google.\n\n"
            + "In order to go back, you can press \"backspace\" on the keyboard.\n\n"
            + "Feel free to use the Navigation tab to get around.";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void ready(Stage stage) {
        this.stage = stage;
        webEngine = webView.getEngine();
        newsManager = new NewsManager();
        newsListItems = FXCollections.observableArrayList();
        
        newsListView.getSelectionModel().selectedIndexProperty().addListener(
                    (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

                        if((int) new_val < 0 || (int) new_val > (stories.size() - 1)){
                            return; 
                        }

                        NewsStory story = stories.get((int) new_val); 
                        webEngine.load(story.webUrl);
                        currentWebUrl = story.webUrl;
                    }
            ); 
        
        if(!"".equals(loadedBookmark)) {
            webEngine.load(loadedBookmark);
        } else {
            searchTextField.setText(searchString);
            loadNews();
        }
    }
    
    public static WebEngine getWebEngine() {
        return webEngine;
    }
    
    private void loadNews() {
        try {
            newsManager.load(searchString);
        } catch(Exception ex){
            displayExceptionAlert(ex); 
            return; 
        }
        
        stories = newsManager.getNewsStories(); 
        newsListItems.clear(); 
        
        stories.forEach((story) -> {
            newsListItems.add(story.title);
        });
        
        newsListView.setItems(newsListItems);
        
        if(stories.size() > 0){
            newsListView.getSelectionModel().select(0);
            newsListView.getFocusModel().focus(0); 
            newsListView.scrollTo(0); 
        }
    }
    
    @FXML
    private void handleSearch(ActionEvent event) {
        if (searchTextField.getText().equals("")) {
            displayErrorAlert("The search field cannot be blank. Enter one or more search words.");
            return;
        }
        searchString = searchTextField.getText();
        loadNews();
    }
    
    @FXML
    private void handleUpdate(ActionEvent event) {
        loadNews();
    }
    
    @FXML
    private void handleAboutAlert(ActionEvent event) {
        displayAboutAlert(ABOUT);
    }
    
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void displayExceptionAlert(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("An Exception Occurred!");
        alert.setContentText("An exception occurred.  View the exception information below by clicking Show Details.");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    @FXML
    private void handleMainMenu(ActionEvent event) {
        stage.setScene(mainMenuScene);
    }

    @FXML
    private void handleBookmarks(ActionEvent event) {
        tryToBookmarksScene();
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        tryToSettingsScene();
    }

    @FXML
    private void handleBookmark(ActionEvent event) {
        for(NewsStory story : stories) {
            if((story.webUrl).equals(currentWebUrl)) {
                BookmarkModel.addBookmark(currentWebUrl,story.jsonStory);
            }
        }
    }
    
    @Override
    public void displayAboutAlert(String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search - About");
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
    
}
