package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GridSearcher implements CreekSearcher {

    private GridSearchState currentState = new FlyingSearcher();
    private Information currentInfo;
    private Position currentPos;

    private Map<Position, List<String>> allBiomes = new HashMap<>();
    private List<POI> creeks = new ArrayList<>();
    private POI site = new POI("NULL", new Position(0, 0)); //Position of site is initially hard-coded before being assigned

    private Compass initialDir;
    private Compass dirBeforeTurn;

    private int range = 0;
    private int groundRange = 0;

    public GridSearcher(Compass firstDir, Compass currentDir) {
        this.initialDir = firstDir;
        this.dirBeforeTurn = currentDir;
    }

    public POI giveSite(){
        return this.site;
    }

    public int creeksAmount(){
        return this.creeks.size();
    }

    public void setState(GridSearchState gState) {
        currentState = gState;
    }

    public void updateInfo(Information info, Position pos) {
        this.currentInfo = info;
        this.currentPos = pos;
    }

    @Override
    public Decision performSearch() {
        return currentState.handle(this);
    }

    @Override
    public String giveClosestCreek() {
        AreaMap areaMap = new AreaMap(allBiomes, creeks, site);
        return areaMap.calculateClosestCreek();
    }

    //Beginning of internal state pattern
    private interface GridSearchState {
        public Decision handle(GridSearcher searcher);
    }
 
    //This state checks if we are done grid-searching by echoing and checking for out of range during the turn
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

    //This state checks how far away we are from out of range or ground. If ground is far enough, we assign that range to groundRange
    //If out of range is an acceptable distance away, we start our big turn to recenter to the next column
    //If out of range is too close, we turn earlier 
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

    //This is the first turn in our big turn
    private class FirstTurn implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;
            searcher.setState(new ScanningInTurn());
            decision = new Decision("scan");
            return decision;
            
        } 
    }

    //We scan right after our first turn in case there is a creek there
    private class ScanningInTurn implements GridSearchState {
        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;
            Information I = currentInfo;
            JSONObject extras = I.getExtra();

            addCreek(I);
            addSite(I);

            
            JSONArray biomes = extras.getJSONArray("biomes");
            List<String> biomeList = new ArrayList<>();
            for (int i = 0; i<biomes.length(); i++) {
                biomeList.add(biomes.getString(i));
            }
            allBiomes.put(currentPos, biomeList);

            searcher.setState(new CheckingDone());
            Compass dirBeforeTheTurn = searcher.dirBeforeTurn;
            Compass echoingDir = dirBeforeTheTurn.opposite();
            decision = new Decision("echo", echoingDir);
            return decision;
        }

    }

    //This state is the typical state of flying for the GridSearcher, where it keeps flying if groundRange is still a lot
    //Otherwise, we scan
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

    //This state is for the necessary flies in the big turn
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

    //This state is the final turn of the big turn, before we go back to scanning the island regularly
    private class FourthTurn implements GridSearchState {

        @Override
        public Decision handle(GridSearcher searcher) {
            Decision decision;
            searcher.setState(new ScanningState());
            dirBeforeTurn = dirBeforeTurn.opposite();
            decision = new Decision("scan");
            return decision;
        } 
    }

    //This state adds the associated creeks and sites(if any) as well as biomes
    //If we scan only OCEAN, it's time to echo forward and check if we should U-turn. Otherwise, fly normally
    private class ScanningState implements GridSearchState {

        @Override
        public Decision handle(GridSearcher searcher) {
            Information I = searcher.currentInfo;
            JSONObject extras = I.getExtra();
            Decision decision;

            addCreek(I);
            addSite(I);
            
            JSONArray biomes = extras.getJSONArray("biomes");
            List<String> biomeList = new ArrayList<>();
            for (int i = 0; i<biomes.length(); i++) {
                biomeList.add(biomes.getString(i));
            }
            allBiomes.put(currentPos, biomeList);

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

    //This is the second turn of our big turn, taking us to the third turn
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

    //This is the third turn of our big turn, taking us to the fourth turn
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

    private void addCreek(Information info) {
        JSONObject extras = info.getExtra();
        JSONArray c = extras.getJSONArray("creeks");
        if (!c.isEmpty()) {
            for (int i = 0; i<c.length(); i++) {
                POI newPOI = new POI(c.getString(i), currentPos);
                creeks.add(newPOI);
            }
        }
    }

    private void addSite(Information info) {
        JSONObject extras = info.getExtra();
        JSONArray s = extras.getJSONArray("sites");
        if (!s.isEmpty()) {
            for (int i = 0; i<s.length(); i++) {
                site = new POI(s.getString(i), currentPos);
            }
        }
    }

       // Public access to gstates purely for testing
       public GridSearchState newCheckingDone(){return new CheckingDone();}
       public GridSearchState newEchoingForwardState(){return new EchoingForwardState();}
       public GridSearchState newFlyingState(){return new FlyingSearcher();}
       public GridSearchState newFlyWideTurn(){return new FlyWideTurn();}
       public GridSearchState newScanningState(){return new ScanningState();}
       public GridSearchState newFirstTurn(){return new FirstTurn();}
       public GridSearchState newSecondTurn(){return new SecondTurn();}
       public GridSearchState newThirdTurn(){return new ThirdTurn();}
       public GridSearchState newFourthTurn(){return new FourthTurn();}

}
