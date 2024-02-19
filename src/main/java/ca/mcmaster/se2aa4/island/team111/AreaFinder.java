package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class AreaFinder {
    private boolean height_found = false;
    private boolean width_found = false;
    private boolean checkingL = true;
    
    public JSONObject findHeight(Compass direction) {
        JSONObject decision = new JSONObject();

        switch(direction) {
            case NORTH:
            case SOUTH: 
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS()));
                height_found = true; break;
            case EAST: 
            case WEST:
            if (checkingL) {
                decision = checkLeft(direction);
                checkingL = false;
                height_found = true;
            } else {
                decision = checkRight(direction);
                checkingL = true;
            } break;
        }

        return decision;
    }

    public JSONObject findWidth(Compass direction) {
        JSONObject decision = new JSONObject();

        switch(direction) {
            case WEST:
            case EAST: 
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS()));
                width_found = true; break;
            case NORTH: 
            case SOUTH:
            if (checkingL) {
                decision = checkLeft(direction);
                checkingL = false;
                width_found = true;
            } else {
                decision = checkRight(direction);
                checkingL = true;
            } break;
        }

        return decision;
    }

    public boolean foundHeight() {
        return height_found;
    }

    public boolean foundWidth() {
        return width_found;
    }

    private JSONObject checkLeft(Compass direction) {
        JSONObject decision = new JSONObject();

        direction = direction.left();
        decision.put("action", "echo");
        decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS()));

        return decision;
    }

    private JSONObject checkRight(Compass direction) {
        JSONObject decision = new JSONObject();

        direction = direction.right();
        decision.put("action", "echo");
        decision.put("parameters", (new JSONObject()).put("direction", direction.CtoS()));
        
        return decision;
    }

}
