package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Drone implements DroneInfo {
    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass direction; //When Direction enum created, will be Direction direction

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        this.direction = direction.toCompass(dir);
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
