package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Drone implements DroneInfo {
    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass direction;

    public Drone(Integer charge, Compass dir) {
        this.battery = new Battery(charge);
        this.direction = dir;
    }

    @Override
    public boolean lowBattery() {
        return battery.isLow();
    }

    @Override
    public String currentDirection() {
        return direction.CtoS(direction);
    }

    
    public void receiveInfo(Information I) {
        battery.depleteCharge(I.getCost());
    }


    public JSONObject giveDecision() {
        JSONObject decision = new JSONObject();

        if (this.lowBattery()) {
            decision.put("action", "stop");
            return decision;
        }

        decision.put("action", "stop");
        return decision;
    }

    public void displayResults() {
        System.out.println("no creek found");
    }

}
