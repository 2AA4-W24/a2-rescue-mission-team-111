package ca.mcmaster.se2aa4.island.team111;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ScanningState implements GridSearchState {

    private final Logger logger = LogManager.getLogger();
    @Override
    public JSONObject handle(GridSearcher searcher) {
        Information I = searcher.getCurrentInfo();
        JSONObject extras = I.getExtra();
        JSONObject decision = new JSONObject();
        
        JSONArray biomes = extras.getJSONArray("biomes");
        for (int i = 0; i<biomes.length(); i++) {
            if (!(biomes.get(i).equals("OCEAN"))) {
                searcher.setState(new FlyingState());
                decision.put("action", "fly");
                return decision;
            }
        }
        Compass echoingDir = searcher.getDirBeforeTurn();
        logger.info("DIR: " + echoingDir.CtoS());
        searcher.setState(new EchoingForwardState());
        decision.put("action", "echo");
        decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
        return decision;
    }
}
