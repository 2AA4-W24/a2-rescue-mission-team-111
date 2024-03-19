package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class SecondTurn implements GridSearchState {
    @Override
    public JSONObject handle(GridSearcher searcher) {
        JSONObject decision = new JSONObject();
        searcher.setState(new ThirdTurn());
        Compass initial_dir = searcher.getInitialDir();
        Compass turningDir = initial_dir.opposite();
        decision.put("action", "heading");
        decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
        return decision;
    } 
}
