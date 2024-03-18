package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScanningState implements GridSearchState {
    @Override
    public void handle(GridSearcher searcher) {
        Information I = searcher.getCurrentInfo();
        JSONObject extras = I.getExtra();
        JSONObject decision = new JSONObject();
        
        JSONArray biomes = extras.getJSONArray("biomes");
        for (int i = 0; i<biomes.length(); i++) {
            if (!(biomes.get(i).equals("OCEAN"))) {
                Compass echoingDir = searcher.getDirBeforeTurn();
                searcher.setState(new EchoingForwardState());
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
                searcher.giveDecision(decision);
                return;
            }
        }
        searcher.setState(new FlyingState());
        searcher.giveDecision(new JSONObject("action", "fly"));
    }
}
