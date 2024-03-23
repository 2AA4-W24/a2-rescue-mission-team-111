package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team111.IslandArriver.EchoingArriver;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;


class DroneTest {

    // Drone should stop if it has low battery
    @Test
    void decisionLowBatteryTest() {
        Drone dr = new Drone(1,"E");
        assertEquals("stop",dr.giveDecision().getAction());
    }

    // Check if the drone will switch to "ARRIVING" state when ground is found
    @Test
    void findingToArriving(){
        Drone dr = new Drone(5000,"E");
        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("range",0);
        job.put("found","GROUND");
        Information info = new Information(15, job);

        dr.receiveInfo(info);
        dr.giveDecision();
        dr.giveDecision();

        assertEquals(DroneState.ARRIVING, dr.getCurrentState());
    }

    // Check if the drone will switch to "SEARCHING" when ground is reached
    @Test
    void arrivingToSearching(){
        Drone dr = new Drone(5000,"E");
        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("range", -1);
        job.put("found","GROUND");
        Information info = new Information(15, job);

        dr.receiveInfo(info);

        // Exhaust all the commands in the queue (8 times)
        for(int i = 0;i < 8;i++){
            dr.giveDecision();
        }

        assertEquals(DroneState.SEARCHING, dr.getCurrentState());
    }

    // Test if drone info matches input info
    @Test
    void recieveinfoTest(){
        Drone dr = new Drone(100, "E");
        Information info = new Information(5, null);
        dr.receiveInfo(info);
        assertEquals(info,dr.getInfo());
    }
}
