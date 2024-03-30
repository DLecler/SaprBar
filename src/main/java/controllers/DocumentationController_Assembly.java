package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DocumentationController_Assembly {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public void switchToSceneAssemblyOfTheStructure(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("assemblyOfTheStructure.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void initialize() {

    }

}
