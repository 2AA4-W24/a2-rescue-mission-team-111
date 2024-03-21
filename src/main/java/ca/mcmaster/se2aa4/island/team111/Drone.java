package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Drone {

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass direction;
    private DroneState current_state = DroneState.FINDING;
    private Arriver a1;
    private GridSearcher g1;
    private Information current_info = new Information(0, new JSONObject());
    private String magicWord = "action";

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        Compass direc = compass.StoC(dir);
        this.direction = direc;
        this.a1 = new Arriver(direc);
        this.g1 = new GridSearcher(direc);
    }

    // Getter for current info
    public Information getInfo(){
        return current_info;
    }
    
    // Public setter for drone state
    public void setState(DroneState ds){
        this.current_state = ds;
    }

    //Receive info from acknowledge results here
    public void receiveInfo(Information I) {
        current_info = I;
        battery.depleteCharge(I.getCost());
        battery.depleteCharge(0);
    }



    public JSONObject giveDecision() {
        JSONObject decision = new JSONObject();
        //If battery is low, stop
        if (battery.isLow()) {
            decision.put(magicWord, "stop");
            return decision;
        }

        switch(current_state) {
            case FINDING:
                decision = a1.findIsland(current_info);
                if (decision.get(magicWord) == "fly") {
                    pos = pos.changePositionFly(direction);
                }
                if (a1.findingIsDone()) {
                    current_state = current_state.nextState();
                }
                return decision;
            case ARRIVING: 
                decision = a1.moveToIsland(current_info.getExtra());
                if (decision.get(magicWord) == "fly") {
                    pos = pos.changePositionFly(direction);
                } else if (decision.get(magicWord) == "heading") {
                    JSONObject parameters = decision.getJSONObject("parameters");
                    Compass old_dir = direction;
                    direction = direction.StoC(parameters.getString("direction"));
                    pos = pos.changePositionTurn(old_dir, direction);
                }
                if (a1.arrivingIsDone()) {
                    this.g1 = new GridSearcher(g1, direction);
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