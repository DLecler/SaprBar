package controllers;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import GraphicСalculations.Construction;
import GraphicСalculations.ReceivingData;
import GraphicСalculations.Rod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TableView;

public class OpenFileController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Rod> dataFileTable;

    @FXML
    private Button nextButton;

    @FXML
    private Text fileStatusText;

    @FXML
    private ChoiceBox<String> fileChoiceBox;

    @FXML
    void switchToSceneMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    String fileDirectory;
    public static File folder;

    boolean constr_get_SL = false, constr_get_SR = false;

    @FXML
    public void switchToSceneWorkPlace(ActionEvent event) throws IOException {
        if (!nextButton.isDisabled()) {
            ReceivingData.filePath = fileDirectory;
            ReceivingData.EnterData();
            Parent root = FXMLLoader.load(getClass().getResource("workPlace.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File[] files = folder.listFiles();
        String[] file_names = new String[files.length];

        int index = 0;

        for (File file : files) {
            if (file.isFile()) {
                file_names[index] = file.getName();
                index++;
            }
        }

        fileChoiceBox.setStyle("-fx-background-color: #FFFFE0; -fx-text-fill: #000000; -fx-font-size: 20px;");
        fileChoiceBox.getItems().addAll(file_names);
        fileChoiceBox.setOnAction(this::CheckFileData);

    }

    public void CheckFileData(ActionEvent event) {

        nextButton.setDisable(true);

        fileDirectory = folder + "\\" + fileChoiceBox.getValue();
        fileChoiceBox.setStyle("-fx-background-color: #32CD32; -fx-text-fill: #000000; -fx-font-size: 20px;");

        boolean correctPoint = true;

        Construction tableData = new Construction();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileDirectory))) {
            String line;

            int count_I = 0;

            while ((line = reader.readLine()) != null) {
                String[] strings = line.split(", ");
                try {

                    int i = Integer.parseInt(strings[0].trim());
                    double Li = Double.parseDouble(strings[1].trim());
                    double Ai = Double.parseDouble(strings[2].trim());
                    double E = Double.parseDouble(strings[3].trim());
                    double F_g = Double.parseDouble(strings[4].trim());
                    double Fl = Double.parseDouble(strings[5].trim());
                    double Fr = Double.parseDouble(strings[6].trim());
                    int lS = Integer.parseInt(strings[7].trim());
                    int rS = Integer.parseInt(strings[8].trim());

                    if (lS == 1) constr_get_SL = true;
                    if (rS == 1) constr_get_SR = true;
                    count_I++;

                    if (i != count_I || Li <= 0 || Ai <= 0 || E <= 0 || (lS != 0 && lS != 1) || (rS != 0 && rS != 1)) {
                        fileChoiceBox.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 20px;");
                        correctPoint = false;
                        break;
                    }
                    tableData.constraction_map.put(i, new Rod(i, Li, Ai, E, F_g, Fl, Fr, lS, rS));

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    fileChoiceBox.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 20px;");
                    correctPoint = false;
                    fileStatusText.setText("FILE CONTAINS INCORRECT DATA");
                    fileStatusText.setFill(Color.INDIANRED);

                }

                if (!correctPoint) break;
            }

        } catch (IOException e) {
            System.out.println("Error: File not found or cannot be read");
        }


        if (!constr_get_SL && !constr_get_SR) {
            fileChoiceBox.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 20px;");
            correctPoint = false;
        }

        if (correctPoint){

            fileStatusText.setText("CORRECT");
            fileStatusText.setFill(Color.GREEN);

            nextButton.setDisable(false);

            ReceivingData.filePath = fileDirectory;

        }
        else{
            fileStatusText.setText("INCORRECT");
            fileStatusText.setFill(Color.INDIANRED);

            nextButton.setDisable(true);
        }

    }

}