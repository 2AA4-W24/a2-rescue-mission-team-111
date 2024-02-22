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

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        this.direction = compass.StoC(dir);
        this.g1 = new GridSearcher(direction);
    }

    public boolean lowBattery() {
        return battery.isLow();
    }

    public Compass currentDirection() {
        return direction;
    }

    public Position getPosition() {
        return pos;
    }
    
    public void receiveInfo(Information I) {
        current_info = I;
        battery.depleteCharge(I.getCost());
    }

    public JSONObject giveDecision() {
        JSONObject decision = new JSONObject();

        if (this.lowBattery()) {
            decision.put("action", "stop");
            return decision;
        }

        switch(current_state) {
            case FINDING:
                decision = e1.findIsland(current_info.getExtra(), currentDirection());
                if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                    current_state = current_state.nextState();
                } else if (decision.get("action") == "fly") {
                    pos = pos.changePositionFly(currentDirection());
                }
                return decision;
            case ARRIVING: 
                decision = e1.moveToIsland(current_info.getExtra(), currentDirection());
                if (decision.get("action") == "scan") {
                    current_state = current_state.nextState();
                } else if (decision.get("action") == "fly") {
                    pos = pos.changePositionFly(currentDirection());
                } else if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                }
                return decision;
            case SEARCHING: 
                Position current_pos = pos;
                if (current_info.getExtra().has("creeks")) {
                    g1.checkPOI(current_info.getExtra(), current_pos);
                }
                logger.info("x-position: " + pos.getX());
                logger.info("y-position: " + pos.getY());
                decision = g1.findCreeks(direction, current_pos, current_info);
                if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                } else if (decision.get("action") == "fly") {
                    pos = pos.changePositionFly(direction);
                }
                logger.info("CURRENT BATTERY " + battery.getCharge());
                return decision;
        }
        return decision;
    }

    public String giveClosest() {
        return g1.calculateClosest();
    }
}