package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class CheckingDone implements GridSearchState {
    @Override
    public void handle(GridSearcher searcher) {
        Information I = searcher.getCurrentInfo();
        JSONObject extras = I.getExtra();
        JSONObject decision = new JSONObject();

        if (extras.get("found").equals("OUT_OF_RANGE")) {
            decision.put("action", "stop");
            searcher.giveDecision(decision);
        } else {
            searcher.setState(new ScanningState());
            decision.put("action", "scan");
            searcher.giveDecision(decision);
        }
    }
}
