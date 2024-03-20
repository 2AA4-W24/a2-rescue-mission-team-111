package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.json.JSONObject;

public class GridSearcher {

    private GridSearchState currentState = new ScanningState();
    private Information currentInfo;
    private Position currentPos;

    public List<POI> creeks = new ArrayList<POI>(); //temporarily public for testing
    public POI site = new POI("NULL", new Position(0, 0));

    private Compass initial_dir;
    private Compass dir_before_turn;

    private int range = 0;
    private int ground_range = 0;

    public GridSearcher(Compass direction) {
        this.initial_dir = direction;
        this.currentState = new ScanningState();
    }

    public void updateInfo(Information I, Position pos) {
        this.currentInfo = I;
        this.currentPos = pos;
    }

    public Information getCurrentInfo() {
        return currentInfo;
    }

    public int getGroundRange() {
        return ground_range;
    }

    public void setGroundRange(int range) {
        ground_range = range;
    }

    public void decrementGroundRange() {
        ground_range--;
    }

    public void addCreek(String id) {
        POI newPOI = new POI(id, currentPos);
        creeks.add(newPOI);
    }

    public void addSite(String id) {
        site = new POI(id, currentPos);
    }


    public void setState(GridSearchState gState) {
        currentState = gState;
    }


    public Compass getDirBeforeTurn() {
        return dir_before_turn;
    }

    public Compass getInitialDir() {
        return initial_dir;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int new_range) {
        this.range = new_range;
    }

    public void toggleDirBeforeTurn() {
        dir_before_turn = dir_before_turn.opposite();
    }

    public void setDirBeforeTurn(Compass dir) {
        dir_before_turn = dir;
    }


    public JSONObject performSearch() {
        return currentState.handle(this);
    }

    public String calculateClosest() {

        if (creeks.isEmpty()) {
            return "No creeks found";
        } else if (site.getID().equals("null")) {
            POI lastCreek = creeks.get(creeks.size()-1);
            return lastCreek.getID();
        }

        POI closest_creek = creeks.get(0);
        for (int i = 1; i<creeks.size(); i++) {
            POI this_creek = creeks.get(i);
            if (getDistance(this_creek) < getDistance(closest_creek)) {
                closest_creek = this_creek;
            }
        }

        return closest_creek.getID();
    }

    public double getDistanceTest(POI creek){
        return getDistance(creek);
    }

    //Uses pythagorean mathematics to check distance
    private double getDistance(POI creek) {
        double x = Math.abs(site.getXvalue()-creek.getXvalue());
        double y = Math.abs(site.getYvalue()-creek.getYvalue());
        return Math.sqrt((x*x) + (y*y));
    }

}
