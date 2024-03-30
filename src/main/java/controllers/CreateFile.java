package controllers;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import GraphicСalculations.Rod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateFile {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button NextButton;

    @FXML
    private Button createButton;

    @FXML
    private TextField fileName_add;

    @FXML
    void switchToSceneMenu(ActionEvent event) throws IOException {
        if (!NextButton.isDisabled()) {
            Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public static File folder;

    @FXML
    void initialize() {

        NextButton.setDisable(true);

        createButton.setOnAction(event -> {
            String fileName = fileName_add.getText();
            if (fileName.length() != 0){
                CreateFile(fileName);
                NextButton.setDisable(false);
                copyDataToFile(folder + "\\" + fileName + ".txt");
            }
        });
    }



    public void switchToSceneCreateConstruction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("assemblyOfTheStructure.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void copyDataToFile(String fileName) {

        try (FileWriter writer = new FileWriter(fileName)) {
            for (Map.Entry<Integer, Rod> entry : AssemblyOfTheStructure.data_to_calc.constraction_map.entrySet()) {
                String I = Integer.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getI());
                String L = Double.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getL());
                String A = Double.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getA());
                String E = Double.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getE());
                String FG = Double.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getFG());
                String FL = Double.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getFL());
                String FR = Double.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getFR());
                String SL = Integer.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getSL());
                String SR = Integer.toString(AssemblyOfTheStructure.data_to_calc.constraction_map.get(entry.getKey()).getSR());

                writer.write(I + ", " + L + ", " + A + ", " + E + ", " + FG + ", " + FL + ", " + FR + ", " + SL + ", " + SR + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void CreateFile(String fileName){

        fileName += ".txt";

        try {
            File file = new File(folder + "\\" + fileName);
            if (file.createNewFile()) {
                System.out.println("File created successfully.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }

    }

}
