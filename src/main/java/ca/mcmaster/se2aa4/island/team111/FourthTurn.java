package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FourthTurn implements GridSearchState {
    @Override
    public void handle(GridSearcher searcher) {
        JSONObject decision = new JSONObject();
        searcher.setState(new CheckingDone());
        Compass dir_before_turn = searcher.getDirBeforeTurn();
        Compass echoingDir = dir_before_turn.opposite();
        decision.put("action", "echo");
        decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
        
    } 
}
