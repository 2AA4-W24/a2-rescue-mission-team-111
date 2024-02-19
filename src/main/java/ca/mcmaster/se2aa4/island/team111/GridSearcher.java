package ca.mcmaster.se2aa4.island.team111;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher implements POIFinder {

    List<POI> creeks = new ArrayList<POI>();
    POI site;

    @Override
    public JSONObject findCreeks() {
        JSONObject decision = new JSONObject();
        decision.put("action", "stop");
        return decision;
    }

    private void set_site(int x, int y) {
        site = new POI("BLAH", new Position(x, y));
    }

    @Override
    public String calculateClosest() {
        set_site(-350,0);
        creeks.add(new POI("1creek", new Position(200, 300)));

        POI closest_creek = creeks.get(0);

        for (int i = 0; i<creeks.size(); i++) {
            POI this_creek = creeks.get(i);
            if (getDistance(this_creek) < getDistance(closest_creek)) {
                closest_creek = this_creek;
            }
        }

        return closest_creek.getID();
    }

    private double getDistance(POI poi) {
        int x = Math.abs(site.getXvalue()-poi.getXvalue());
        int y = Math.abs(site.getYvalue()-poi.getYvalue());
        double distance = Math.sqrt((x*x) + (y*y));
        return distance;
    }

    @Override
    public void checkPOI(JSONObject extra, Position pos) {
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
