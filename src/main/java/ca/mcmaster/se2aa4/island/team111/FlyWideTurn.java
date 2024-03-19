package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FlyWideTurn implements GridSearchState {


    @Override
    public JSONObject handle(GridSearcher searcher) {
        JSONObject decision = new JSONObject();

        if (searcher.getRange() > 3) {
            searcher.setRange(3);
            decision.put("action", "fly");
            return decision;
        }  else if (searcher.getRange() > 2) {
            searcher.setRange(2);
            Compass turningDir = searcher.getInitialDir();
            searcher.setState(new FirstTurn());
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
            return decision;
        } else {
            searcher.setState(new SecondTurn());
            Compass dir_before_turn = searcher.getDirBeforeTurn();
            Compass turningDir = dir_before_turn.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
            return decision;
        }
    } 
}
