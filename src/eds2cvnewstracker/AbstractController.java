/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author erics
 */
public abstract class AbstractController {
    
    public Stage stage;
    public ObservableList<String> list;
    private Parent bookmarkRoot;
    private Parent settingsRoot;
    
    public void toBookmarksScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Bookmarks.fxml"));
        bookmarkRoot = (Parent)loader.load();
        BookmarksController bookmarksController = loader.<BookmarksController>getController();

        try {
            Scene scene = new Scene(bookmarkRoot, 600, 400);
            Stage subStage = new Stage();
            subStage.setX(900);
            subStage.setY(350);
            subStage.initModality(Modality.APPLICATION_MODAL);
            subStage.setScene(scene);
            subStage.showAndWait();
        } catch(IllegalStateException ex) {
            System.out.println("ERROR: IllegalStateException occured in BookmarksScene transfer.");
        }
    }
    
    public void tryToBookmarksScene() {
        try {
            toBookmarksScene();
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void toSettingsScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
        settingsRoot = (Parent)loader.load();
        SettingsController settingsController = loader.<SettingsController>getController();

        try {
            Scene scene = new Scene(settingsRoot, 600, 400);
            Stage subStage = new Stage();
            subStage.setX(900);
            subStage.setY(350);
            subStage.initModality(Modality.APPLICATION_MODAL);
            subStage.setScene(scene);
            subStage.showAndWait();
        } catch(IllegalStateException ex) {
            System.out.println("ERROR: IllegalStateException occured in SettingsScene transfer.");
        }
    }
    
    public void tryToSettingsScene() {
        try {
            toSettingsScene();
        } catch (IOException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
