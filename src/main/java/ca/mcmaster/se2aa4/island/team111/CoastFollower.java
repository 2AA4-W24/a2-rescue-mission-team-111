package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class CoastFollower implements POIFinder {
    
    @Override
    public JSONObject findCreeks(Compass direction, Position pos, Information I) {
        JSONObject decision = new JSONObject();

        return decision;
    }

    @Override
    public String calculateClosest() {
        return "";
    }

    @Override
    public void checkPOI(JSONObject extra, Position pos) {
    
    }
}
