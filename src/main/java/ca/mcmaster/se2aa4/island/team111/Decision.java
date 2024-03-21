package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Decision {
    private String magicWord = "action";
    private String action;
    private Compass direction = null;

    public Decision(String theAction) {
        this.action = theAction;
    }

    public Decision(String theAction, Compass dir) {
        this.action = theAction;
        this.direction = dir;
    }

    public String getAction() {
        return this.action;
    }

    public Compass getDir() {
        return this.direction;
    }

    public JSONObject DecisionToJSON() {
        JSONObject decision = new JSONObject();
        if (direction == null) {
            decision.put(magicWord, action);
        } else {
            decision.put(magicWord, action);
            decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS()));
        }
        return decision;
    }
}
