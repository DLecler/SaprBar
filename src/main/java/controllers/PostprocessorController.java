package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import GraphicСalculations.GetSystem;
import GraphicСalculations.ReceivingData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import сalculation.CalcMain;

public class PostprocessorController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Canvas epurCanvas;

    public void switchToSceneWorkPlace(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("workPlace.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToScenePostProcTableData(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("postProcTableData.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void initialize() {

        blankForEpur();

    }

    public static void printEpurData(GraphicsContext ctx, boolean up, boolean left, double x, double y, double Uj){

        ctx.save();

        Affine transform = ctx.getTransform();
        transform.appendScale(1, -1);
        ctx.setTransform(transform);

        ctx.setFont(Font.font("Times New Roman", 15));
        ctx.setTextAlign(TextAlignment.CENTER);

        Uj = Math.abs(Uj);

        if (left) {
            if (up) {
                ctx.setFill(Color.GREEN);
                ctx.fillText( Double.toString(Uj), x - 15, -(y));
            }
            else {
                ctx.setFill(Color.GREEN);
                ctx.fillText( Double.toString(Uj), x - 15, -(y));
            }
        }
        else{
            if (up) {
                ctx.setFill(Color.BLUE);
                ctx.fillText( Double.toString(Uj), x + 15, -(y));
            }
            else {
                ctx.setFill(Color.BLUE);
                ctx.fillText( Double.toString(Uj), x + 15, -(y));
            }
        }
        ctx.restore();

    }

    public static void culcEquations(GraphicsContext ctx, double zero_line, double delta, int tupeEquation){

        double x2 = ReceivingData.allLength_epur;

        ctx.strokePolyline(new double[]{0, x2}, new double[]{zero_line, zero_line}, 2);

        int n = GetSystem.data_to_calculations_construction.constraction_map.size();
        int point_Ux = 0;

        ctx.save();
        Affine transform = ctx.getTransform();
        transform.appendTranslation(0, zero_line);
        ctx.setTransform(transform);

        double L_done = 0;

        for (int i = 0; i < n; i++) {

            double L_epur = GetSystem.data_to_epur_construction.constraction_map.get(i).getL();
            double L_calc = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
            int hatch_point = 0;

            BigDecimal start = new BigDecimal("0.0");
            BigDecimal end = new BigDecimal(Double.toString(L_epur));
            BigDecimal step = new BigDecimal("0.1");

            for (BigDecimal J = start; J.compareTo(end) <= 0; J = J.add(step)) {

                double Ux = CalcMain.vector_U[point_Ux];
                double Ux_1 = CalcMain.vector_U[point_Ux + 1];
                double X = L_calc*(J.doubleValue()/L_epur);
                double x = L_done + J.doubleValue();

                double Uj;
                if (tupeEquation == 1) Uj = CalcMain.EquationNx(i, X, Ux_1, Ux);
                else if (tupeEquation == 2) Uj = CalcMain.EquationUx(i, X, Ux_1, Ux);
                else {
                    double A = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();
                    Uj = CalcMain.EquationNx(i, X, Ux_1, Ux) / A;
                }

                double y = (delta * Uj);

                if (J.doubleValue() == 0.0) {
                    if (Uj >= 0) {
                        if (Uj == Math.round(Uj*10.0)/10.0) printEpurData(ctx, true, false, x, y + 10, Uj);
                        else printEpurData(ctx, true, false, x+5, y + 10, Math.round(Uj*100.0)/100.0);
                    }
                    else {
                        if (Uj == Math.round(Uj*10.0)/10.0) printEpurData(ctx, false, false, x, y - 10, Uj);
                        else printEpurData(ctx, false, false, x+5, y - 10, Math.round(Uj*100.0)/100.0);
                    }
                }
                if (J.doubleValue() == Math.floor(L_epur*10.0)/10.0) {
                    if (Uj >= 0) {
                        if (Uj == Math.round(Uj*10.0)/10.0) printEpurData(ctx, true, true, x, y + 10, Uj);
                        else printEpurData(ctx, true, true, x-5, y + 10, Math.round(Uj*100.0)/100.0);
                    }
                    else {
                        if (Uj == Math.round(Uj*10.0)/10.0) printEpurData(ctx, false, true, x, y - 10, Uj);
                        else printEpurData(ctx, false, true, x-5, y - 10,  Math.round(Uj*100.0)/100.0);
                    }
                }

                ctx.setFill(Color.BLACK);
                ctx.fillOval(x, y,1,1);
                hatch_point ++;
                if (hatch_point == 50) {
                    hatch_point = 0;
                    ctx.strokePolyline(new double[]{x, x}, new double[]{y, 0}, 2);
                }
            }

            point_Ux += 2;
            L_done += L_epur;
        }
        ctx.restore();
    }

    public static void drawNxUxSIGMx(GraphicsContext ctx, double zero_lineNx, double zero_lineUx, double zero_lineSIGMx){

        ctx.save();

        Affine transform = ctx.getTransform();
        transform.appendScale(1, -1);
        ctx.setTransform(transform);
        ctx.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFill(Color.BLACK);

        ctx.fillText("Nx[ql]", -25, -zero_lineNx);
        ctx.fillText("Ux[ql^2/EA]", -45, -zero_lineUx);
        ctx.fillText("σx[ql/A]", -31, -zero_lineSIGMx);

        ctx.restore();
    }

    public void drawEpur(GraphicsContext ctx, double underLenY) {

        ctx.save();
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(1.);

        double x = 0;

        for (int i = 0; i < CalcMain.Lenghts.length; i++) {

            if (x == 0) ctx.strokePolyline(new double[]{x, x}, new double[]{underLenY, - 550}, 2);
            x += CalcMain.Lenghts[i];
            ctx.strokePolyline(new double[]{x, x}, new double[]{underLenY, - 550}, 2);

        }

        double sum_hmax_Nx = CalcMain.maxNx + Math.abs(CalcMain.negative_maxNx);
        double sum_hmax_Ux = CalcMain.maxUx + Math.abs(CalcMain.negative_maxUx);
        double sum_hmax_SIGMx = CalcMain.maxSIGMx + Math.abs(CalcMain.negative_maxSIGMx);

        double deltaNx = 106 / sum_hmax_Nx;
        double deltaUx = 106 / sum_hmax_Ux;
        double deltaSIGMx = 106 / sum_hmax_SIGMx;

        double zero_lineSIGMx = -544 - CalcMain.negative_maxSIGMx * deltaSIGMx;
        double zero_lineUx = zero_lineSIGMx + CalcMain.maxSIGMx * deltaSIGMx + 40 - CalcMain.negative_maxUx * deltaUx;
        double zero_lineNx = zero_lineUx + CalcMain.maxUx * deltaUx + 40 - CalcMain.negative_maxNx * deltaNx;

        drawNxUxSIGMx(ctx, zero_lineNx, zero_lineUx, zero_lineSIGMx);

        culcEquations(ctx, zero_lineNx, deltaNx, 1);
        culcEquations(ctx, zero_lineUx, deltaUx, 2);
        culcEquations(ctx, zero_lineSIGMx, deltaSIGMx, 3);

        ctx.restore();

    }

    public void blankForEpur()  {

        CalcMain.main();

        double zoom = 1;

        GraphicsContext ctx = epurCanvas.getGraphicsContext2D();

        double length_epur = ReceivingData.allLength_epur;

        ctx.setFill(Color.WHITE);
        ctx.fillRect(0, 0, epurCanvas.getWidth(), epurCanvas.getHeight());

        Affine transform = ctx.getTransform();

        transform.appendTranslation(50 + 0.5 * (1070 - length_epur),ReceivingData.changeMaxHeight_epur/2 + 40);
        transform.appendScale(zoom, -zoom);
        ctx.setTransform(transform);

        double startX = 0;
        boolean read_data = true;

        for (int i = 0; i < GetSystem.data_to_epur_construction.constraction_map.size(); i++) {

            double x1 = startX;
            double x2 = x1 + GetSystem.data_to_epur_construction.constraction_map.get(i).getL();
            double y1 = GetSystem.data_to_epur_construction.constraction_map.get(i).getA() / 2;
            double y2 = -1 * GetSystem.data_to_epur_construction.constraction_map.get(i).getA() / 2;

            ctx.setLineWidth(2.);
            ctx.strokePolyline(new double[]{x1, x2, x2, x1, x1}, new double[]{y1, y1, y2, y2, y1}, 5);
            DrawingController.printRodData(ctx, i, x2 - (x2 - x1) * 0.5, -(y1 + 15), 15, true);

            startX += GetSystem.data_to_epur_construction.constraction_map.get(i).getL();

            DrawingController.printArrow(ctx, i, x1, x2, 15, GetSystem.data_to_epur_construction, true);

            double underLenY = -1 * (ReceivingData.changeMaxHeight_epur/2 + 30);

            if (GetSystem.data_to_epur_construction.constraction_map.get(i).getSL() == 1) {
                DrawingController.drawSupport(ctx, 1, x1, underLenY);
            }
            if (GetSystem.data_to_epur_construction.constraction_map.get(i).getSR() == 1) {
                DrawingController.drawSupport(ctx, 2, x2, underLenY);
            }

            ctx.setLineWidth(1.);
            ctx.strokePolyline(new double[]{x1, x1}, new double[]{0, underLenY}, 2);
            ctx.strokePolyline(new double[]{x2, x2}, new double[]{0, underLenY}, 2);
            DrawingController.drawTip(ctx, -1, x1, underLenY, x2 - x1);
            double l = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
            DrawingController.printArrowData(ctx, x2 - (x2 - x1) * 0.5, -(underLenY + 10), 3, l, 15);

            if (read_data) PostProcTableDataController.lines = new ArrayList<>();
            drawEpur(ctx, underLenY);
            read_data = false;

        }
    }
}