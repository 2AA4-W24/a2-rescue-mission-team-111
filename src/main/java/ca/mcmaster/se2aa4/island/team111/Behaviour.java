package ca.mcmaster.se2aa4.island.team111;

import org.json.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Behaviour{

    private final Logger logger = LogManager.getLogger();

    private int moves;
    private int range = 0;
    private String biome = "";


    public void findIsland(JSONObject extra) {
        if (extra.get("found").equals("GROUND")) {
            biome = (String) extra.get("found");
            range = extra.getInt("range");
        } else {
            biome = "NOT GROUND";
            range = -1;
        }
    }

    public JSONObject moveToIsland(JSONObject extra) {
        JSONObject decision = new JSONObject();
        if (moves<range) {
            decision.put("action", "fly");
            moves++;
        } else if (moves == range) {
            decision.put("action", "scan");
            moves++;
        } else {
            decision.put("action", "stop");
        }
        return decision;
    }

    public String giveBiome() {
        return biome;
    }

    public void setRange(int new_range) {
        this.range = new_range;
    }

    public int giveRange() {
        return range;
    }


}
