package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher implements POIFinder {

    private final Logger logger = LogManager.getLogger();

    public List<POI> creeks = new ArrayList<POI>();
    public POI site = null;

    private Compass initial_dir;
    private Compass dir_before_turn;
    private Compass new_dir = Compass.NORTH;

    private boolean scanning = true;
    private boolean flying = false;
    private boolean echoing = true;
    private boolean secondTurn = false;

    private boolean flyOnce = true;
    private boolean flyTwice = false;
    private boolean half_turn = false;
    private boolean full_turn = false;
    private boolean recenter = false;
    private boolean second_recenter = false;
    private boolean done = true;


    public GridSearcher(Compass direction) {
        this.initial_dir = direction;
        site = new POI("NULL", new Position(26, 26));
    }


    private String closeEcho(Information I) {
        JSONObject extra = I.getExtra();
        if (extra.getString("found").equals("OUT_OF_RANGE") && extra.getInt("range") < 2) {
            return "Now";
        } else if (extra.getString("found").equals("OUT_OF_RANGE") && extra.getInt("range") < 3) {
            return "Early";
        } else {
            return "Good";
        }

    }


    @Override
    public JSONObject findCreeks(Compass direction, Position pos, Information I) {
        JSONObject decision = new JSONObject();

        decision = firstSearch(direction, pos, I);
        
        return decision;
    }

    private JSONObject firstSearch(Compass direction, Position pos, Information I) {
        JSONObject decision = new JSONObject();
            if (checkTurn(I)) {
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS()));
            } else if (echoCheck(I)) {
                if (closeEcho(I).equals("Now")) {
                    dir_before_turn = direction;
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", initial_dir.CtoS()));
                    secondTurn = true;
                    return decision;
                } else if (closeEcho(I).equals("Early")) {
                    flyOnce = false;
                    flyTwice = true;
                    decision = makeTurns(direction, I);
                } else {
                    decision = makeTurns(direction, I);
                }
            } else if (secondTurn) {
                new_dir = dir_before_turn.opposite();
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", new_dir.CtoS()));
                secondTurn = false;
                return decision;
            } else if (!done) {
                decision = makeTurns(direction, I);
            } else {
                if (scanning) {
                    decision.put("action", "scan");
                    scanning = false;
                } else {
                    decision.put("action", "fly");
                    scanning = true;
                }
            }

        return decision;
    }


    private boolean echoCheck(Information I) {
        JSONObject extra = I.getExtra();
        if (extra.has("found")) {
            if (extra.get("found").equals("OUT_OF_RANGE")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTurn(Information I) {
        JSONObject extra = I.getExtra();

        if (extra.has("biomes")) {
            JSONArray biomes = extra.getJSONArray("biomes");
            for (int i = 0; i<biomes.length(); i++) {
                logger.info("BIOME " + biomes.get(i));
                if (!(biomes.get(i).equals("OCEAN"))) {
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    private JSONObject makeTurns(Compass direction, Information I) {
        JSONObject decision = new JSONObject();

        if (flyOnce) {
            decision.put("action", "fly");
            flyOnce = false;
            flyTwice = true;
            done = false;
            return decision;
        } else if (flyTwice) {
            decision.put("action", "fly");
            flyTwice = false;
            half_turn = true;
            done = false;
            return decision;
        }
         if (half_turn) {
            logger.info("TURN 1");
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", initial_dir.CtoS()));
            half_turn = false;
            flying = true;
            dir_before_turn = direction;
            done = false;
        } else if (flying) {
            decision.put("action", "fly");
            flying = false;
            full_turn = true;
        } else if (full_turn) {
            logger.info("TURN 2");
            new_dir = dir_before_turn.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", new_dir.CtoS()));
            full_turn = false;
            recenter = true;
        } else if (recenter) {
            logger.info("TURN 3");
            Compass opposite_dir = initial_dir.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", opposite_dir.CtoS()));
            recenter = false;
            second_recenter = true;
        } else if (second_recenter) {
            logger.info("TURN 4");
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", new_dir.CtoS()));
            second_recenter = false;
            echoing = true;
        } else if (echoing) {
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", new_dir.CtoS()));
            echoing = false;
        } else {
            if (echoCheck(I)) {
                decision.put("action", "stop");
            } else {
                flyOnce = true;
                flyTwice = true;
                half_turn = true;
                scanning = false;
                full_turn = true;
                recenter = true;
                second_recenter = true;
                echoing = true;
                done = true;
                decision.put("action", "scan");
            }
        }
        return decision;
    }


    @Override
    public String calculateClosest() {

        POI closest_creek = creeks.get(0);

        for (int i = 1; i<creeks.size(); i++) {
            POI this_creek = creeks.get(i);
            logger.info("CHAMPION ID: " + closest_creek.getID());
            logger.info("CHAMPION DISTANCE: " + getDistance(closest_creek));
            logger.info("CHALLENGER ID: " + this_creek.getID());
            logger.info("CHALLENGER DISTANCE: " + getDistance(this_creek));
            if (getDistance(this_creek) < getDistance(closest_creek)) {
                closest_creek = this_creek;
            }
        }
        logger.info("EMERGENCY ID: " + site.getID());

        return closest_creek.getID();
    }

    private double getDistance(POI creek) {
        Position site_pos = site.getPos();
        Position creek_pos = creek.getPos();
        int x = Math.abs(site_pos.getX()-creek_pos.getX());
        int y = Math.abs(site_pos.getY()-creek_pos.getY());
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
