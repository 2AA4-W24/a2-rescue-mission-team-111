package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Surroundings {

    public JSONObject echoRight(String direction) {
        JSONObject decision = new JSONObject();

        switch(direction) {
            case "E": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "S")); break;
            case "N": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "E")); break;
            case "W": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "N")); break;
            case "S": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "W")); break;
        }
    
        return decision;
    }

    public JSONObject echoLeft(String direction) {
        JSONObject decision = new JSONObject();

        switch(direction) {
            case "E": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "N")); break;
            case "N": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "W")); break;
            case "W": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "S")); break;
            case "S": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "E")); break;
        }
    
        return decision;
    }
    public JSONObject echoForwards(String direction) {
        JSONObject decision = new JSONObject();

        switch(direction) {
            case "E": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "E")); break;
            case "N": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "N")); break;
            case "W": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "W")); break;
            case "S": decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", "S")); break;
        }
    
        return decision;

    }
}
