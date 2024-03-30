package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import GraphicСalculations.Construction;
import GraphicСalculations.GetSystem;
import GraphicСalculations.ReceivingData;
import GraphicСalculations.Rod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class DrawingController {

    @FXML
    private Canvas mainCanvas;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button ADD_ROD_Button, DELETE_ROD_Button, CHANGE_Rod_Button, SAVE_Button;
    @FXML
    private TextField add_L, add_A, add_E, add_FG, add_FL, add_FR, add_SL, add_SR, add_NUMB_Rod;

    public static int index_rodes = 0;
    public static Construction data_to_calc, data_to_render;
    public static ArrayList<Double> lengths, heights;

    public static double maxLength, maxHeight, allLength, underLenY;
    public static boolean constr_get_SL, constr_get_SR;

    @FXML
    public void switchToSceneDOCUMENTATION(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("documentation_drawing.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    public void switchToSceneWorkPlace(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("workPlace.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void initialize() {

        CopyStartData();
        draw();

        ADD_ROD_Button.setOnAction(event -> {

            boolean correct_point;
            double receivedL = 0, receivedA = 0, receivedE = 0, receivedFG = 0, receivedFR = 0, receivedFL = 0;
            int receivedNumberRod = -1, receivedSL = 0, receivedSR = 0;


            if (add_NUMB_Rod.getText().length() != 0) {
                try {
                    receivedNumberRod = Integer.parseInt(add_NUMB_Rod.getText());

                    if (receivedNumberRod > 0 && receivedNumberRod < data_to_calc.constraction_map.size() + 1)
                        add_NUMB_Rod.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
                    else
                        add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                }
            }

            try {
                receivedL = Double.parseDouble(add_L.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_L.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
            try {
                receivedA = Double.parseDouble(add_A.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_A.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
            try {
                receivedE = Double.parseDouble(add_E.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_E.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
            try {
                receivedFG = Double.parseDouble(add_FG.getText());
                add_FG.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FG.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
            try {
                receivedFL = Double.parseDouble(add_FL.getText());
                add_FL.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
            try {
                receivedFR = Double.parseDouble(add_FR.getText());
                add_FR.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
            try {
                receivedSL = Integer.parseInt(add_SL.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
            try {
                receivedSR = Integer.parseInt(add_SR.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }

            correct_point = CheckAddData(true);

            if (correct_point) {

                if (receivedSL == 1) receivedFL = 0;
                if (receivedSR == 1) receivedFR = 0;

                if (add_NUMB_Rod.getText().length() == 0) {

                    if (constr_get_SR) {
                        add_L.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_A.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_E.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_FG.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_FL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_FR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        correct_point = false;
                    }

                    if ((receivedSL == 1 && receivedSR == 1 || receivedSL == 1 && receivedSR == 0) && data_to_calc.constraction_map.size() == 0)
                        AddRod(false, index_rodes, receivedL, receivedA, receivedE, receivedFG, receivedFL, receivedFR, receivedSL, receivedSR);

                    else {
                        if (receivedSL == 1 && receivedSR == 1) {
                            add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            correct_point = false;
                        }
                        if (receivedSL == 1 && receivedSR == 0) {
                            add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            correct_point = false;
                        }
                    }

                    if ((receivedSL == 0 && receivedSR == 0 || receivedSL == 0 && receivedSR == 1) && !constr_get_SR)
                        AddRod(false, index_rodes, receivedL, receivedA, receivedE, receivedFG, receivedFL, receivedFR, receivedSL, receivedSR);
                    else {
                        if (receivedSL == 0 && receivedSR == 0 || receivedSL == 0 && receivedSR == 1) {
                            add_L.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_A.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_E.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_FG.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_FL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_FR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        }
                    }
                }
                else{

                    if (receivedSL == 0 && receivedSR == 0){
                        if (!constr_get_SL)
                            AddRod(true, receivedNumberRod - 1, receivedL, receivedA, receivedE, receivedFG, receivedFL, receivedFR, receivedSL, receivedSR);
                        else {
                            if (receivedNumberRod == 1) {
                                add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                                correct_point = false;
                            }
                            else
                                AddRod(true, receivedNumberRod - 1, receivedL, receivedA, receivedE, receivedFG, receivedFL, receivedFR, receivedSL, receivedSR);
                        }
                    }


                    if (receivedSL == 1 && receivedSR == 1){
                        add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        correct_point = false;
                    }


                    if (receivedSL == 1 && receivedSR == 0){
                        if (!constr_get_SL){
                            if (receivedNumberRod != 1) {
                                add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                                correct_point = false;
                            }
                            else
                                AddRod(true, 0, receivedL, receivedA, receivedE, receivedFG, receivedFL, receivedFR, receivedSL, receivedSR);
                        }
                        else{
                            if (receivedNumberRod != 1) {
                                add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                                correct_point = false;
                            }
                            else {
                                add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                                add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                                correct_point = false;
                            }
                        }
                    }


                    if (receivedSL == 0 && receivedSR == 1){
                        if (constr_get_SL && receivedNumberRod == 1)
                            add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        if (constr_get_SR){
                            add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            correct_point = false;
                        }
                        else{
                            add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                            correct_point = false;
                        }
                    }

                }

            }

            if (correct_point) {

                draw();

                add_L.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_A.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_E.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_FG.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_FL.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_FR.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_SL.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_SR.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                add_NUMB_Rod.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");

                add_L.clear();
                add_A.clear();
                add_E.clear();
                add_FG.clear();
                add_FL.clear();
                add_FR.clear();
                add_SL.clear();
                add_SR.clear();
                add_NUMB_Rod.clear();


                if (add_NUMB_Rod.getText().length() == 0)
                    add_NUMB_Rod.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }


        });

        DELETE_ROD_Button.setOnAction(event -> {

            int receivedNumberRod;
            boolean correct_point = true;


            add_L.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_A.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_E.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_FG.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_FL.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_FR.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_SL.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_SR.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");

            add_L.clear();
            add_A.clear();
            add_E.clear();
            add_FG.clear();
            add_FL.clear();
            add_FR.clear();
            add_SL.clear();
            add_SR.clear();

            if (add_NUMB_Rod.getText().length() != 0) {
                try {
                    receivedNumberRod = Integer.parseInt(add_NUMB_Rod.getText());

                    if (receivedNumberRod > 0 && receivedNumberRod < data_to_calc.constraction_map.size() + 1)
                        add_NUMB_Rod.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
                    else {
                        add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        correct_point = false;
                    }

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
            }
            else{
                add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (correct_point) {
                int i;
                add_NUMB_Rod.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                try {
                    i = Integer.parseInt(add_NUMB_Rod.getText());
                    add_NUMB_Rod.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");

                    if (data_to_calc.constraction_map.size() == 1 && i == 1) {

                        data_to_calc = new Construction();
                        data_to_render = new Construction();
                        lengths = new ArrayList<Double>();
                        heights = new ArrayList<Double>();
                        maxLength = 0;
                        maxHeight = 0;
                        allLength = 0;
                        underLenY = 0;
                        constr_get_SL = false;
                        constr_get_SR = false;
                        index_rodes--;

                    } else DeleteRod(i - 1);

                    add_NUMB_Rod.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
                    add_NUMB_Rod.clear();
                    draw();


                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                }
            }

        });

        CHANGE_Rod_Button.setOnAction(event -> {


            int receivedNumberRod;
            boolean correct_point = true;

            if (add_NUMB_Rod.getText().length() != 0) {
                try {
                    receivedNumberRod = Integer.parseInt(add_NUMB_Rod.getText());

                    if (receivedNumberRod > 0 && receivedNumberRod < data_to_calc.constraction_map.size() + 1)
                        add_NUMB_Rod.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
                    else {
                        add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                        correct_point = false;
                    }

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
            }
            else{
                add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (correct_point) {
                int i;
                add_NUMB_Rod.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                try {
                    i = Integer.parseInt(add_NUMB_Rod.getText());
                    add_NUMB_Rod.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");

                    ChangeRod(i - 1);
                    draw();

                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                }
            }

        });

        SAVE_Button.setOnAction(event -> {

            if (constr_get_SL || constr_get_SR) ChangeDataToFile(ReceivingData.filePath);
            else
                SAVE_Button.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF;");

        });

    }

    public static void ChangeDataToFile(String filePath) {

        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File created successfully.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < data_to_calc.constraction_map.size(); i++) {
                int I = data_to_calc.constraction_map.get(i).getI();
                double L = data_to_calc.constraction_map.get(i).getL();
                double A = data_to_calc.constraction_map.get(i).getA();
                double E = data_to_calc.constraction_map.get(i).getE();
                double FG = data_to_calc.constraction_map.get(i).getFG();
                double FL = data_to_calc.constraction_map.get(i).getFL();
                double FR = data_to_calc.constraction_map.get(i).getFR();
                int SL = data_to_calc.constraction_map.get(i).getSL();
                int SR = data_to_calc.constraction_map.get(i).getSR();
                writer.write(I + ", " + L + ", " + A + ", " + E + ", " + FG + ", " + FL + ", " + FR + ", " + SL + ", " + SR + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ReceivingData.lengths = new ArrayList<Double>();
        ReceivingData.heights = new ArrayList<Double>();
        ReceivingData.maxLength = 0;
        ReceivingData.maxHeight = 0;
        ReceivingData.allLength_epur = 0;
        ReceivingData.changeMaxHeight_epur = 0;

        ReceivingData.EnterData();

    }

    public static void CopyStartData() {

        index_rodes = 0;
        maxLength = 0;
        maxHeight = 0;
        allLength = 0;
        underLenY = 0;

        data_to_calc = new Construction();
        data_to_render = new Construction();

        lengths = new ArrayList<Double>();
        heights = new ArrayList<Double>();

        constr_get_SL = false;
        constr_get_SR = false;

        for (int i = 0; i < GetSystem.data_to_calculations_construction.constraction_map.size(); i++) {

            int I = GetSystem.data_to_calculations_construction.constraction_map.get(i).getI();
            double L = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
            double A = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();
            double E = GetSystem.data_to_calculations_construction.constraction_map.get(i).getE();
            double FG = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFG();
            double FL = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFL();
            double FR = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFR();
            int SL = GetSystem.data_to_calculations_construction.constraction_map.get(i).getSL();
            int SR = GetSystem.data_to_calculations_construction.constraction_map.get(i).getSR();

            lengths.add(L);
            heights.add(A);

            if (maxLength < L) maxLength = L;
            if (maxHeight < A) maxHeight = A;

            if (SL == 1) constr_get_SL = true;
            if (SR == 1) constr_get_SR = true;

            data_to_calc.constraction_map.put(i, new Rod(I, L, A, E, FG, FL, FR, SL, SR));
            index_rodes++;

        }

        UpdatingRenderingData();
        PreparationForses();
    }

    public static void UpdatingRenderingData() {

        data_to_render.constraction_map.clear();
        lengths = new ArrayList<>();
        heights = new ArrayList<>();
        maxLength = 0;
        maxHeight = 0;
        allLength = 0;
        for (int i = 0; i < data_to_calc.constraction_map.size(); i++) {

            int I = data_to_calc.constraction_map.get(i).getI();
            double L = data_to_calc.constraction_map.get(i).getL();
            double A = data_to_calc.constraction_map.get(i).getA();
            double E = data_to_calc.constraction_map.get(i).getE();
            double FG = data_to_calc.constraction_map.get(i).getFG();
            double FL = data_to_calc.constraction_map.get(i).getFL();
            double FR = data_to_calc.constraction_map.get(i).getFR();
            int SL = data_to_calc.constraction_map.get(i).getSL();
            int SR = data_to_calc.constraction_map.get(i).getSR();

            lengths.add(L);
            heights.add(A);
            if (maxLength < L) maxLength = L;
            if (maxHeight < A) maxHeight = A;
            allLength += L;

            data_to_render.constraction_map.put(i, new Rod(I, L, A, E, FG, FL, FR, SL, SR));

        }
    }

    public boolean CheckAddData(boolean procedure_ADD){

        boolean correct_point = true;

        double checkL, checkA, checkE, checkFG, checkFL, checkFR;
        int checkSL, checkSR;

        if (add_L.getText().length() != 0){
            try {
                checkL = Double.parseDouble(add_L.getText());
                if (add_L.getText().length() != 0 && (checkL <= 0 || !add_L.getText().matches("-?\\d+"))) {
                    add_L.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
                else
                    add_L.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_L.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        if (add_A.getText().length() != 0){
            try {
                checkA = Double.parseDouble(add_A.getText());
                if (add_A.getText().length() != 0 && (checkA <= 0 || !add_A.getText().matches("-?\\d+"))){
                    add_A.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
                else
                    add_A.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_A.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        if (add_E.getText().length() != 0){
            try{
                checkE = Double.parseDouble(add_E.getText());
                if (add_E.getText().length() != 0 && (checkE <= 0 || !add_E.getText().matches("-?\\d+"))){
                    add_E.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
                else
                    add_E.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_E.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        if (add_FG.getText().length() != 0){
            try{
                checkFG = Double.parseDouble(add_FG.getText());
                if (add_FG.getText().length() != 0 && (!add_FG.getText().matches("-?\\d+"))){
                    add_FG.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
                else
                    add_FG.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FG.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        if (add_FL.getText().length() != 0){
            try{
                checkFL = Double.parseDouble(add_FL.getText());
                if (add_FL.getText().length() != 0 && (!add_FL.getText().matches("-?\\d+"))){
                    add_FL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
                else
                    add_FL.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        if (add_FR.getText().length() != 0){
            try{
                checkFR = Double.parseDouble(add_FR.getText());
                if (add_FR.getText().length() != 0 && (!add_FR.getText().matches("-?\\d+"))){
                    add_FR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        if (add_SL.getText().length() != 0){
            try{
                checkSL = Integer.parseInt(add_SL.getText());
                if (add_SL.getText().length() != 0 && (checkSL != 0 && checkSL != 1 || !add_SL.getText().matches("-?\\d+"))){
                    add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
                else
                    add_SL.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        if (add_SR.getText().length() != 0){
            try{
                checkSR = Integer.parseInt(add_SR.getText());
                if (add_SR.getText().length() != 0 && (checkSR != 0 && checkSR != 1 || !add_SR.getText().matches("-?\\d+"))){
                    add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
                else
                    add_SR.setStyle("-fx-background-color: #8FBC8F; -fx-text-fill: #000000; -fx-font-size: 15px;");
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }

        if (procedure_ADD){

            if (add_L.getText().length() == 0){
                add_L.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (add_A.getText().length() == 0){
                add_A.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (add_E.getText().length() == 0){
                add_E.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (add_FG.getText().length() == 0){
                add_FG.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (add_FL.getText().length() == 0){
                add_FL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (add_FR.getText().length() == 0){
                add_FR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (add_SL.getText().length() == 0){
                add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }

            if (add_SR.getText().length() == 0){
                add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                correct_point = false;
            }
        }
        return correct_point;

    }

    public static void AddRod(boolean flag, int index_add_rod, double L, double A, double E, double FG, double FL, double FR, int SL, int SR) {

        if (SL == 1) constr_get_SL = true;
        if (SR == 1) constr_get_SR = true;

        data_to_render.constraction_map = new HashMap<>();

        if (!flag) {
            data_to_calc.constraction_map.put(index_rodes, new Rod(index_rodes + 1, L, A, E, FG, FL, FR, SL, SR));
            index_rodes++;
        }
        else {

            HashMap<Integer, Rod> map = new HashMap<>();

            for (int i = 0; i < data_to_calc.constraction_map.size(); i++) {
                int I_ = data_to_calc.constraction_map.get(i).getI();
                double L_ = data_to_calc.constraction_map.get(i).getL();
                double A_ = data_to_calc.constraction_map.get(i).getA();
                double E_ = data_to_calc.constraction_map.get(i).getE();
                double FG_ = data_to_calc.constraction_map.get(i).getFG();
                double FL_ = data_to_calc.constraction_map.get(i).getFL();
                double FR_ = data_to_calc.constraction_map.get(i).getFR();
                int SL_ = data_to_calc.constraction_map.get(i).getSL();
                int SR_ = data_to_calc.constraction_map.get(i).getSR();
                map.put(i, new Rod(I_, L_, A_, E_, FG_, FL_, FR_, SL_, SR_));
            }

            data_to_calc.constraction_map.clear();
            int point = 0;

            boolean find_point = false;

            for (int i = 0; i < map.size(); i++) {

                if (point == index_add_rod) {
                    find_point = true;
                    data_to_calc.constraction_map.put(point, new Rod(point + 1, L, A, E, FG, FL, FR, SL, SR));
                    i--;
                    index_rodes++;
                } else {
                    int I_ = map.get(i).getI();
                    double L_ = map.get(i).getL();
                    double A_ = map.get(i).getA();
                    double E_ = map.get(i).getE();
                    double FG_ = map.get(i).getFG();
                    double FL_ = map.get(i).getFL();
                    double FR_ = map.get(i).getFR();
                    int SL_ = map.get(i).getSL();
                    int SR_ = map.get(i).getSR();
                    if (!find_point)
                        data_to_calc.constraction_map.put(point, new Rod(I_, L_, A_, E_, FG_, FL_, FR_, SL_, SR_));
                    else
                        data_to_calc.constraction_map.put(point, new Rod(I_+1, L_, A_, E_, FG_, FL_, FR_, SL_, SR_));
                }
                point++;
            }
            map.clear();
        }

        UpdatingRenderingData();
        PreparationForses();

    }

    public void DeleteRod(int indexToRemove) {

        boolean findDelInd = false;
        int i_point = 0;
        HashMap<Integer, Rod> map = new HashMap<>();

        for (int i = 0; i < data_to_calc.constraction_map.size(); i++) {
            int I = data_to_calc.constraction_map.get(i).getI();
            double L = data_to_calc.constraction_map.get(i).getL();
            double A = data_to_calc.constraction_map.get(i).getA();
            double E = data_to_calc.constraction_map.get(i).getE();
            double FG = data_to_calc.constraction_map.get(i).getFG();
            double FL = data_to_calc.constraction_map.get(i).getFL();
            double FR = data_to_calc.constraction_map.get(i).getFR();
            int SL = data_to_calc.constraction_map.get(i).getSL();
            int SR = data_to_calc.constraction_map.get(i).getSR();

            if (i != indexToRemove) {
                if (findDelInd) map.put(i_point, new Rod(I - 1, L, A, E, FG, FL, FR, SL, SR));
                else map.put(i_point, new Rod(I, L, A, E, FG, FL, FR, SL, SR));
                i_point++;
            } else {
                findDelInd = true;
                if (SL == 1) constr_get_SL = false;
                if (SR == 1) constr_get_SR = false;
            }
        }

        data_to_calc.constraction_map.clear();

        for (int i = 0; i < map.size(); i++) {
            int I = map.get(i).getI();
            double L = map.get(i).getL();
            double A = map.get(i).getA();
            double E = map.get(i).getE();
            double FG = map.get(i).getFG();
            double FL = map.get(i).getFL();
            double FR = map.get(i).getFR();
            int SL = map.get(i).getSL();
            int SR = map.get(i).getSR();
            data_to_calc.constraction_map.put(i, new Rod(I, L, A, E, FG, FL, FR, SL, SR));
        }
        map.clear();

        index_rodes--;

        UpdatingRenderingData();
        PreparationForses();

    }

    public void ChangeRod(int indexToChange) {

        boolean correct_point = true;
        double receivedL = 0, receivedA = 0, receivedE = 0, receivedFG = 0, receivedFR = 0, receivedFL = 0;
        int receivedSL = 0, receivedSR = 0;

        if (add_L.getText().length() != 0) {
            try {
                receivedL = Double.parseDouble(add_L.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_L.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }
        if (add_A.getText().length() != 0) {
            try {
                receivedA = Double.parseDouble(add_A.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_A.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }
        if (add_E.getText().length() != 0) {
            try {
                receivedE = Double.parseDouble(add_E.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_E.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }
        if (add_FG.getText().length() != 0) {
            try {
                receivedFG = Double.parseDouble(add_FG.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FG.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }
        if (add_FL.getText().length() != 0) {
            try {
                receivedFL = Double.parseDouble(add_FL.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }
        if (add_FR.getText().length() != 0) {
            try {
                receivedFR = Double.parseDouble(add_FR.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_FR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }
        if (add_SL.getText().length() != 0) {
            try {
                receivedSL = Integer.parseInt(add_SL.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }
        if (add_SR.getText().length() != 0) {
            try {
                receivedSR = Integer.parseInt(add_SR.getText());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
            }
        }

        correct_point = CheckAddData(false);

        if (correct_point) {

            if (receivedSL == 1) {
                if (indexToChange != 0) {
                    add_SL.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    if (!constr_get_SL)
                        add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
            }

            if (receivedSR == 1) {
                if (indexToChange != data_to_calc.constraction_map.size() - 1) {
                    add_SR.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    if (!constr_get_SR)
                        add_NUMB_Rod.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: #FFFFFF; -fx-font-size: 15px;");
                    correct_point = false;
                }
            }

        }

        if (correct_point){

            if (add_L.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setL(receivedL);
            if (add_A.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setA(receivedA);
            if (add_E.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setE(receivedE);
            if (add_FG.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setFG(receivedFG);
            if (add_FL.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setFL(receivedFL);
            if (add_FR.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setFR(receivedFR);
            if (add_SL.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setSL(receivedSL);
            if (add_SR.getText().length() != 0) data_to_calc.constraction_map.get(indexToChange).setSR(receivedSR);

            if (add_SL.getText().length() != 0 && receivedSL == 0) {
                if (data_to_calc.constraction_map.get(0).getSL() == 0)
                    constr_get_SL = false;
            }
            if (add_SR.getText().length() != 0 && receivedSR == 0) {
                if (data_to_calc.constraction_map.get(data_to_calc.constraction_map.size()-1).getSR() == 0)
                    constr_get_SR = false;
            }

            if (receivedSL == 1) constr_get_SL = true;
            if (receivedSR == 1) constr_get_SR = true;

            if (constr_get_SL && indexToChange == 0)
                data_to_calc.constraction_map.get(indexToChange).setFL(0);
            if (constr_get_SR && indexToChange == data_to_calc.constraction_map.size()-1)
                data_to_calc.constraction_map.get(indexToChange).setFR(0);


            add_NUMB_Rod.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_L.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_A.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_E.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_FG.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_FL.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_FR.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_SL.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");
            add_SR.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-font-size: 15px;");

            add_NUMB_Rod.clear();
            add_L.clear();
            add_A.clear();
            add_E.clear();
            add_FG.clear();
            add_FL.clear();
            add_FR.clear();
            add_SL.clear();
            add_SR.clear();
        }

        UpdatingRenderingData();
        PreparationForses();
    }

    public static void FittingData(ArrayList<Integer> arr_index_lengths, ArrayList<Integer> arr_index_heights) {

        double k_lenghts = 1.0, k_heights = 1.0;
        double delta_k_lenghts = 0.05, delta_k_heights = 0.05;

        for (int i = arr_index_lengths.size() - 1; i >= 0; i--) {
            allLength -= data_to_render.constraction_map.get(arr_index_lengths.get(i)).getL();
            double new_len_rod = maxLength * k_lenghts;
            allLength += new_len_rod;

            if (i != 0){
                double L_1 = data_to_render.constraction_map.get(arr_index_lengths.get(i)).getL();
                double L_2 = data_to_render.constraction_map.get(arr_index_lengths.get(i-1)).getL();
                if (L_1 != L_2) k_lenghts -= delta_k_lenghts;
            }

            data_to_render.constraction_map.get(arr_index_lengths.get(i)).setL(new_len_rod);

        }

        for (int i = arr_index_heights.size() - 1; i >= 0; i--) {
            double new_height_rod = maxHeight * k_heights;

            if (i != 0){
                double H_1 = data_to_render.constraction_map.get(arr_index_lengths.get(i)).getA();
                double H_2 = data_to_render.constraction_map.get(arr_index_lengths.get(i-1)).getA();
                if (H_1 != H_2) k_heights -= delta_k_heights;
            }

            data_to_render.constraction_map.get(arr_index_heights.get(i)).setA(new_height_rod);
        }


        if (allLength < 900){
            while (allLength < 900){
                double newAllLength = 0, newMAXLength = 0;
                for (int i = 0; i < data_to_render.constraction_map.size(); i++) {
                    double newL = data_to_render.constraction_map.get(i).getL() * 1.02;
                    data_to_render.constraction_map.get(i).setL(newL);
                    newAllLength += newL;
                    if (newMAXLength < newL) newMAXLength = newL;
                }
                allLength = newAllLength;
                maxLength = newMAXLength;
            }
        }
        if (allLength > 900){
            while (allLength > 900){
                double newAllLength = 0, newMAXLength = 0;
                for (int i = 0; i < data_to_render.constraction_map.size(); i++) {
                    double newL = data_to_render.constraction_map.get(i).getL() * 0.98;
                    data_to_render.constraction_map.get(i).setL(newL);
                    newAllLength += newL;
                    if (newMAXLength < newL) newMAXLength = newL;
                }
                allLength = newAllLength;
                maxLength = newMAXLength;
            }
        }

        if (maxHeight < 200){
            while (maxHeight * 1.02 < 200) {
                double newMAXHeight = 0;
                for (int i = 0; i < data_to_render.constraction_map.size(); i++) {
                    double newH = data_to_render.constraction_map.get(i).getA() * 1.02;
                    if (newMAXHeight < newH) newMAXHeight = newH;
                    data_to_render.constraction_map.get(i).setA(newH);
                }
                maxHeight = newMAXHeight;
            }

        }
        if (maxHeight > 200) {
            while (maxHeight > 200) {
                double newMAXHeight = 0;
                for (int i = 0; i < data_to_render.constraction_map.size(); i++) {
                    double newH = data_to_render.constraction_map.get(i).getA() * 0.98;
                    if (newMAXHeight < newH) newMAXHeight = newH;
                    data_to_render.constraction_map.get(i).setA(newH);
                }
                maxHeight = newMAXHeight;
            }

        }
    }

    public static void ParallelSort() {

        ArrayList<Integer> arr_index_lenghts = new ArrayList<Integer>();
        ArrayList<Integer> arr_index_heights = new ArrayList<Integer>();

        for (int i = 0; i < lengths.size(); i++) {
            arr_index_lenghts.add(i);
            arr_index_heights.add(i);
        }

        boolean isSorted = false;
        int point_key;
        double point_value;

        while (!isSorted) {
            isSorted = true;

            for (int i = 0; i < lengths.size() - 1; i++) {

                if (lengths.get(i) > lengths.get(i + 1)) {
                    isSorted = false;

                    point_key = arr_index_lenghts.get(i);
                    arr_index_lenghts.set(i, arr_index_lenghts.get(i + 1));
                    arr_index_lenghts.set(i + 1, point_key);

                    point_value = lengths.get(i);
                    lengths.set(i, lengths.get(i + 1));
                    lengths.set(i + 1, point_value);
                }

                if (heights.get(i) > heights.get(i + 1)) {
                    isSorted = false;

                    point_key = arr_index_heights.get(i);
                    arr_index_heights.set(i, arr_index_heights.get(i + 1));
                    arr_index_heights.set(i + 1, point_key);

                    point_value = heights.get(i);
                    heights.set(i, heights.get(i + 1));
                    heights.set(i + 1, point_value);
                }

            }

        }

        FittingData(arr_index_lenghts, arr_index_heights);
    }

    public static void PreparationForses() {

        for (int i = 0; i < data_to_calc.constraction_map.size() - 1; i++) {

            double left_F = data_to_calc.constraction_map.get(i).getFR();
            double right_F = data_to_calc.constraction_map.get(i + 1).getFL();

            if (left_F >= right_F) {
                left_F -= right_F;
                data_to_calc.constraction_map.get(i).setFR(left_F);
                data_to_calc.constraction_map.get(i + 1).setFL(0);
                data_to_render.constraction_map.get(i).setFR(left_F);
                data_to_render.constraction_map.get(i + 1).setFL(0);
            }
            else {
                right_F -= left_F;
                data_to_calc.constraction_map.get(i).setFR(0);
                data_to_calc.constraction_map.get(i + 1).setFL(right_F);
                data_to_render.constraction_map.get(i).setFR(0);
                data_to_render.constraction_map.get(i + 1).setFL(right_F);
            }
        }

        ParallelSort();
    }

    public static void drawTip(GraphicsContext ctx, int strokePoint, double x, double y, double len) {

        ctx.setLineWidth(1.);

        switch (strokePoint) {
            case (0):
                ctx.strokePolyline(new double[]{x, x + len}, new double[]{0, 0}, 2);
                ctx.strokePolyline(new double[]{x + len, x + 0.75 * len}, new double[]{0, 0.15 * len}, 2);
                ctx.strokePolyline(new double[]{x + len, x + 0.75 * len}, new double[]{0, -0.15 * len}, 2);
                break;
            case (1):
                ctx.strokePolyline(new double[]{x, x - len}, new double[]{0, 0}, 2);
                ctx.strokePolyline(new double[]{x - len, x - 0.75 * len}, new double[]{0, 0.15 * len}, 2);
                ctx.strokePolyline(new double[]{x - len, x - 0.75 * len}, new double[]{0, -0.15 * len}, 2);
                break;

            case (-1):
                ctx.setLineWidth(1.);
                ctx.strokePolyline(new double[]{x, x + len}, new double[]{y, y}, 2);
                ctx.strokePolyline(new double[]{x, x + 0.1 * len}, new double[]{y, y + 5}, 2);
                ctx.strokePolyline(new double[]{x, x + 0.1 * len}, new double[]{y, y - 5}, 2);
                ctx.strokePolyline(new double[]{x + len, x + 0.9 * len}, new double[]{y, y + 5}, 2);
                ctx.strokePolyline(new double[]{x + len, x + 0.9 * len}, new double[]{y, y - 5}, 2);
                break;

        }

    }

    public static void printArrowData(GraphicsContext ctx, double x, double y, int type, double data, int font_size) {

        ctx.save();

        Affine transform = ctx.getTransform();
        transform.appendScale(1, -1);
        ctx.setTransform(transform);
        ctx.setFont(Font.font("Times New Roman", FontWeight.BOLD, font_size));
        ctx.setTextAlign(TextAlignment.CENTER);

        switch (type) {
            case (1):
                ctx.setFill(Color.BLACK);
                if (data == (int) data) ctx.fillText(String.format("%sq", Math.abs((int) data)), x, y);
                else ctx.fillText(String.format("%sq", Math.abs(data)), x, y);
                break;
            case (2):
                ctx.setFill(Color.RED);
                if (data == (int) data) ctx.fillText(String.format("%sF", (int) data), x, y);
                else ctx.fillText(String.format("%sF", data), x, y);
                break;
            case (3):
                ctx.setFill(Color.BLACK);
                if (data == (int) data) ctx.fillText(String.format("%sL", (int) data), x, y);
                else ctx.fillText(String.format("%sL", data), x, y);
                break;
        }

        ctx.restore();
    }

    public static void printArrow(GraphicsContext ctx, int index, double x1, double x2, double y, Construction data, boolean processorPoint) {

        double Fx2 = x2;
        double Fx1 = x1;
        double rodLength = data.constraction_map.get(index).getL();
        double FG, FL, FR;

        if (processorPoint) {
            FG = GetSystem.data_to_calculations_construction.constraction_map.get(index).getFG();
            FL = GetSystem.data_to_calculations_construction.constraction_map.get(index).getFL();
            FR = GetSystem.data_to_calculations_construction.constraction_map.get(index).getFR();
        } else {
            FG = data_to_calc.constraction_map.get(index).getFG();
            FL = data_to_calc.constraction_map.get(index).getFL();
            FR = data_to_calc.constraction_map.get(index).getFR();
        }

        double indent, len;

        if (FG != 0) {

            len = rodLength * 0.18;
            indent = rodLength * 0.025;

            if (FG > 0) {
                for (int j = 0; j < 5; j++) {
                    drawTip(ctx, 0, x1, 0, len);
                    if (j == 2) printArrowData(ctx, x1 + len / 2, -y, 1, FG, 15);
                    x1 += (len + indent);
                }
            } else {
                for (int j = 0; j < 5; j++) {
                    drawTip(ctx, 1, x2, 0, len);
                    if (j == 2) printArrowData(ctx, x2 - len / 2, -y, 1, FG, 15);
                    x2 -= (len + indent);
                }
            }
        }

        if (FL != 0) {
            ctx.save();
            ctx.setStroke(Color.RED);
            if (FL > 0 || data.constraction_map.get(index).getSL() != 0) {
                drawTip(ctx, 0, Fx1, 0, rodLength * 0.3);
                printArrowData(ctx, Fx1 + rodLength * 0.3, -y, 2, FL, 15);
            } else {
                drawTip(ctx, 1, Fx1, 0, rodLength * 0.3);
                printArrowData(ctx, Fx1 - rodLength * 0.3, -y, 2, Math.abs(FL), 15);
            }
            ctx.restore();
        }

        if (FR != 0) {
            ctx.save();
            ctx.setStroke(Color.RED);
            if (FR > 0 || data.constraction_map.get(index).getSR() != 0) {
                drawTip(ctx, 1, Fx2, 0, rodLength * 0.3);
                printArrowData(ctx, Fx2 - rodLength * 0.3, -y, 2, FR, 15);
            } else {
                drawTip(ctx, 0, Fx2, 0, rodLength * 0.3);
                printArrowData(ctx, Fx2 + rodLength * 0.3, -y, 2, Math.abs(FR), 15);
            }
            ctx.restore();
        }

    }

    public static void drawSupport(GraphicsContext ctx, int type_support, double x, double underLenY) {

        ctx.save();
        ctx.setLineWidth(2.);
        ctx.strokePolyline(new double[]{x, x}, new double[]{-1 * underLenY, underLenY}, 2);

        double y = underLenY;
        double point = underLenY * 2 / 20;

        if (type_support == 1) {
            for (int i = 0; i < 20; i++) {
                ctx.strokePolyline(new double[]{x, x + point}, new double[]{y, y - point}, 2);
                y -= point;
            }
        } else {
            for (int i = 0; i < 20; i++) {
                ctx.strokePolyline(new double[]{x, x - point}, new double[]{y, y - point}, 2);
                y -= point;
            }
        }

        ctx.restore();

    }

    public static void printRodData(GraphicsContext ctx, int i, double x, double y, int font_size, boolean processorPoint) {

        ctx.save();

        Affine transform = ctx.getTransform();
        transform.appendScale(1, -1);
        ctx.setTransform(transform);
        ctx.setFont(Font.font("Times New Roman", FontWeight.BOLD, font_size));
        ctx.setTextAlign(TextAlignment.CENTER);

        double E, A;

        ctx.setFill(Color.BLUE);
        if (processorPoint) {
            E = GetSystem.data_to_calculations_construction.constraction_map.get(i).getE();
            A = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();
        }
        else {
            E = data_to_calc.constraction_map.get(i).getE();
            A = data_to_calc.constraction_map.get(i).getA();
        }
        ctx.fillText(String.format("%sE, %sA", E, A), x, y);
        ctx.restore();

    }

    public void draw() {

        GraphicsContext ctx = mainCanvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

        ctx.save();

        Affine transform = ctx.getTransform();
        transform.appendTranslation(mainCanvas.getWidth() / 2 - allLength / 2, mainCanvas.getHeight() / 2);
        transform.appendScale(1, -1);
        ctx.setTransform(transform);


        double startX = 0;

        for (int i = 0; i < data_to_render.constraction_map.size(); i++) {

            double x1 = startX;
            double x2 = x1 + data_to_render.constraction_map.get(i).getL();
            double y1 = data_to_render.constraction_map.get(i).getA() / 2;
            double y2 = -1 * data_to_render.constraction_map.get(i).getA() / 2;

            ctx.setLineWidth(2.);
            ctx.strokePolyline(new double[]{x1, x2, x2, x1, x1}, new double[]{y1, y1, y2, y2, y1}, 5);
            printRodData(ctx, i, x2 - (x2 - x1) * 0.5, -(y1 + 15), 15, false);

            startX += data_to_render.constraction_map.get(i).getL();

            printArrow(ctx, i, x1, x2, 15, data_to_render, false);

            if (underLenY > -1 * (maxHeight / 2 + 30)) underLenY = -1 * (maxHeight / 2 + 30);

            if (data_to_render.constraction_map.get(i).getSL() == 1) {
                drawSupport(ctx, 1, x1, underLenY);
            }
            if (data_to_render.constraction_map.get(i).getSR() == 1) {
                drawSupport(ctx, 2, x2, underLenY);
            }

            ctx.setLineWidth(1.);
            ctx.strokePolyline(new double[]{x1, x1}, new double[]{0, underLenY}, 2);
            ctx.strokePolyline(new double[]{x2, x2}, new double[]{0, underLenY}, 2);
            drawTip(ctx, -1, x1, underLenY, x2 - x1);
            double l = data_to_calc.constraction_map.get(i).getL();
            printArrowData(ctx, x2 - (x2 - x1) * 0.5, -(underLenY - 20), 3, l, 15);

        }

        ctx.restore();

    }

}