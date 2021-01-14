/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author erics
 */
public class BookmarksController implements Initializable,UserFriendly {

    
    @FXML
    private Button loadButton;
    @FXML
    private Button removeButton;
    @FXML
    private ComboBox<String> bookmarksListBox;
    
    private final String about = "Welcome to the Bookmarks page!\n\n"
            + "Feel free to use the Navigation bar to return to the previous window or simply use the X in the top right corner.\n\n"
            + "Use the drop down menu to select a bookmark if you have one saved, then you have 2 options: \n"
            + "Press \"Load\" in order to render that web page in the search page.\n"
            + "Press \"Remove\" in order to remove the selected bookmark.";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> list = BookmarkModel.getBookmarks();
        bookmarksListBox.getItems().addAll(list);
    }
    
    private void loadButton() {
        String str = bookmarksListBox.getValue();
        if(str != null && !str.equals("")) {
            NewsDisplayController.loadedBookmark = str;
            NewsDisplayController.getWebEngine().load(str);
        }
    }
    
    private void removeButton() {
        String val = bookmarksListBox.getValue();
        BookmarkModel.removeBookmark(val);
        bookmarksListBox.getItems().remove(val);
    }
    
    @FXML
    private void handleLoadButton(ActionEvent event) {
        loadButton();
    }

    @FXML
    private void handleRemoveButton(ActionEvent event) {
        removeButton();
    }
    
    @Override
    public void displayAboutAlert(String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bookmarks - About");
        alert.setHeaderText("News Tracker");
        alert.setContentText("Hello! This is the developer, Eric Stranquist. I created this application to search political news articles without Google's \"Personalized Search Algorithms\". This is also my final project for CS3330 taught by Professor Wergles. All credits to him.");
        
        TextArea textArea = new TextArea(about);
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

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) loadButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAboutAlert(ActionEvent event) {
        displayAboutAlert(about);
    }
    
}
