package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class surroundings {

    public static JSONObject echoRight(String direction) {
        JSONObject decision = new JSONObject();
    
        if (direction.equals("E")) {
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "S"));
        } else {
            decision.put("action", "stop");
        }
        return decision;
    }

    public static JSONObject echoLeft(String direction) {
        JSONObject decision = new JSONObject();
    
        if (direction.equals("E")) {
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "N"));
        } else {
            decision.put("action", "stop");
        }
        return decision;
    }
    public static JSONObject echoForwards(String direction) {
        JSONObject decision = new JSONObject();
    
        if (direction.equals("E")) {
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "E"));
        } else {
            decision.put("action", "stop");
        }
        return decision;
    }

}
