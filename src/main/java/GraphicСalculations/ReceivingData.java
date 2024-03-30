package GraphicСalculations;

import сalculation.CalcMain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReceivingData {

    public static String filePath;

    public static ArrayList<Double> lengths = new ArrayList<Double>();
    public static ArrayList<Double> heights = new ArrayList<Double>();

    public static double maxLength = 0, maxHeight = 0;
    public static double allLength_epur = 0, changeMaxHeight_epur = 0;

    public static void CopyData(){
        for (int i = 0; i < GetSystem.data_to_calculations_construction.constraction_map.size(); i++) {

            double L = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
            double A = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();
            double E = GetSystem.data_to_calculations_construction.constraction_map.get(i).getE();
            double FG = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFG();
            double FL = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFL();
            double FR = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFR();
            int SL = GetSystem.data_to_calculations_construction.constraction_map.get(i).getSL();
            int SR = GetSystem.data_to_calculations_construction.constraction_map.get(i).getSR();

            GetSystem.data_to_epur_construction.constraction_map.put(i, new Rod(i+1, L, A, E, FG, FL, FR, SL, SR));
        }
    }

    public static void FittingData(ArrayList<Integer> arr_index_lengths, ArrayList<Integer> arr_index_heights){

        double k = 0.95;
        double point_len = maxLength;
        double point_h = maxHeight;

        allLength_epur = 0;
        changeMaxHeight_epur = 0;

        for (int i = arr_index_lengths.size() - 1; i >= 0; i--) {
            double l = GetSystem.data_to_epur_construction.constraction_map.get(arr_index_lengths.get(i)).getL();
            if (l < point_len) {
                GetSystem.data_to_epur_construction.constraction_map.get(arr_index_lengths.get(i)).setL(point_len*k);
                point_len *= k;
            }
            else GetSystem.data_to_epur_construction.constraction_map.get(arr_index_lengths.get(i)).setL(point_len);
            allLength_epur += point_len;

        }
        for (int i = arr_index_heights.size() - 1; i >= 0; i--) {
            double h = GetSystem.data_to_epur_construction.constraction_map.get(arr_index_heights.get(i)).getA();
            if (h < point_h) {
                GetSystem.data_to_epur_construction.constraction_map.get(arr_index_heights.get(i)).setA(point_h*k);
                point_h *= k;
            }
            else GetSystem.data_to_epur_construction.constraction_map.get(arr_index_heights.get(i)).setA(point_h);
            if (changeMaxHeight_epur < point_len) changeMaxHeight_epur = point_len;
        }

        int lenSize = 100 * GetSystem.data_to_epur_construction.constraction_map.size();
        if (lenSize < 500) lenSize = 500;

        if (allLength_epur < lenSize){
            while (allLength_epur < lenSize){

                double newAllLength = 0;

                for (int i = 0; i < GetSystem.data_to_epur_construction.constraction_map.size(); i++) {
                    double newL = GetSystem.data_to_epur_construction.constraction_map.get(i).getL() * 1.05;
                    GetSystem.data_to_epur_construction.constraction_map.get(i).setL(newL);
                    newAllLength += newL;
                }

                allLength_epur = newAllLength;
            }
        }

        if (allLength_epur > lenSize){
            while (allLength_epur > lenSize){

                double newAllLength = 0;

                for (int i = 0; i < GetSystem.data_to_epur_construction.constraction_map.size(); i++) {
                    double newL = GetSystem.data_to_epur_construction.constraction_map.get(i).getL() * 0.95;
                    GetSystem.data_to_epur_construction.constraction_map.get(i).setL(newL);
                    newAllLength += newL;
                }

                allLength_epur = newAllLength;
            }
        }

        if (changeMaxHeight_epur < 150){

            while (changeMaxHeight_epur < 150) {

                double newchangeMaxHeight = 0;

                for (int i = 0; i < GetSystem.data_to_epur_construction.constraction_map.size(); i++) {
                    double newH = GetSystem.data_to_epur_construction.constraction_map.get(i).getA() * 1.05;

                    GetSystem.data_to_epur_construction.constraction_map.get(i).setA(newH);

                    if (newchangeMaxHeight < newH) newchangeMaxHeight = newH;
                }

                changeMaxHeight_epur = newchangeMaxHeight;
            }

        }

        if (changeMaxHeight_epur >= 150) {

            while (changeMaxHeight_epur >= 150) {

                double newchangeMaxHeight = 0;

                for (int i = 0; i < GetSystem.data_to_epur_construction.constraction_map.size(); i++) {
                    double newH = GetSystem.data_to_epur_construction.constraction_map.get(i).getA() * 0.95;

                    GetSystem.data_to_epur_construction.constraction_map.get(i).setA(newH);

                    if (newchangeMaxHeight < newH) newchangeMaxHeight = newH;
                }

                changeMaxHeight_epur = newchangeMaxHeight;
            }

        }

    }

    public static void ParallelSort(){

        ArrayList<Integer> arr_index_lenghts = new ArrayList<Integer>();
        ArrayList<Integer> arr_index_heights = new ArrayList<Integer>();

        for (int i = 0; i < lengths.size(); i++) {
            arr_index_lenghts.add(i);
            arr_index_heights.add(i);
        }

        boolean isSorted = false;
        int point_key;
        double point_value;

        while(!isSorted){
            isSorted = true;

            for (int i = 0; i < lengths.size() - 1; i++) {

                if (lengths.get(i)>lengths.get(i+1)){
                    isSorted = false;

                    point_key = arr_index_lenghts.get(i);
                    arr_index_lenghts.set(i, arr_index_lenghts.get(i+1));
                    arr_index_lenghts.set(i+1, point_key);

                    point_value = lengths.get(i);
                    lengths.set(i, lengths.get(i+1));
                    lengths.set(i+1, point_value);
                }

                if (heights.get(i)>heights.get(i+1)){
                    isSorted = false;

                    point_key = arr_index_heights.get(i);
                    arr_index_heights.set(i, arr_index_heights.get(i+1));
                    arr_index_heights.set(i+1, point_key);

                    point_value = heights.get(i);
                    heights.set(i, heights.get(i+1));
                    heights.set(i+1, point_value);
                }

            }

        }

        FittingData(arr_index_lenghts, arr_index_heights);
    }
    public static void PreparationForses() {

        for (int i = 0; i < GetSystem.data_to_calculations_construction.constraction_map.size() - 1; i++) {


            double left_F = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFR();
            double right_F = GetSystem.data_to_calculations_construction.constraction_map.get(i+1).getFL();

            if (left_F >= right_F){
                left_F -= right_F;
                GetSystem.data_to_calculations_construction.constraction_map.get(i).setFR(left_F);
                GetSystem.data_to_calculations_construction.constraction_map.get(i+1).setFL(0);
            }
            else {
                right_F -= left_F;
                GetSystem.data_to_calculations_construction.constraction_map.get(i).setFR(0);
                GetSystem.data_to_calculations_construction.constraction_map.get(i+1).setFL(right_F);
            }
        }

        CopyData();
        ParallelSort();
    }

    public static void EnterData() {
        lengths = new ArrayList<Double>();
        heights = new ArrayList<Double>();

        maxLength = 0;
        maxHeight = 0;
        allLength_epur = 0;
        changeMaxHeight_epur = 0;

        GetSystem.data_to_epur_construction = new Construction();
        GetSystem.data_to_calculations_construction = new Construction();

        int index = 0;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while(line != null){

                String[] strings = line.split(", ");
                int i = Integer.parseInt(strings[0].trim());
                double Li = Double.parseDouble(strings[1].trim());
                double Ai = Double.parseDouble(strings[2].trim());
                double E = Double.parseDouble(strings[3].trim());
                double F_g = Double.parseDouble(strings[4].trim());
                double Fl = Double.parseDouble(strings[5].trim());
                double Fr = Double.parseDouble(strings[6].trim());
                int lS = Integer.parseInt(strings[7].trim());
                int rS = Integer.parseInt(strings[8].trim());

                if (lS == 1) Fl = 0;
                if (rS == 1) Fr = 0;

                GetSystem.data_to_calculations_construction.constraction_map.put(index, new Rod(i, Li, Ai, E, F_g, Fl, Fr, lS, rS));

                lengths.add(Li);
                heights.add(Ai);

                if (maxLength < Li) maxLength = Li;
                if (maxHeight < Ai) maxHeight = Ai;

                index++;
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        PreparationForses();

    }

}