package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class DecisionTranslator implements Translator {

    private Decision finalDecision;
    private JSONObject JSONDecision;
    private String magicWord = "action"; //If keyword action is changed, it is maintanable without changing the whole code

    public DecisionTranslator(Decision decision) {
        this.finalDecision = decision;
    }

    @Override
    public void translate() {
        JSONObject decision = new JSONObject();
        if (finalDecision.getDir() == Compass.NONE) { //If there is no direction, treat as fly or scan.
            decision.put(magicWord, finalDecision.getAction());
        } else {
            decision.put(magicWord, finalDecision.getAction());
            decision.put("parameters", (new JSONObject()).put("direction", finalDecision.getDir().CtoS()));
        }
        this.JSONDecision = decision;
    }

    public JSONObject getDecision() {
        return JSONDecision;
    }
}
