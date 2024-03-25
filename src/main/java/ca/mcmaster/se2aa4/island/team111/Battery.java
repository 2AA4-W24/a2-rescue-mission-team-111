package ca.mcmaster.se2aa4.island.team111;

public class Battery {
    private int charge;

    public Battery(int charge) {
        this.charge = charge;
    }

    public void depleteCharge(int cost) {
        charge -= cost;
    }

    public boolean isLow(Position pos) {
        double x = pos.getX();
        double y = pos.getY();
        double minCharge = (Math.sqrt((x*x)+(y*y))*0.2) + 20; //Equation derived from multiple tests with "stop" command on different maps
        return charge <= minCharge;
    }

    public int getCharge() {
        return charge;
    }
}