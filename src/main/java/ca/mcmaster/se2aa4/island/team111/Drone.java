package ca.mcmaster.se2aa4.island.team111;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Drone implements DroneInfo {

    private final Logger logger = LogManager.getLogger();

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass direction;
    private DroneState current_state = DroneState.FINDING;
    private EchoArrive e1 = new EchoArrive();
    private Information current_info = new Information(0, new JSONObject());
    private GridSearcher g1 = new GridSearcher();

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        this.direction = compass.StoC(dir);
    }

    @Override
    public boolean lowBattery() {
        return battery.isLow();
    }

    @Override
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
                    direction = direction.StoC(parameters.getString("direction"));
                    current_state = current_state.nextState(current_state);
                }
                return decision;
            case ARRIVING: 
                decision = e1.moveToIsland(current_info.getExtra(), currentDirection());
                if (decision.get("action") == "scan") {
                    current_state = current_state.nextState(current_state);
                }
                return decision;
            case SEARCHING: 
                g1.checkPOI(current_info.getExtra(), pos);
                decision = g1.findCreeks();
                String closest = g1.calculateClosest();
                logger.info("Closest creek: " + closest);
                current_state.nextState(current_state);
                decision.put("action", "stop");
                return decision;
            case CALCULATING:
                System.out.println("Calculating closest creek...");
            
        }
        return decision;
    }

    public void displayResults() {
        System.out.println("no creek found");
    }
}