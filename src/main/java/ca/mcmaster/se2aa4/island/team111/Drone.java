package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Drone {

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass initialDir;
    private Compass direction;
    private DroneState currentState = DroneState.FINDING;
    private IslandArriver islandArriver;
    private CreekSearcher gridSearcher;
    private Information currentInfo = new Information(0, new JSONObject());
     

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        Compass direc = compass.StoC(dir);
        this.initialDir = direc;
        this.direction = direc;
        this.islandArriver = new IslandArriver(direc);
    }

    public Information getInfo(){
        return currentInfo;
    }

    public void receiveInfo(Information I) {
        currentInfo = I;
        battery.depleteCharge(I.getCost());
    }

    public DroneState getCurrentState(){
        return this.currentState;
    }

    public Decision giveDecision() {
        Decision decision;
        //If battery is low, stop
        if (battery.isLow(pos)) {
            return new Decision("stop");
        }

        switch(currentState) {
            case FINDING:
                islandArriver.updateInfo(currentInfo);
                decision = islandArriver.find(); 
                if (decision.getAction().equals("fly")) {
                    pos = pos.changePosition(direction);
                }
                if (islandArriver.findingIsDone()) {
                    currentState = currentState.nextState();
                }
                return decision;
            case ARRIVING: 
                islandArriver.updateInfo(currentInfo);
                decision = islandArriver.moveTo(); 
                if (decision.getAction().equals("fly")) {
                    pos = pos.changePosition(direction);
                } else if (decision.getAction().equals("heading")) {
                    Compass oldDir = direction;
                    direction = decision.getDir();
                    pos = pos.changePosition(oldDir, direction);
                }
                if (islandArriver.arrivingIsDone()) {
                    this.gridSearcher = new GridSearcher(initialDir, direction);
                    currentState = currentState.nextState();
                }
                return decision;
            case SEARCHING:
                gridSearcher.updateInfo(currentInfo, pos);
                decision = gridSearcher.performSearch(); 
                if (decision.getAction().equals("heading")) {
                    Compass oldDir = direction;
                    direction = decision.getDir();
                    pos = pos.changePosition(oldDir, direction);
                } else if (decision.getAction().equals("fly")) {
                    pos = pos.changePosition(direction);
                }
                return decision;
        }
        return new Decision("stop");
    }

    public String giveClosest() {
        return gridSearcher.giveClosestCreek();
    }
}