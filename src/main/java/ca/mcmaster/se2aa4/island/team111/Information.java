package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

//The JSONObject response is encapsulated into this Information object
public class Information {
    private int cost;
    private JSONObject extras;

    public Information(int cost, JSONObject extras) {
        this.cost = cost;
        this.extras = extras;
    }

    public int getCost() {
        return cost;
    }

    public JSONObject getExtra() {
        return extras;
    }
}