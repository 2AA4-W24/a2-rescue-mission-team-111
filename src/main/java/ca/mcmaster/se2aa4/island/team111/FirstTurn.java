package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FirstTurn implements GridSearchState {
    @Override
    public void handle(GridSearcher searcher) {
        searcher.setState(new FlyWideTurn());
        searcher.giveDecision(new JSONObject("action", "fly"));
    } 
}
