package ca.mcmaster.se2aa4.island.team111;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Drone {

    private final Logger logger = LogManager.getLogger();

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass direction;
    private DroneState current_state = DroneState.MEASURING;
    private EchoArrive e1 = new EchoArrive();
    private GridSearcher g1;
    private AreaFinder a1 = new AreaFinder();
    private Information current_info = new Information(0, new JSONObject());
    private int map_height = -1;
    private int map_width = -1;

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
            case MEASURING:
                decision = a1.findHeight(currentDirection());
                JSONObject extras = current_info.getExtra();
                if (extras.has("found")) {
                    if ((extras.getInt("range") == 0)) {
                        return decision;
                    } else {
                        map_height = extras.getInt("range");
                    }
                } else {
                    return decision;
                }

                decision = a1.findWidth(currentDirection());
                extras = current_info.getExtra();
                if (!a1.foundWidth()) {
                    return decision;
                } else {
                    if (extras.has("found")) {
                        if ((extras.getInt("range") == 0)) {
                            return decision;
                        } else {
                            map_width = extras.getInt("range");

                    } 
                    } else {
                        return decision;
                    }
                }
                current_state = current_state.nextState();
                break;

            case FINDING:
                decision = e1.findIsland(current_info.getExtra(), currentDirection());
                if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos.changePositionTurn(old_dir, direction);
                    current_state = current_state.nextState();
                } else if (decision.get("action") == "fly") {
                    pos.changePositionFly(currentDirection());
                }
                return decision;
            case ARRIVING: 
                decision = e1.moveToIsland(current_info.getExtra(), currentDirection());
                if (decision.get("action") == "scan") {
                    current_state = current_state.nextState();
                } else if (decision.get("action") == "fly") {
                    pos.changePositionFly(currentDirection());
                }
                return decision;
            case SEARCHING: 
                g1.checkPOI(current_info.getExtra(), pos);
                decision = g1.findCreeks(direction, pos, current_info, map_height, map_width);
                if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos.changePositionTurn(old_dir, direction);
                } else if (decision.get("action") == "fly") {
                    pos.changePositionFly(direction);
                }
                logger.info("CURRENT DIRECTION: " + direction);
                logger.info("x-position: " + pos.getX());
                logger.info("y-position: " + pos.getY());
                logger.info("CURRENT BATTERY " + battery.getCharge());
                return decision;
            case CALCULATING:
                g1.calculateClosest();
        }
        return decision;
    }

    public void displayResults() {
        System.out.println("no creek found");
    }
}