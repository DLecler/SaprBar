package GraphicСalculations;

public class Rod {
    private int I;
    private double L;
    private double A;
    private double E;
    private double FG;
    private double FL;
    private double FR;
    private int SL;
    private int SR;

    public Rod(int I, double L, double A, double E, double FG, double FL, double FR, int SL, int SR) {

        this.I = I;
        this.L = L;
        this.A = A;
        this.E = E;
        this.FG = FG;
        this.FL = FL;
        this.FR = FR;
        this.SL = SL;
        this.SR = SR;

    }

    public int getI() {
        return I;
    }
    public double getL() {
        return L;
    }
    public double getA() {
        return A;
    }
    public double getE() {
        return E;
    }
    public double getFG() {
        return FG;
    }
    public double getFL() {
        return FL;
    }
    public double getFR() {
        return FR;
    }
    public int getSL() {
        return SL;
    }
    public int getSR() {
        return SR;
    }


    public void setI(int i) {
        I = i;
    }
    public void setL(double l) {
        L = l;
    }
    public void setA(double a) { A = a; }
    public void setE(double e) { E = e; }
    public void setFG(double fg) { FG = fg; }
    public void setFL(double fl) { FL = fl; }
    public void setFR(double fr) { FR = fr; }
    public void setSL(int sl) { SL = sl; }
    public void setSR(int sr) { SR = sr; }

}
