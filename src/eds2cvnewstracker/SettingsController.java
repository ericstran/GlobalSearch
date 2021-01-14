/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import java.net.URL;
import java.util.Arrays;
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
public class SettingsController implements Initializable,UserFriendly{

    @FXML
    private ComboBox<String> languageOptions;
    @FXML
    private ComboBox<String> specificSiteOptions;

    private final String[] langList = {
        "-- Default: Any Languages --","Arabic","German","English","Spanish","French","Hebrew",
        "Italian","Dutch","Norwegian","Portuguese","Russian","Northern Sami","Udmurt","Chinese"
    };
    
    private final String[] domList = {
        "-- Default: All Sites --","gizmodo.com","engadget.com","theverge.com","axios.com",
        "sciencedaily.com","techradar.com","wired.com","techcrunch.com","abcnews.go.com",
        "cbsnews.com","cnn.com","foxnews.com","msnbc.com","nbcnews.com","nytimes.com",
        "latimes.com","usatoday.com","wsj.com","washingtonpost.com","dailynews.com",
        "bloomberg.com","vice.com","hbo.com","huffpost.com","tmz.com","cnet.com","npr.org",
        "hollywoodreporter.com","newsweek.com","time.com","usnews.com","theguardian.com"
    };
    
    private final String about = "Welcome to the Settings page!\n\n"
            + "Here you will be able to change what language your searched articles are, and specify a particular news outlet you would like to search on (only the current list is supported at this time).\n"
            + "Feel free to use the navigation bar to return to the previous page or press the X in the top right corner.\n\n"
            + "Use the \"Language\" option to select any of the supported languages.\n"
            + "Use the \"Specific Site\" option to refine your search to only 1 news vendor.\n\n"
            + "Or you may leave both spaces blank in which your search results will be of any language and of any news site (more than is on the list).\n\n"
            + "Once you have finished selecting your choices, press the \"Save Changes\" button to finalize your changes.\n"
            + "Your changes will revert back to default on closing of application.";
    
    @FXML
    private Button saveChanges;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateLanguageOptions();
        populateSpecificSiteOptions();
    }
    
    private void populateLanguageOptions() {
        languageOptions.getItems().addAll(Arrays.asList(langList));
    }
    
    private void populateSpecificSiteOptions() {
        specificSiteOptions.getItems().addAll(Arrays.asList(domList));
    }
    
    @FXML
    private void handleSaveSettings(ActionEvent event) {
        String lang = languageOptions.getValue();
        Boolean validLang = checkLang(lang);
        if(validLang) {
            SettingsModel.setLanguage(lang);
        } else {
            SettingsModel.setLanguage("");
        }
        
        String dom = specificSiteOptions.getValue();
        Boolean validDom = checkDom(dom);
        if(validDom) {
            SettingsModel.setDomain(dom);
        } else {
            SettingsModel.setDomain("");
        }
        Stage stage = (Stage) saveChanges.getScene().getWindow();
        stage.close();
    }
    
    private Boolean checkDom(String dom) {
        try {
            if(dom.equals(domList[0])) return false;
            for(String str : domList) {
                if(dom.equals(str)) {
                    return true;
                }
            }
        } catch(Exception ex) {
            return false;
        }
        return false;
    }
    
    private Boolean checkLang(String lang) {
        try {
            if(lang.equals(langList[0])) return false;
            for(String str : langList) {
                if(str.equals(lang)) {
                    return true;
                }
            }
        } catch(Exception ex) {
            return false;
        }
        return false;
    }
 
    @Override
    public void displayAboutAlert(String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings - About");
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
        Stage stage = (Stage) saveChanges.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAboutAlert(ActionEvent event) {
        displayAboutAlert(about);
    }
    
}
