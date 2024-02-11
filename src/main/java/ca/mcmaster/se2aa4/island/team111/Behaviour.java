package ca.mcmaster.se2aa4.island.team111;

import org.json.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Behaviour{

    private final Logger logger = LogManager.getLogger();

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
        JSONArray biomes = new JSONArray(extra.getJSONArray("biomes"));
        for (int i = 0; i<biomes.length(); i++) {
            if (!biomes.get(i).equals("OCEAN") || !extra.getJSONArray("creeks").isEmpty()) {
                decision.put("action", "stop");
                return decision;
            } 
        }
        decision.put("action", "fly");
        return decision;
    }

    public String giveBiome() {
        return biome;
    }

    public int giveRange() {
        return range;
    }


}
