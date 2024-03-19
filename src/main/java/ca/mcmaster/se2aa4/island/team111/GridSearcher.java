package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher {

    private GridSearchState currentState = new FlyingState();
    private Information currentInfo;

    public List<POI> creeks = new ArrayList<POI>(); //temporarily public for testing
    public POI site = new POI("NULL", new Position(26, -26));

    private Compass initial_dir;
    private Compass dir_before_turn = Compass.SOUTH;

    private int range = 0;
    private int maxRange = -1;

    public GridSearcher(Compass direction) {
        this.initial_dir = direction;
        this.currentState = new ScanningState();
    }

    public int getMaxRange() {
        return maxRange;
    } 

    public void decrementMaxRange() {
        maxRange--;
    }

    public void setMaxRange(int range) {
        maxRange = range;
    }

    public void updateInfo(Information I) {
        this.currentInfo = I;
    }

    public Information getCurrentInfo() {
        return currentInfo;
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


    public JSONObject findCreeks(Compass direction, Position pos, Information I) {

        JSONObject decision = new JSONObject();
        
        decision = currentState.handle(this);

        return decision;
    }

    public String calculateClosest() {

        POI closest_creek = creeks.get(0);

        //Closest creek is the creek with closest distance to the site
        for (int i = 1; i<creeks.size(); i++) {
            POI this_creek = creeks.get(i);
            if (getDistance(this_creek) < getDistance(closest_creek)) {
                closest_creek = this_creek;
            }
        }

        return closest_creek.getID();
    }

    //Uses pythagorean mathematics to check distance
    private double getDistance(POI creek) {
        int x = Math.abs(site.getXvalue()-creek.getXvalue());
        int y = Math.abs(site.getYvalue()-creek.getYvalue());
        double distance = Math.sqrt((x*x) + (y*y));
        return distance;
    }

    public void checkPOI(JSONObject extra, Position pos) {
        //if the result of a scan has creeks in it, add them to the list. Same with sites.
        if (extra.has("creeks")) {
            JSONArray c = extra.getJSONArray("creeks");
            if (!c.isEmpty()) {
                for (int i = 0; i<c.length(); i++) {
                    String ID = c.getString(i);
                    POI newPOI = new POI(ID, pos);
                    creeks.add(newPOI);
                }
            }
        }
        if (extra.has("sites")) {
            JSONArray sites = extra.getJSONArray("sites");
            if (!sites.isEmpty()) {
                for (int i = 0; i<sites.length(); i++) {
                    String ID = sites.getString(i);
                    site = new POI(ID, pos);
                }
            }
        }
    }

}
