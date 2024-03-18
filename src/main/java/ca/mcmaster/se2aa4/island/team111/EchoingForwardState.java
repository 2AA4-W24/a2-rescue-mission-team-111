package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class EchoingForwardState implements GridSearchState {
    @Override
    public void handle(GridSearcher searcher) {
        Information I = searcher.getCurrentInfo();
        JSONObject extras = I.getExtra();
        JSONObject decision = new JSONObject();

        searcher.setRange(extras.getInt("range"));
        if (extras.getInt("range") > 2) {
            searcher.setState(new FlyWideTurn());
            decision.put("action", "fly");
            searcher.giveDecision(decision);
        } else {
            searcher.setState(new turnQuickly());
            Compass turningDir = searcher.getInitialDir();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
            searcher.giveDecision(decision);
        }

    } 

}
