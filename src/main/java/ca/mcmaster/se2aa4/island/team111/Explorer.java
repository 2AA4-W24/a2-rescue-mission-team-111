package ca.mcmaster.se2aa4.island.team111;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid, Compass {

    int moves = 0;
    int maxmoves = 50;
    private final Logger logger = LogManager.getLogger();
    public static JSONObject extras;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        logger.info("Left:", headingRotate(direction, Turn.L));
    }

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject();
        JSONObject parameters = new JSONObject();

        if((moves<=maxmoves)&&(moves%2 == 0)){
            decision.put("action", "fly");
        }else if (moves<=maxmoves){
            parameters.put("direction","S");
            decision.put("action","echo");
            decision.put("parameters",parameters);
        }

        if (moves>6 && moves%2 == 0) {
            if (behaviour.findIsland(extras) == "GROUND") {
            logger.info("Range: " + behaviour.giveRange());
            }
        }
        moves += 1;

        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        extras = extraInfo;
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }



}
