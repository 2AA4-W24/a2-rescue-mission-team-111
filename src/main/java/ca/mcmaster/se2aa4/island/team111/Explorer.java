package ca.mcmaster.se2aa4.island.team111;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener; 

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private Drone drone;

    @Override
    public void initialize(String s) {
        logger.info(" Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info(" Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        drone = new Drone(batteryLevel, direction);
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {

        Decision decision = drone.giveDecision();

        DecisionTranslator decisionTranslator = new DecisionTranslator(decision);
        decisionTranslator.translate();
        JSONObject finalDecision = decisionTranslator.getDecision();

        return finalDecision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        ResponseTranslator responseTranslator = new ResponseTranslator(response);
        responseTranslator.translate();
        Information info = responseTranslator.getInfo(); //Eliminates coupling to JSONObject
        drone.receiveInfo(info);
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
    }

    @Override
    public String deliverFinalReport() {
        String closest = drone.giveClosest(); 
        return closest;
    }

}


