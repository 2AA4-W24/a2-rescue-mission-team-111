package ca.mcmaster.se2aa4.island.team111;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

public class behaviour{

    private final static Logger logger = LogManager.getLogger();
    private static int range = 0;
    private static String biome = "";


    public static String findIsland(JSONObject extra) {
        if (extra.get("found").equals("GROUND")) {
            biome = (String) extra.get("found");
            logger.info("Biome: " + biome);
            range = extra.getInt("range");
            logger.info("Range: " + range);
        } else {
            biome = "NOT GROUND";
            range = -1;
            logger.info("Biome: " + biome);
            logger.info("Range: " + range);
        }
        logger.info("TRUE BIOME: " + extra.get("found"));
        return biome;
    }

    public static String giveBiome() {
        return biome;
    }

    public static int giveRange() {
        return range;
    }

}
