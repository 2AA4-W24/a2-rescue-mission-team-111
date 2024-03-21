package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher {

    private GridSearchState currentState = new FlyingSearcher();
    private Information currentInfo;
    private Position currentPos;

    private List<POI> creeks = new ArrayList<POI>(); //temporarily public for testing
    private POI site = new POI("NULL", new Position(0, 0));

    private Compass initialDir;
    private Compass dirBeforeTurn;

    private int range = 0;
    private int groundRange = 0;

    public GridSearcher(Compass firstDir, Compass direction) {
        this.initialDir = firstDir;
        this.dirBeforeTurn = direction;
    }

    private void setState(GridSearchState gState) {
        currentState = gState;
    }

    public JSONObject performSearch(Information info, Position pos) {
        this.currentInfo = info;
        this.currentPos = pos;
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
            if (getDistance(this_creek, site) < getDistance(closest_creek, site)) {
                closest_creek = this_creek;
            }
        }
        return closest_creek.getID();
    }

    public double getDistanceTest(POI creek){
        return getDistance(creek, site);
    }

    //Uses pythagorean mathematics to check distance
    private double getDistance(POI creek, POI site) {
        double x = Math.abs(site.getXvalue()-creek.getXvalue());
        double y = Math.abs(site.getYvalue()-creek.getYvalue());
        return Math.sqrt((x*x) + (y*y));
    }

    private interface GridSearchState {
        JSONObject handle(GridSearcher searcher);
    }

    private class CheckingDone implements GridSearchState {

        @Override
        public JSONObject handle(GridSearcher searcher) {
            Information I = searcher.currentInfo;
            JSONObject extras = I.getExtra();
            JSONObject decision = new JSONObject();

            if (extras.get("found").equals("OUT_OF_RANGE")) {
                decision.put("action", "stop");
                return decision;
            } else {
                searcher.setState(new FlyWideTurn());
                decision.put("action", "fly");
                return decision;
            }
        }
    }

    private class EchoingForwardState implements GridSearchState {
        @Override
        public JSONObject handle(GridSearcher searcher) {
            Information I = searcher.currentInfo;
            JSONObject extras = I.getExtra();
            JSONObject decision = new JSONObject();

            if (extras.get("found").equals("OUT_OF_RANGE")) {
                range = extras.getInt("range");
                if (extras.getInt("range") > 2) {
                    searcher.setState(new FlyWideTurn());
                    decision.put("action", "fly");
                    return decision;
                } else {
                    searcher.setState(new FirstTurn());
                    Compass turningDir = searcher.initialDir;
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
                    return decision;  
                }
            } else {
                if (extras.getInt("range") > 1) {
                    groundRange = extras.getInt("range");
                }
                searcher.setState(new FlyingSearcher());
                decision.put("action", "fly");
                return decision;
            }

        } 

    }

    private class FirstTurn implements GridSearchState {
        @Override
        public JSONObject handle(GridSearcher searcher) {
            JSONObject decision = new JSONObject();
            searcher.setState(new CheckingDone());
            Compass dir_before_turn = searcher.dirBeforeTurn;
            Compass echoingDir = dir_before_turn.opposite();
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
            return decision;
            
        } 
    }

    private class FlyingSearcher implements GridSearchState {
        @Override
        public JSONObject handle(GridSearcher searcher) {
            JSONObject decision = new JSONObject();

            if (searcher.groundRange > 2) {
                searcher.setState(new FlyingSearcher());
                groundRange--;
                decision.put("action", "fly");
                return decision;
            }
            searcher.setState(new ScanningState());
            decision.put("action", "scan");
            return decision;
        } 

    }

    private class FlyWideTurn implements GridSearchState {
        @Override
        public JSONObject handle(GridSearcher searcher) {
            JSONObject decision = new JSONObject();

            if (searcher.range > 3) {
                range = 3;
                decision.put("action", "fly");
                return decision;
            }  else if (searcher.range > 2) {
                range = 2;
                Compass turningDir = searcher.initialDir;
                searcher.setState(new FirstTurn());
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
                return decision;
            } else {
                searcher.setState(new SecondTurn());
                Compass dir_before_turn = searcher.dirBeforeTurn;
                Compass turningDir = dir_before_turn.opposite();
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
                return decision;
            }
        } 
    }

    private class FourthTurn implements GridSearchState {

        @Override
        public JSONObject handle(GridSearcher searcher) {
            JSONObject decision = new JSONObject();
            searcher.setState(new FlyingSearcher());
            dirBeforeTurn = dirBeforeTurn.opposite();
            decision.put("action", "scan");
            return decision;
        } 
    }

    private class ScanningState implements GridSearchState {

        @Override
        public JSONObject handle(GridSearcher searcher) {
            Information I = searcher.currentInfo;
            JSONObject extras = I.getExtra();
            JSONObject decision = new JSONObject();

            JSONArray c = extras.getJSONArray("creeks");
            if (!c.isEmpty()) {
                for (int i = 0; i<c.length(); i++) {
                    POI newPOI = new POI(c.getString(i), currentPos);
                    creeks.add(newPOI);
                }
            }

            JSONArray s = extras.getJSONArray("sites");
            if (!s.isEmpty()) {
                for (int i = 0; i<s.length(); i++) {
                    site = new POI(s.getString(i), currentPos);
                }
            }
            
            JSONArray biomes = extras.getJSONArray("biomes");
            for (int i = 0; i<biomes.length(); i++) {
                if (!(biomes.get(i).equals("OCEAN"))) {
                    searcher.setState(new FlyingSearcher());
                    decision.put("action", "fly");
                    return decision;
                }
            }
            Compass echoingDir = searcher.dirBeforeTurn;
            searcher.setState(new EchoingForwardState());
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
            return decision;
        }
    }

    private class SecondTurn implements GridSearchState {
        @Override
        public JSONObject handle(GridSearcher searcher) {
            JSONObject decision = new JSONObject();
            searcher.setState(new ThirdTurn());
            Compass initial_dir = searcher.initialDir;
            Compass turningDir = initial_dir.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
            return decision;
        } 
    }

    private class ThirdTurn implements GridSearchState {
        @Override
        public JSONObject handle(GridSearcher searcher) {
            JSONObject decision = new JSONObject();
            searcher.setState(new FourthTurn());
            Compass dir_before_turn = searcher.dirBeforeTurn;
            Compass turningDir = dir_before_turn.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
            return decision;
        } 
    }

}
