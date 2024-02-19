package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class EchoArrive {

    private int moves_to_island = 0;
    private int range;
    private boolean echoFirst = true;
    private boolean echoingR = true;

    public JSONObject findIsland(JSONObject extra, Compass direction) {

        JSONObject decision = new JSONObject();
        if (extra == null || extra.isEmpty()) {
            if (echoingR) {
                Compass echo = direction.right();
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
                echoingR = !echoingR;
            } else {
                Compass echo = direction.left();
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
                echoingR = !echoingR;
            }

        } else if (extra.get("found").equals("GROUND")) {
            if (!echoingR) {
                Compass turn = direction.right();
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", turn.CtoS()));
            } else {
                Compass turn = direction.left();
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", turn.CtoS()));
        }
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
        while (moves_to_island <= range) {
            decision.put("action", "fly");
            moves_to_island++;
            return decision;
        }

        decision.put("action", "scan");
        return decision;
    }
}
