package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class EchoingForwardState implements GridSearchState {
    @Override
    public JSONObject handle(GridSearcher searcher) {
        Information I = searcher.getCurrentInfo();
        JSONObject extras = I.getExtra();
        JSONObject decision = new JSONObject();

        if (extras.get("found").equals("OUT_OF_RANGE")) {
            searcher.setRange(extras.getInt("range"));
            if (extras.getInt("range") > 2) {
                searcher.setState(new FlyWideTurn());
                decision.put("action", "fly");
                return decision;
            } else {
                searcher.setState(new FirstTurn());
                Compass turningDir = searcher.getInitialDir();
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
                return decision;
            }
        } else {
            searcher.setState(new FlyingState());
            decision.put("action", "fly");
            return decision;
        }

    } 

}
