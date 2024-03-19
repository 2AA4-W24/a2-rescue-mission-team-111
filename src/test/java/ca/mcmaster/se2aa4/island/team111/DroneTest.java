package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;


public class DroneTest {

    //If drone starts with no battery
    @Test
    public void decisionLowBattery() {
        Drone dr = new Drone(1,"E");
        JSONObject stopdec = new JSONObject();
        stopdec.put("action","stop");

        assertTrue(stopdec.similar(dr.giveDecision()));
        
    }
}