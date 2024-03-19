package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Drone {

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
        g1.updateInfo(I, pos);
        battery.depleteCharge(I.getCost());
    }

    public JSONObject giveDecision() {
        JSONObject decision = new JSONObject();

        //If battery is low, stop
        if (battery.isLow()) {
            decision.put("action", "stop");
            return decision;
        }

        switch(current_state) {
            case FINDING:
                decision = e1.findIsland(current_info.getExtra(), direction);
                if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                } else if (decision.get("action") == "fly") {
                    pos = pos.changePositionFly(direction);
                }
                if (e1.findingDone()) {
                    current_state = current_state.nextState();
                }
                return decision;
            case ARRIVING: 
                decision = e1.moveToIsland(current_info.getExtra(), direction);
                if (decision.get("action") == "fly") {
                    pos = pos.changePositionFly(direction);
                } else if (decision.get("action") == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                }
                if (e1.arrivingDone()) {
                    current_state = current_state.nextState();
                    g1.setDirBeforeTurn(direction);
                }
                return decision;
            case SEARCHING:
                decision = g1.performSearch(direction, pos, current_info); //Find POIs
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