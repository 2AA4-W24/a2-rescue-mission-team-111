package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

//Interface of finding Points of Interest implemented by grid-searching class and class to follow the coast
public interface POIFinder {
    JSONObject findCreeks(Compass direction, Position pos, Information I);
    String calculateClosest();
    void checkPOI(JSONObject extra, Position pos);
}
