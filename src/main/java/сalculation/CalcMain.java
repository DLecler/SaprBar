package сalculation;

        import GraphicСalculations.GetSystem;

        import java.math.BigDecimal;
        import java.math.RoundingMode;

public class CalcMain {

    public static double[][] matrix_A;
    public static double[] vector_B, vector_Delta, vector_U, Lenghts;
    public static double all_Lenght_epur = 0, negative_maxNx = 0, negative_maxUx = 0, negative_maxSIGMx = 0, maxNx = 0, maxUx = 0, maxSIGMx = 0;


    public static void culcMatrA(){

        final int n = GetSystem.data_to_calculations_construction.constraction_map.size() + 1;

        matrix_A = new double[n][n];

        double[] data_for_A = new double[n-1];

        for (int i = 0; i < data_for_A.length; i++) {
            double E = GetSystem.data_to_calculations_construction.constraction_map.get(i).getE();
            double A = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();
            double L = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
            data_for_A[i] = E*A/L;
        }


        int point_start = 0, point_end = 1, id_data_for_A = 0;

        int SL = GetSystem.data_to_calculations_construction.constraction_map.get(0).getSL();
        int SR = GetSystem.data_to_calculations_construction.constraction_map.get(n-2).getSR();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (j >= point_start && j <= point_end) {

                    matrix_A[i][j] = data_for_A[id_data_for_A];

                    if (i == j && i != 0 && j != n - 1 && i != n - 1) {
                        id_data_for_A++;
                        matrix_A[i][j] += data_for_A[id_data_for_A];
                    }

                }

                if ((i%2 == 0 && j%2 == 1) || (i%2 == 1 && j%2 == 0)) matrix_A[i][j] *= -1.0;
                if (i == 0 && j == 0 && SL == 1) matrix_A[0][0] = 1.0;
                if (i == n-1 && j == n-1 && SR == 1) matrix_A[i][j] = 1.0;
            }

