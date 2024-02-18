package ca.mcmaster.se2aa4.island.team111;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener; 

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();

    Translator t = new Translator();
    Drone drone;

    public JSONObject extras;
    EchoArrive e1 = new EchoArrive();
    String dir;
    boolean island_found = false;


    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        dir = direction;
        Integer batteryLevel = info.getInt("budget");
        drone = new Drone(batteryLevel, direction);
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject();

        if (drone.lowBattery()) {
            logger.info("Low battery!");
            decision.put("action", "stop");
        }

        if (island_found) {
            decision = e1.moveToIsland(extras);
            return decision.toString();
        } else {
            decision = e1.findIsland(extras, dir);
            if (decision.get("action") == "heading") {
                island_found = true;
            }
        }
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        Information I = t.translate(response);
        drone.receiveInfo(I);
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
