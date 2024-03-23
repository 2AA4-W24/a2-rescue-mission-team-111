package ca.mcmaster.se2aa4.island.team111;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Drone {

    private final Logger logger = LogManager.getLogger();

    private Battery battery;
    private Position pos = new Position(0, 0);
    private Compass initialDir;
    private Compass direction;
    private DroneState currentState = DroneState.FINDING;
    private IslandArriver islandArriver;
    private GridSearcher gridSearcher;
    private Information currentInfo = new Information(0, new JSONObject());
     

    public Drone(Integer charge, String dir) {
        this.battery = new Battery(charge);
        Compass compass = Compass.NORTH;
        Compass direc = compass.StoC(dir);
        this.initialDir = direc;
        this.direction = direc;
        this.islandArriver = new IslandArriver(direc);
    }

    // Getter for current info
    public Information getInfo(){
        return currentInfo;
    }

    //Receive info from acknowledge results here
    public void receiveInfo(Information I) {
        currentInfo = I;
        battery.depleteCharge(I.getCost());
    }

    //Getter for current state
    public DroneState getCurrentState(){
        return this.currentState;
    }


    public Decision giveDecision() {
        logger.info("Battery: " + battery.getCharge());
        Decision decision;
        //If battery is low, stop
        if (battery.isLow(pos)) {
            return new Decision("stop");
        }

        switch(currentState) {
            case FINDING:
                islandArriver.updateInfo(currentInfo);
                decision = islandArriver.find();
                String instruction1 = decision.getAction();
                if (instruction1.equals("fly")) {
                    pos = pos.changePosition(direction);
                }
                if (islandArriver.findingIsDone()) {
                    currentState = currentState.nextState();
                }
                return decision;
            case ARRIVING: 
                islandArriver.updateInfo(currentInfo);
                decision = islandArriver.moveTo();
                String instruction2 = decision.getAction();
                if (instruction2.equals("fly")) {
                    pos = pos.changePosition(direction);
                } else if (instruction2.equals("heading")) {
                    Compass old_dir = direction;
                    direction = decision.getDir();
                    pos = pos.changePosition(old_dir, direction);
                }
                if (islandArriver.arrivingIsDone()) {
                    this.gridSearcher = new GridSearcher(initialDir, direction);
                    currentState = currentState.nextState();
                }
                return decision;
            case SEARCHING:
                gridSearcher.updateInfo(currentInfo, pos);
                decision = gridSearcher.performSearch();
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
        return gridSearcher.giveClosestCreek();
    }
}