            if (i != 0) point_start++;
            if (point_end != n - 1) point_end++;

        }

        if(SL != 0) {
            matrix_A[0][1] = 0.0;
            matrix_A[1][0] = 0.0;
        }
        if (SR != 0) {
            matrix_A[n - 1][n - 2] = 0.0;
            matrix_A[n - 2][n - 1] = 0.0;
        }

    }

    public static void copyArr(double[][] a, double[][] b){
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) b[i][j] = a[i][j];
        }
    }

    public static void culcVectorB(){

        final int n = GetSystem.data_to_calculations_construction.constraction_map.size() + 1;
        vector_B = new double[n];

        double SL = GetSystem.data_to_calculations_construction.constraction_map.get(0).getSL();
        double SR = GetSystem.data_to_calculations_construction.constraction_map.get(n-2).getSR();

        for (int i = 0; i < n; i++) {

            if (i == 0 || i == n-1){

                int ind = i;

                if (i == n-1) ind -= 1;

                if ((SL == 0 && i == 0) || (SR == 0 && i == n-1)){
                    double F_longitudinal = GetSystem.data_to_calculations_construction.constraction_map.get(ind).getFG();
                    double F_extern_l = GetSystem.data_to_calculations_construction.constraction_map.get(ind).getFL();
                    double F_extern_r = GetSystem.data_to_calculations_construction.constraction_map.get(ind).getFR();
                    double L = GetSystem.data_to_calculations_construction.constraction_map.get(ind).getL();

                    vector_B[i] = (0.5 * F_longitudinal * L) + (F_extern_l - F_extern_r);
                }
                else{
                    if ((SL != 0 && i == 0) || (SR != 0 && i == n-1)) vector_B[i] = 0;
                }

            }

            else {
                double F_longitudinal_l = GetSystem.data_to_calculations_construction.constraction_map.get(i-1).getFG();
                double F_extern_R_l = GetSystem.data_to_calculations_construction.constraction_map.get(i-1).getFR();
                double L_l = GetSystem.data_to_calculations_construction.constraction_map.get(i-1).getL();

                double F_longitudinal_r = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFG();
                double F_extern_L_r = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFL();
                double L_r = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();

                vector_B[i] = (0.5 * F_longitudinal_l * L_l) - F_extern_R_l + (0.5 * F_longitudinal_r * L_r) + F_extern_L_r;
            }
        }

    }


    public static double[] copyVectB(){
        double[] new_VectB = new double[vector_B.length];

        for (int i = 0; i < vector_B.length; i++) {
            new_VectB[i] = vector_B[i];
        }
        return new_VectB;
    }

    public static void culcVectorDelta() {

        double[][] copuMatr_A = new double[matrix_A.length][matrix_A.length];
        copyArr(matrix_A, copuMatr_A);
        final int n = GetSystem.data_to_calculations_construction.constraction_map.size() + 1;
        vector_Delta = new double[n];

        BigDecimal[][] mA = new BigDecimal[matrix_A.length][matrix_A.length];

        for (int i = 0; i < matrix_A.length; i++) {
            for (int j = 0; j < matrix_A[i].length; j++) {
                mA[i][j] = new BigDecimal(Double.toString(matrix_A[i][j]));
            }
        }

        BigDecimal[] vB = new BigDecimal[vector_B.length];

        for (int i = 0; i < matrix_A.length; i++) {
            vB[i] = new BigDecimal(Double.toString(vector_B[i]));
        }

        vector_Delta = solve(mA, vB);

    }

    public static double[] solve(BigDecimal[][] coefficients, BigDecimal[] constants) {
        int n = constants.length;
        for (int p = 0; p < n; p++) {
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (coefficients[i][p].abs().compareTo(coefficients[max][p].abs()) > 0) {
                    max = i;
                }
            }
            BigDecimal[] temp = coefficients[p];
            coefficients[p] = coefficients[max];
            coefficients[max] = temp;
            BigDecimal t = constants[p];
            constants[p] = constants[max];
            constants[max] = t;
            for (int i = p + 1; i < n; i++) {
                BigDecimal alpha = coefficients[i][p].divide(coefficients[p][p], 20, RoundingMode.HALF_UP);
                constants[i] = constants[i].subtract(alpha.multiply(constants[p]));
                for (int j = p; j < n; j++) {
                    coefficients[i][j] = coefficients[i][j].subtract(alpha.multiply(coefficients[p][j]));
                }
            }
        }
        BigDecimal[] solution = new BigDecimal[n];
        for (int i = n - 1; i >= 0; i--) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = i + 1; j < n; j++) {
                sum = sum.add(coefficients[i][j].multiply(solution[j]));
            }
            solution[i] = constants[i].subtract(sum).divide(coefficients[i][i], 20, RoundingMode.HALF_UP);
        }

        double[] double_solution = new double[solution.length];

        for (int i = 0; i < solution.length; i++) double_solution[i] = solution[i].doubleValue();


        return double_solution;
    }

    public static void culcVectorU(){

        final int n = GetSystem.data_to_calculations_construction.constraction_map.size() * 2;
        vector_U = new double[n];

        int point = 0, count = 2, ind_U = 0;

        while (ind_U != n){
            if (ind_U == 0 || ind_U == n-1) {
                vector_U[ind_U] = vector_Delta[point];
                point ++;
            }
            else {
                vector_U[ind_U] = vector_Delta[point];
                ind_U ++;
                vector_U[ind_U] = vector_Delta[point];
                point ++;
            }
            ind_U ++;
        }

    }

    public static double EquationNx(int i, double x, double Upl, double Up0){

        double E = GetSystem.data_to_calculations_construction.constraction_map.get(i).getE();
        double A = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();
        double L = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
        double q = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFG();
        return E*A/L * (Upl-Up0) + q*L/2 * (1-2*x/L);
    }

    public static double EquationUx(int i, double x, double Upl, double Up0){

        double E = GetSystem.data_to_calculations_construction.constraction_map.get(i).getE();
        double A = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();
        double L = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
        double q = GetSystem.data_to_calculations_construction.constraction_map.get(i).getFG();
        return (Up0 + (q*L*x)/(2*E*A))*(1-x/L) + Upl*x/L;
    }

    public static void main() {

        all_Lenght_epur = 0;
        negative_maxNx = 0;
        negative_maxUx = 0;
        negative_maxSIGMx = 0;
        maxNx = 0;
        maxUx = 0;
        maxSIGMx = 0;

        culcMatrA();
        culcVectorB();
        culcVectorDelta();
        culcVectorU();

        int point_lenghts = 0, point_Ux = 0;

        Lenghts = new double[GetSystem.data_to_epur_construction.constraction_map.size()];

        for (int i = 0; i < GetSystem.data_to_epur_construction.constraction_map.size(); i++) {

            Lenghts[point_lenghts] = GetSystem.data_to_epur_construction.constraction_map.get(i).getL();
            all_Lenght_epur += GetSystem.data_to_epur_construction.constraction_map.get(i).getL();
            point_lenghts ++;


            double L_rod = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
            double L_calc = GetSystem.data_to_calculations_construction.constraction_map.get(i).getL();
            double A_calc = GetSystem.data_to_calculations_construction.constraction_map.get(i).getA();

            for (double j = 0.0; j < L_rod + 0.1; j += 0.1) {
                double count = 1000;
                double X = L_calc * (j / L_rod);
                double Ux = CalcMain.vector_U[point_Ux];
                double Ux_1 = CalcMain.vector_U[point_Ux + 1];

                double Uj_Nx = Math.round(CalcMain.EquationNx(i, X, Ux_1, Ux) * count) / count;
                double Uj_Ux = Math.round(CalcMain.EquationUx(i, X, Ux_1, Ux) * count) / count;
                double Uj_SIGMx = Math.round(Uj_Nx/A_calc * count) / count;

                if (negative_maxNx > Uj_Nx) negative_maxNx = Uj_Nx;
                if (maxNx < Uj_Nx) maxNx = Uj_Nx;
                if (negative_maxUx > Uj_Ux) negative_maxUx = Uj_Ux;
                if (maxUx < Uj_Ux) maxUx = Uj_Ux;
                if (negative_maxSIGMx > Uj_SIGMx) negative_maxSIGMx = Uj_SIGMx;
                if (maxSIGMx < Uj_SIGMx) maxSIGMx = Uj_SIGMx;

            }

            point_Ux += 2;

        }

    }

}