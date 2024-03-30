package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import GraphicСalculations.GetSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import сalculation.CalcMain;

public class PostProcTableDataController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane scr_pane, scr_pane_2;

    @FXML
    private Text txt_pane, txt_pane_2;

    @FXML
    private Button getButton;

    @FXML
    private TextField n_rod, x_rod;


    public void switchToScenePostprocessor(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("postprocessor.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static ArrayList<String> lines = new ArrayList<>();

    @FXML
    void initialize() {

        lines.clear();
        calcTableData();


        scr_pane.setContent(txt_pane);
        scr_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            sb.append(line);
            sb.append("\n");
        }

        txt_pane.setText(sb.toString());
        txt_pane.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        getButton.setOnAction(event -> {
            int number_Rod = Integer.parseInt(n_rod.getText());
            double X_Rod = Double.parseDouble(x_rod.getText());
            double Ux = CalcMain.vector_U[number_Rod*2-2];
            double Ux_1 = CalcMain.vector_U[number_Rod*2-1];
            double NX = CalcMain.EquationNx(number_Rod - 1, X_Rod, Ux_1, Ux);
            double UX = CalcMain.EquationUx(number_Rod - 1, X_Rod, Ux_1, Ux);
            double Ap = GetSystem.data_to_calculations_construction.constraction_map.get(number_Rod-1).getA();

            scr_pane_2.setContent(txt_pane_2);
            scr_pane_2.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

            txt_pane_2.setText("\n Rod (" + number_Rod + ")\n" +
                    "\tNx(" + X_Rod + ") = " + NX + "\n" +
                    "\tUx(" + X_Rod + ") = " + UX + "\n" +
                    "\tσx(" + X_Rod + ") = " + NX / Ap);
            txt_pane_2.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        });
    }

    public static void calcTableData() {

        PostProcTableDataController.lines.clear();

        int point_Ux = 0;
        double maxNx, minNx, maxUx, minUx, maxNx_x, minNx_x, maxUx_x, minUx_x;

        ArrayList<Double> arrNodes = new ArrayList<>();

        for (int i = 0; i < GetSystem.data_to_calculations_construction.constraction_map.size(); i++) {

            maxNx = 0;
            minNx = 0;
            maxUx = 0;
            minUx = 0;
            maxNx_x = 0;
            minNx_x = 0;
            maxUx_x = 0;
            minUx_x = 0;

            double lenRod = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();

            arrNodes.clear();

            BigDecimal start = new BigDecimal("0.0");
            BigDecimal end = new BigDecimal(Double.toString(lenRod));
            BigDecimal step = new BigDecimal("0.01");

            for (BigDecimal J = start; J.compareTo(end) <= 0; J = J.add(step)) {
                double Ux = CalcMain.vector_U[point_Ux];
                double Ux_1 = CalcMain.vector_U[point_Ux + 1];

                if (J.doubleValue() == 0.0 || J.doubleValue() == lenRod) {

                    arrNodes.add(CalcMain.EquationNx(i, J.doubleValue(), Ux_1, Ux));
                    arrNodes.add(CalcMain.EquationUx(i, J.doubleValue(), Ux_1, Ux));
                }

                if (maxNx <= CalcMain.EquationNx(i, J.doubleValue(), Ux_1, Ux)) {
                    maxNx = CalcMain.EquationNx(i, J.doubleValue(), Ux_1, Ux);
                    maxNx_x = J.doubleValue();
                }
                if (minNx >= CalcMain.EquationNx(i, J.doubleValue(), Ux_1, Ux)) {
                    minNx = CalcMain.EquationNx(i, J.doubleValue(), Ux_1, Ux);
                    minNx_x = J.doubleValue();
                }
                if (maxUx <= CalcMain.EquationUx(i, J.doubleValue(), Ux_1, Ux)) {
                    maxUx = CalcMain.EquationUx(i, J.doubleValue(), Ux_1, Ux);
                    maxUx_x = J.doubleValue();
                }
                if (minUx >= CalcMain.EquationUx(i, J.doubleValue(), Ux_1, Ux)) {
                    minUx = CalcMain.EquationUx(i, J.doubleValue(), Ux_1, Ux);
                    minUx_x = J.doubleValue();
                }

            }

            double Ux = CalcMain.vector_U[point_Ux];
            double Ux_1 = CalcMain.vector_U[point_Ux + 1];
            double Ap = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();

            int I = i+1;
            PostProcTableDataController.lines.add(
                    " Rod (" + I + ")\n" +
                            "\tNx(" + 0.0 + ") = " + arrNodes.get(0) + "\n" +
                            "\tUx(" + 0.0 + ") = " + arrNodes.get(1) + "\n" +
                            "\tσx(" + 0.0 + ") = " + arrNodes.get(0) / Ap + "\n");

            if (minNx != 0 && minNx_x != lenRod && minNx_x != 0.0)
                PostProcTableDataController.lines.add("\tminNx(" + minNx_x + ") = " + CalcMain.EquationNx(i, minNx_x, Ux_1, Ux) + "\n");
            if (maxNx != 0 && maxNx_x != lenRod && maxNx_x != 0.0)
                PostProcTableDataController.lines.add("\tmaxNx(" + maxNx_x + ") = " + CalcMain.EquationNx(i, maxNx_x, Ux_1, Ux) + "\n");
            if (minUx != 0 && minUx_x != lenRod && minUx_x != 0.0)
                PostProcTableDataController.lines.add("\tminUx(" + minUx_x + ") = " + CalcMain.EquationUx(i, minUx_x, Ux_1, Ux) + "\n");
            if (maxUx != 0 && maxUx_x != lenRod && maxUx_x != 0.0)
                PostProcTableDataController.lines.add("\tmaxUx(" + maxUx_x + ") = " + CalcMain.EquationUx(i, maxUx_x, Ux_1, Ux) + "\n");

            PostProcTableDataController.lines.add(
                    "\tNx(" + lenRod + ") = " + arrNodes.get(2) + "\n" +
                    "\tUx(" + lenRod + ") = " + arrNodes.get(3) + "\n" +
                    "\tσx(" + lenRod + ") = " + arrNodes.get(2) / Ap + "\n");

            point_Ux += 2;

        }

    }

}
