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
    private GridSearcher g1 = new GridSearcher(Compass.NORTH);
    private Information current_info = new Information(0, new JSONObject());

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        this.direction = compass.StoC(dir);
        this.g1 = new GridSearcher(direction);
    }
    
    //Receive info from acknowledge results here
    public void receiveInfo(Information I) {
        current_info = I;
        g1.updateInfo(I);
        battery.depleteCharge(I.getCost());
    }

    public JSONObject giveDecision() {
        JSONObject decision = new JSONObject();

        //If battery is low, stop
        if (battery.isLow()) {
            decision.put("action", "stop");
            return decision;
        }

        //According to current state, execute methods
        switch(current_state) {
            case FINDING:
                decision = e1.findIsland(current_info.getExtra(), direction);
                if (decision.get("action") == "heading") {
                    //If we turn when finding island, then go to next phase of exploration
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                    current_state = current_state.nextState();
                } else if (decision.get("action") == "fly") {
                    pos = pos.changePositionFly(direction);
                }
                //Update position according to command returned
                return decision;
            case ARRIVING: 
                //Start moving to island and only move to next phase if we get scan back
                decision = e1.moveToIsland(current_info.getExtra(), direction);
                if (decision.get("action") == "scan") {
                    current_state = current_state.nextState();
                    g1.setDirBeforeTurn(direction);
                } else if (decision.get("action") == "fly") {
                    pos = pos.changePositionFly(direction);
                } else if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    logger.info("NEW DIR: " + direction.CtoS());
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                }
                return decision;
            case SEARCHING:
                logger.info("DIR: " + direction.CtoS());
                Position current_pos = pos;
                g1.checkPOI(current_info.getExtra(), current_pos); //Check if you got a POI from information
                decision = g1.findCreeks(direction, current_pos, current_info); //Find POIs
                if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                } else if (decision.get("action") == "fly") {
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