package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FourthTurn implements GridSearchState {

    @Override
    public JSONObject handle(GridSearcher searcher) {
        JSONObject decision = new JSONObject();
        searcher.setState(new ScanningState());
        searcher.toggleDirBeforeTurn();
        decision.put("action", "scan");
        return decision;
    } 
}
