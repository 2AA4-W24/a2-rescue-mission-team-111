package ca.mcmaster.se2aa4.island.team111;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Drone {

    private final Logger logger = LogManager.getLogger();

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass direction;
    private DroneState current_state = DroneState.FINDING;
    private EchoArrive e1 = new EchoArrive();
    private GridSearcher g1;
    private Information current_info = new Information(0, new JSONObject());
    private String magicWord = "action";

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        this.direction = compass.StoC(dir);
    }

    // Getter for current info
    public Information getInfo(){
        return current_info;
    }
    
    //Receive info from acknowledge results here
    public void receiveInfo(Information I) {
        current_info = I;
        battery.depleteCharge(I.getCost());
    }

    public JSONObject giveDecision() {
        JSONObject decision = new JSONObject();
        logger.info("Battery: " + battery.getCharge());
        //If battery is low, stop
        if (battery.isLow()) {
            decision.put(magicWord, "stop");
            return decision;
        }

        switch(current_state) {
            case FINDING:
                decision = e1.findIsland(current_info.getExtra(), direction);
                if (decision.get(magicWord) == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                    this.g1 = new GridSearcher(old_dir, direction);
                } else if (decision.get(magicWord) == "fly") {
                    pos = pos.changePositionFly(direction);
                }
                if (e1.findingDone()) {
                    current_state = current_state.nextState();
                }
                return decision;
            case ARRIVING: 
                decision = e1.moveToIsland(current_info.getExtra(), direction);
                if (decision.get(magicWord) == "fly") {
                    pos = pos.changePositionFly(direction);
                } else if (decision.get(magicWord) == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                }
                if (e1.arrivingDone()) {
                    current_state = current_state.nextState();
                }
                return decision;
            case SEARCHING:
                decision = g1.performSearch(current_info, pos);
                if (decision.get(magicWord) == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                } else if (decision.get(magicWord) == "fly") {
                    pos = pos.changePositionFly(direction);
                }
                return decision;
        }
        return decision;
    }

    public String giveClosest() {
        return g1.calculateClosest();
    }
}