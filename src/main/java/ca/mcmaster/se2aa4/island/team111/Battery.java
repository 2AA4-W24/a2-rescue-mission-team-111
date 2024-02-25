package ca.mcmaster.se2aa4.island.team111;

public class Battery {
    private int charge;

    public Battery(int charge) {
        this.charge = charge;
    }

    public void depleteCharge(int cost) {
        charge -= cost;
    }

    public boolean isLow() {
        return charge < 30; //25 magic number, deal with it later
    }
}