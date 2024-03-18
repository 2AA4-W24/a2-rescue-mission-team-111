package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher implements POIFinder {

    public List<POI> creeks = new ArrayList<POI>(); //temporarily public for testing
    public POI site = new POI("NULL", new Position(26, -26));

    private Compass initial_dir;
    private Compass dir_before_turn;
    private Compass new_dir = Compass.NORTH;

    //Do we need to reduce number of boolean attributes? If so, how to switch between decisions every takeDecision() loop?
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
    }


    //Check if we're too close to the edge
    private String closeEcho(Information I) {
        JSONObject extra = I.getExtra();
        if (extra.getString("found").equals("OUT_OF_RANGE") && extra.getInt("range") < 2) {
            return "Now";
        } else if (extra.getString("found").equals("OUT_OF_RANGE") && extra.getInt("range") < 3) {
            return "Early"; //Likely need to change this 
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

    //Grid-searching algorithm
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

    //check if there's no more land in front of you
    private boolean echoCheck(Information I) {
        JSONObject extra = I.getExtra();
        if (extra.has("found")) {
            if (extra.get("found").equals("OUT_OF_RANGE")) {
                return true;
            }
        }
        return false;
    }

    //If we're on ocean-only, check if we can turn
    private boolean checkTurn(Information I) {
        JSONObject extra = I.getExtra();

        if (extra.has("biomes")) {
            JSONArray biomes = extra.getJSONArray("biomes");
            for (int i = 0; i<biomes.length(); i++) {
                if (!(biomes.get(i).equals("OCEAN"))) {
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    //Make appropriate turns to efficiently turn instead of leaving gaps in the searching of the map.
    //We do this by making multiple turns
    //Before making this turns, we fly 1-2 times so we don't miss anything when turning
    //We make 4 turns in total to efficiently turn
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
            new_dir = dir_before_turn.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", new_dir.CtoS()));
            full_turn = false;
            recenter = true;
        } else if (recenter) {
            Compass opposite_dir = initial_dir.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", opposite_dir.CtoS()));
            recenter = false;
            second_recenter = true;
        } else if (second_recenter) {
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

    @Override
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
