package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FlyingState implements GridSearchState {
    @Override
    public void handle(GridSearcher searcher) {
        searcher.setState(new ScanningState());
        searcher.giveDecision(new JSONObject("action", "scan"));
    } 

}
