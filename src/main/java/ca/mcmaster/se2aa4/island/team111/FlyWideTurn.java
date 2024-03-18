package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FlyWideTurn implements GridSearchState {

    JSONObject decision = new JSONObject();

    @Override
    public void handle(GridSearcher searcher) {
        if (searcher.getRange() > 3) {
            searcher.setRange(3);
            searcher.giveDecision(new JSONObject("action", "fly"));
        }  else if (searcher.getRange() > 2) {
            searcher.setRange(2);
            Compass turningDir = searcher.getInitialDir();
            searcher.setState(new FirstTurn());
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
            searcher.giveDecision(decision);
        } else {
            searcher.setState(new SecondTurn());
            Compass dir_before_turn = searcher.getDirBeforeTurn();
            Compass turningDir = dir_before_turn.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
            searcher.giveDecision(decision);
        }
    } 
}
