package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class MenuController {

    @FXML
    private Button CreateConstruction_Button, OpenFile_Button;

    public static boolean CC_B = true, OF_B = true;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToSceneCreateConstruction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("assemblyOfTheStructure.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToSceneOpenFile(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("openFile.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {
        OpenFile_Button.setDisable(CC_B);
        CreateConstruction_Button.setDisable(OF_B);
    }

    public void SelectWoringFolder(){

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Working Directory");
        chooser.setInitialDirectory(new File("c:\\"));
        File directory = chooser.showDialog(stage);

        if (directory != null) {
            CreateFile.folder = directory;
            OpenFileController.folder = directory;
            CC_B = false;
            OF_B = false;
        } else {
            CC_B = true;
            OF_B = true;
        }

        OpenFile_Button.setDisable(CC_B);
        CreateConstruction_Button.setDisable(OF_B);

    }

}
