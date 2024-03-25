package ca.mcmaster.se2aa4.island.team111;

//For way of storing creeks and sites tied to their positions
//Stands for Point Of Interest
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
