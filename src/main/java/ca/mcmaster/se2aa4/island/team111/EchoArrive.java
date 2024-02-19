package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class EchoArrive {

    private int moves_to_island = 0;
    private int range;

    Compass echo = Compass.NORTH;

    private boolean echoFirst = true;
    private boolean echoingR = true;

    public JSONObject findIsland(JSONObject extra, Compass direction) {

        JSONObject decision = new JSONObject();
        if (extra == null || extra.isEmpty()) {
                if (echoingR) {
                    echo = direction.right();
                    decision.put("action", "echo");
                    decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
                    echoingR = !echoingR;
                } else {
                    echo = direction.left();
                    decision.put("action", "echo");
                    decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
                    echoingR = !echoingR;
                }
        } else if (extra.get("found").equals("GROUND")) {
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
        } else {
            decision.put("action", "fly");
            }
        
        return decision;
    }

    public JSONObject moveToIsland(JSONObject extras, Compass direction) {
        JSONObject decision = new JSONObject();

        if (echoFirst) {
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS()));
            echoFirst = false;
            return decision;
        } 

        if (!extras.isEmpty()) {
            range = extras.getInt("range");
        }
        while (moves_to_island < range) {
            decision.put("action", "fly");
            moves_to_island++;
            return decision;
        }

        decision.put("action", "scan");
        return decision;
    }
}
