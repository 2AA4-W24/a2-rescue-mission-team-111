package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public interface POIFinder {
    JSONObject findCreeks(Compass direction, Position pos, int height, int width);
    String calculateClosest();
    void checkPOI(JSONObject extra, Position pos);
}
