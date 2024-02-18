package ca.mcmaster.se2aa4.island.team111;


import org.json.JSONObject;

public class EchoArrive {

    private int moves_to_island = 0;
    private int range;
    private boolean echoing = true;
    private boolean done = false;
    private boolean echoingR = true;
    private String new_dir;

    public JSONObject findIsland(JSONObject extra, String direction) {

        JSONObject decision = new JSONObject();
        if (extra == null || extra.isEmpty()) {
            if (echoingR) {
                decision = echoRight(direction); //later, drone will use this and pass it's own direction in
                echoingR = false;
            } else {
                decision = echoLeft(direction);
                echoingR = true;
            }

        } else if (extra.get("found").equals("GROUND")) {
            if (!echoingR) {
                switch (direction) { //will implement rotation in Compass/Direction
                    case "E": new_dir = "S";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "S")); break;
                    case "S": new_dir = "W";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "W")); break;
                    case "N": new_dir = "E";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "E")); break;
                    case "W": new_dir = "N";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "N")); break;
                }
            } else {
                switch (direction) { //will implement rotation in Compass/Direction
                    case "E": new_dir = "N";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "N")); break;
                    case "S": new_dir = "E";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "E")); break;
                    case "N": new_dir = "W";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "W")); break;
                    case "W": new_dir = "S";
                    decision.put("action", "heading");
                    decision.put("parameters", (new JSONObject()).put("direction", "S")); break;
            }
        }
        } else {
            decision.put("action", "fly");
        }
        return decision;
    }

    public JSONObject moveToIsland(JSONObject extras) {
        JSONObject decision = new JSONObject();

        if (echoing) {
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", new_dir));
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

    private JSONObject echoRight(String direction) {
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

    private JSONObject echoLeft(String direction) {
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
}
