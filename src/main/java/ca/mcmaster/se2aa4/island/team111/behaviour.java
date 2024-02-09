package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class behaviour{

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

    public String giveBiome() {
        return biome;
    }

    public int giveRange() {
        return range;
    }

}
