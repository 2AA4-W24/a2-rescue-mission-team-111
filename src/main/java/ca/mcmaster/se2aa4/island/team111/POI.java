package ca.mcmaster.se2aa4.island.team111;

public class POI {

    private String ID;
    private Position pos;

    public POI(String ID, Position pos) {
        this.ID = ID;
        this.pos = pos;
    }

    public String getID() {
        return ID;
    }

    public int getXvalue() {
        return pos.getX();
    }

    public int getYvalue() {
        return pos.getY();
    }
}
