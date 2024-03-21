package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Drone {

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass initialDir;
    private Compass direction;
    private DroneState current_state = DroneState.FINDING;
    private Arriver a1;
    private GridSearcher g1;
    private Information current_info = new Information(0, new JSONObject());
     

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        Compass direc = compass.StoC(dir);
        this.initialDir = direc;
        this.direction = direc;
        this.a1 = new Arriver(direc);
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
    }


    public Decision giveDecision() {
        Decision decision;
        //If battery is low, stop
        if (battery.isLow()) {
            return new Decision("stop");
        }

        switch(current_state) {
            case FINDING:
                decision = a1.findIsland(current_info);
                String instruction1 = decision.getAction();

                if (instruction1.equals("fly")) {
                    pos = pos.changePosition(direction);
                }
                if (a1.findingIsDone()) {
                    current_state = current_state.nextState();
                }
                return decision;
            case ARRIVING: 
                decision = a1.moveToIsland(current_info.getExtra());
                String instruction2 = decision.getAction();
                if (instruction2.equals(decision)) {
                    pos = pos.changePosition(direction);
                } else if (instruction2.equals("heading")) {
                    Compass old_dir = direction;
                    direction = decision.getDir();
                    pos = pos.changePosition(old_dir, direction);
                }
                if (a1.arrivingIsDone()) {
                    this.g1 = new GridSearcher(initialDir, direction);
                    current_state = current_state.nextState();
                }
                return decision;
            case SEARCHING:
                decision = g1.performSearch(current_info, pos);
                String instruction3 = decision.getAction();
                if (instruction3.equals("heading")) {
                    Compass old_dir = direction;
                    direction = decision.getDir();
                    pos = pos.changePosition(old_dir, direction);
                } else if (instruction3.equals("fly")) {
                    pos = pos.changePosition(direction);
                }
                return decision;
        }
        return new Decision("stop");
    }

    public String giveClosest() {
        return g1.calculateClosest();
    }
}