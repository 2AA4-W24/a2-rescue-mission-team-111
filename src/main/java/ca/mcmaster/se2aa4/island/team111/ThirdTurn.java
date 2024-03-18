package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class ThirdTurn implements GridSearchState {
    @Override
    public void handle(GridSearcher searcher) {
        JSONObject decision = new JSONObject();
        searcher.setState(new FourthTurn());
        Compass dir_before_turn = searcher.getDirBeforeTurn();
        Compass turningDir = dir_before_turn.opposite();
        decision.put("action", "heading");
        decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
        searcher.giveDecision(decision);
    } 
}
