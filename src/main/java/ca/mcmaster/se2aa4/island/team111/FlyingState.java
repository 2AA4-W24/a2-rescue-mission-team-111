package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FlyingState implements GridSearchState {
    @Override
    public JSONObject handle(GridSearcher searcher) {
        JSONObject decision = new JSONObject();

        if (searcher.getGroundRange() > 2) {
            searcher.setState(new FlyingState());
            searcher.decrementGroundRange();
            decision.put("action", "fly");
            return decision;
        }
        searcher.setState(new ScanningState());
        decision.put("action", "scan");
        return decision;
    } 

}
