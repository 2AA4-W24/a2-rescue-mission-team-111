package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Drone implements DroneInfo {

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass direction;
    private boolean island_found = false;
    private EchoArrive e1 = new EchoArrive();
    private Information current_info = new Information(0, new JSONObject());

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

        if (!island_found) {
            decision = e1.findIsland(current_info.getExtra(), currentDirection());
            if (decision.get("action") == "heading") {
                JSONObject parameters = decision.getJSONObject("parameters");
                direction = direction.StoC(parameters.getString("direction"));
                island_found = true;
            }
            return decision;
        } else {
            decision = e1.moveToIsland(current_info.getExtra(), currentDirection());
            return decision;
        }
    }

    public void displayResults() {
        System.out.println("no creek found");
    }

}
