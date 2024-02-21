package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class EchoArrive {

    private int moves_to_island = 0;
    private int range;

    private Compass echo = Compass.NORTH;
    private Compass previous_dir = Compass.NORTH;

    private boolean echoFirst = true;
    private boolean echoingR = true;
    private boolean done = false;
    private boolean found_ground = false;

    private boolean firstTurn = true;
    private boolean secondTurn = false;

    public JSONObject findIsland(JSONObject extra, Compass direction) {

        JSONObject decision = new JSONObject();
        if (extra == null || extra.isEmpty() && !found_ground) {
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
        } else if (extra.has("found") && extra.get("found").equals("GROUND")) {
            found_ground = true;
            decision.put("action", "fly");
        } else if (found_ground) {
            previous_dir = direction;
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
        } else {
            decision.put("action", "fly");
        }
        
        return decision;
    }

    public JSONObject moveToIsland(JSONObject extras, Compass direction) {
        JSONObject decision = new JSONObject();

        if (firstTurn) {
            Compass fullTurn = previous_dir.opposite();
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", fullTurn.CtoS()));
            firstTurn = false;
            secondTurn = true;
            return decision;
        } else if (secondTurn) {
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
            secondTurn = false;
            return decision;
        }

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

    public boolean askIfDone() {
        return done;
    }

}
