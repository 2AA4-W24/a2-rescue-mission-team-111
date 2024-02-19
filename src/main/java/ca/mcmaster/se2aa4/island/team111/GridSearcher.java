package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher implements POIFinder {

    private final Logger logger = LogManager.getLogger();

    private List<POI> creeks = new ArrayList<POI>();
    private POI site;

    private Compass initial_dir;
    private Compass dir_before_turn;

    private boolean scanning = true;
    private boolean flying = true;
    private boolean turning = false;

    private boolean half_turn = true;
    private boolean full_turn = false;

    private boolean ocean_only = false;

    private int moves = 0;

    public GridSearcher(Compass direction) {
        this.initial_dir = direction;
    }

    @Override
    public JSONObject findCreeks(Compass direction, Position pos, int height, int width) {
        JSONObject decision = new JSONObject();

        decision = firstSearch(direction, pos, height, width);
        
        return decision;
    }

    private JSONObject firstSearch(Compass direction, Position pos, int height, int width) {
        JSONObject decision = new JSONObject();
        logger.info("CURRENT DIRECTION: " + direction);
        if (moves < 800) {
            logger.info("Moves " + moves);
            if (checkEdges(pos, height, width)) {
                decision = makeTurns(direction);
            } else {
                if (scanning) {
                    decision.put("action", "scan");
                    moves++;
                    scanning = false;
                } else {
                    decision.put("action", "fly");
                    moves++;
                    scanning = true;
                }
            }
        } else {
            decision.put("action", "stop");
        }
        return decision;
    }
    
    private JSONObject makeTurns(Compass direction) {
        JSONObject decision = new JSONObject();

        if (half_turn) {
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", initial_dir.CtoS()));
            logger.info("INITIAL DIR: " + initial_dir.CtoS());
            half_turn = false;
            scanning = true;
            dir_before_turn = direction;
            logger.info("DIR BEFORE TURN " + dir_before_turn.CtoS());
        } else if (scanning) {
            logger.info("SCANNING IN TURN");
            decision.put("action", "scan");
            scanning = false;
            full_turn = true;
        } else if (full_turn) {
            logger.info("DIR BEFORE TURN");
            Compass new_dir = dir_before_turn.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", new_dir.CtoS()));
            full_turn = false;
        } else {
            decision.put("action", "fly");
            scanning = true;
            half_turn = true;
            full_turn = true;
        }
        return decision;
    }

    private boolean checkEdges(Position pos, int height, int width) {
        if (Math.abs(pos.getY()) > height-2 || Math.abs(pos.getY()) < 2) {
            return true;
        } else if (Math.abs(pos.getX()) > width-2 || Math.abs(pos.getX()) < 2) {
            return true;
        }
        return false;
    }


    private void set_site(int x, int y) {
        site = new POI("BLAH", new Position(x, y));
    }

    @Override
    public String calculateClosest() {
        set_site(938,598);
        creeks.add(new POI("1creek", new Position(0, 2000)));

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
