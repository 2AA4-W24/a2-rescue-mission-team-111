package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class EchoArrive {

    private int moves_to_island = 0;
    private int range;
    private boolean echoing = true;
    private boolean done = false;
    private boolean echoingR = true;

    public JSONObject findIsland(JSONObject extra, Compass direction) {

        JSONObject decision = new JSONObject();
        if (extra == null || extra.isEmpty()) {
            if (echoingR) {
                Compass echo = direction.right(direction);
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS(echo)));
                echoingR = false;
            } else {
                Compass echo = direction.left(direction);
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS(echo)));
                echoingR = true;
            }

        } else if (extra.get("found").equals("GROUND")) {
            if (!echoingR) {
                Compass turn = direction.right(direction);
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", turn.CtoS(turn)));
            } else {
                Compass turn = direction.left(direction);
                decision.put("action", "heading");
                decision.put("parameters", (new JSONObject()).put("direction", turn.CtoS(turn)));
        }
        } else {
            decision.put("action", "fly");
        }
        return decision;
    }

    public JSONObject moveToIsland(JSONObject extras, Compass direction) {
        JSONObject decision = new JSONObject();

        if (echoing) {
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS(direction)));
            echoing = false;
            return decision;
        } 

        if (!extras.isEmpty() && !done) {
            range = extras.getInt("range");
        }
        while (moves_to_island < range) {
            decision.put("action", "fly");
            moves_to_island++;
            return decision;
        }

        if (!done) {
            decision.put("action", "scan");
            done = true;
        } else {
            decision.put("action", "stop");
        }

        return decision;
    }

}
