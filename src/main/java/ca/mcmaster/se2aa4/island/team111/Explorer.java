package ca.mcmaster.se2aa4.island.team111;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener; 

public class Explorer implements IExplorerRaid, Compass {

    private final Logger logger = LogManager.getLogger();

    Translator t = new Translator();
    Drone drone;

    public JSONObject extras;
    Behaviour b1 = new Behaviour();
    Surroundings s1 = new Surroundings();

    String dir;
    String new_dir;

    int moves = 0;
    int maxmoves = 50;

    int current_moves = 0;
    int moves_to_island;

    int choice = 2;
    int final_choice;

    boolean island_found = false;
    boolean turned = false;
    boolean echoing = true;
    boolean finding = true;


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
        logger.info("Left:", headingRotate(direction, Turn.L));
    }

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject();

        if (drone.lowBattery()) {
            logger.info("Low battery!");
            decision.put("action", "stop");
        }

        if (island_found) {
            if (!turned) {
            switch (final_choice) {
                case 0: switch (dir) {
                    case "E": new_dir = "S"; break;
                    case "S": new_dir = "W"; break;
                    case "N": new_dir = "E"; break;
                    case "W": new_dir = "N"; break;
                }
                break;
                case 1: switch(dir) {
                    case "E": new_dir = "N"; break;
                    case "S": new_dir = "E"; break;
                    case "N": new_dir = "W"; break;
                    case "W": new_dir = "S"; break;
                }
                break;
            }
            logger.info("New direction: " + new_dir);
            decision.put("action", "heading");
            decision.put("parameters", (new JSONObject()).put("direction", new_dir));
            turned = true;
            return decision.toString();
            

        } else {
            if (echoing) {
                logger.info("DIRECTION: " + new_dir);
                decision = s1.echoForwards(new_dir);
                echoing = false;
                return decision.toString();
            } else {
                if (finding) {
                    b1.findIsland(extras);
                    finding = false;
                } 
                decision = b1.moveToIsland(extras);
                return decision.toString();
            }
        }
            
        } else {
        if((moves<=maxmoves)&&(moves%2 == 0)){
            decision.put("action", "fly");
        }else if (moves<=maxmoves){
            if (choice % 3 == 0) {
                decision = s1.echoRight(dir);
            } else if (choice % 3 == 1) {
                decision = s1.echoLeft(dir);
            } else {
                decision = s1.echoForwards(dir);
            }
        }

            if (moves>6 && moves%2 == 0) { 
            Behaviour b1 = new Behaviour();
            b1.findIsland(extras);
            logger.info("TRUE BIOME: " + b1.giveBiome());
            if (b1.giveBiome().equals("GROUND")) {
                final_choice = (choice-1)%3;
                logger.info("TRUE RANGE: " + b1.giveRange());
                island_found = true;
            }
        }
        moves++;
        choice++;
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
