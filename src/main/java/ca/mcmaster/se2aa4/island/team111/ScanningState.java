package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScanningState implements GridSearchState {

    @Override
    public JSONObject handle(GridSearcher searcher) {
        Information I = searcher.getCurrentInfo();
        JSONObject extras = I.getExtra();
        JSONObject decision = new JSONObject();

        JSONArray c = extras.getJSONArray("creeks");
        if (!c.isEmpty()) {
            for (int i = 0; i<c.length(); i++) {
                searcher.addCreek(c.getString(i));
            }
        }

        JSONArray s = extras.getJSONArray("sites");
        if (!s.isEmpty()) {
            for (int i = 0; i<s.length(); i++) {
                searcher.addSite(s.getString(i));
            }
        }
        
        JSONArray biomes = extras.getJSONArray("biomes");
        for (int i = 0; i<biomes.length(); i++) {
            if (!(biomes.get(i).equals("OCEAN"))) {
                searcher.setState(new FlyingState());
                decision.put("action", "fly");
                return decision;
            }
        }
        Compass echoingDir = searcher.getDirBeforeTurn();
        searcher.setState(new EchoingForwardState());
        decision.put("action", "echo");
        decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
        return decision;
    }
}
