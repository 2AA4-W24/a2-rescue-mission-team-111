package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher {

    private GridSearchState currentState = new FlyingSearcher();
    private Information currentInfo;
    private Position currentPos;

    private List<POI> creeks = new ArrayList<POI>();
    private POI site = new POI("NULL", new Position(0, 0));

    private Compass initialDir;
    private Compass dirBeforeTurn;

    private int range = 0;
    private int groundRange = 0;

    public GridSearcher(Compass firstDir, Compass currentDir) {
        this.initialDir = firstDir;
        this.dirBeforeTurn = currentDir;
    }

    // Public setter for testing
    public void setStatePublic(GridSearchState gState){
        this.setState(gState);
    }

    private void setState(GridSearchState gState) {
        currentState = gState;
    }

    public Decision performSearch(Information info, Position pos) {
        this.currentInfo = info;
        this.currentPos = pos;
        return currentState.handle(this);
    }

    // For Testing
    public Integer creeksAmount(){
        return creeks.size();
    }
    // For Testing
    public Position sitePosition(){
        return new Position(site.getXvalue(), site.getYvalue());
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

    private interface GridSearchState {
        public Decision handle(GridSearcher searcher);
    }

    // Public access to gstates
    public GridSearchState newCheckingDone(){return new CheckingDone();}
    public GridSearchState newEchoingForwardState(){return new EchoingForwardState();}
    public GridSearchState newFlyingState(){return new FlyingSearcher();}
    public GridSearchState newFlyWideTurn(){return new FlyWideTurn();}
    public GridSearchState newScanningState(){return new ScanningState();}
    public GridSearchState newFirstTurn(){return new FirstTurn();}
    public GridSearchState newSecondTurn(){return new SecondTurn();}
    public GridSearchState newThirdTurn(){return new ThirdTurn();}
    public GridSearchState newFourthTurn(){return new FourthTurn();}


    private class CheckingDone implements GridSearchState {

        @Override
        public Decision handle(GridSearcher searcher) {
            Information I = searcher.currentInfo;
            JSONObject extras = I.getExtra();
            Decision decision;

            if (extras.get("found").equals("OUT_OF_RANGE")) {
                decision = new Decision("stop");
            } else {
                searcher.setState(new FlyWideTurn());
                decision = new Decision("fly");
            }
            return decision;
        }
    }

    private class EchoingForwardState implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Information I = searcher.currentInfo;
            JSONObject extras = I.getExtra();
            Decision decision;

            if (extras.get("found").equals("OUT_OF_RANGE")) {
                range = extras.getInt("range");
                if (extras.getInt("range") > 2) {
                    searcher.setState(new FlyWideTurn());
                    decision = new Decision("fly");
                } else {
                    searcher.setState(new FirstTurn());
                    Compass turningDir = searcher.initialDir;
                    decision = new Decision("heading", turningDir);
                }
            } else {
                if (extras.getInt("range") > 1) {
                    groundRange = extras.getInt("range");
                }
                searcher.setState(new FlyingSearcher());
                decision = new Decision("fly");
            }
            return decision;

        } 

    }

    private class FirstTurn implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;
            searcher.setState(new CheckingDone());
            Compass dir_before_turn = searcher.dirBeforeTurn;
            Compass echoingDir = dir_before_turn.opposite();
            decision = new Decision("echo", echoingDir);
            return decision;
            
        } 
    }

    private class FlyingSearcher implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;

            if (searcher.groundRange > 2) {
                searcher.setState(new FlyingSearcher());
                groundRange--;
                decision = new Decision("fly");
                return decision;
            }
            searcher.setState(new ScanningState());
            decision = new Decision("scan");
            return decision;
        } 

    }

    private class FlyWideTurn implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;

            if (searcher.range > 3) {
                range = 3;
                decision = new Decision("fly");
                return decision;
            }  else if (searcher.range > 2) {
                range = 2;
                Compass turningDir = searcher.initialDir;
                searcher.setState(new FirstTurn());
                decision = new Decision("heading", turningDir);
                return decision;
            } else {
                searcher.setState(new SecondTurn());
                Compass dir_before_turn = searcher.dirBeforeTurn;
                Compass turningDir = dir_before_turn.opposite();
                decision = new Decision("heading", turningDir);
                return decision;
            }
        } 
    }

    private class FourthTurn implements GridSearchState {

        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;
            searcher.setState(new FlyingSearcher());
            dirBeforeTurn = dirBeforeTurn.opposite();
            decision = new Decision("scan");
            return decision;
        } 
    }

    private class ScanningState implements GridSearchState {

        @Override
        public Decision handle(GridSearcher searcher) {
            Information I = searcher.currentInfo;
            JSONObject extras = I.getExtra();
            Decision decision;

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
                    decision = new Decision("fly");
                    return decision;
                }
            }
            Compass echoingDir = searcher.dirBeforeTurn;
            searcher.setState(new EchoingForwardState());
            decision = new Decision("echo", echoingDir);
            return decision;
        }
    }

    private class SecondTurn implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;

            searcher.setState(new ThirdTurn());
            Compass initial_dir = searcher.initialDir;
            Compass turningDir = initial_dir.opposite();
            decision = new Decision("heading", turningDir);
            return decision;
        } 
    }

    private class ThirdTurn implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;
            searcher.setState(new FourthTurn());
            Compass dir_before_turn = searcher.dirBeforeTurn;
            Compass turningDir = dir_before_turn.opposite();
            decision = new Decision("heading", turningDir);
            return decision;
        } 
    }

}
