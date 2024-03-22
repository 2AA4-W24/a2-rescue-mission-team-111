package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team111.IslandArriver.EchoingArriver;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;


class DroneTest {

    //If drone starts with no battery
    @Test
    void decisionLowBatteryTest() {
        Drone dr = new Drone(1,"E");
        assertEquals("stop",dr.giveDecision().getAction());
    }

    // @Test
    // void findingToArriving(){
    //     Drone dr = new Drone(5000,"E");
    //     JSONObject job = new JSONObject();
    //     job.put("extras","");
    //     job.put("found","GROUND");
    //     Information info = new Information(15, job);

    //     dr.receiveInfo(info);
    //     dr.giveDecision();
    //     dr.giveDecision();

    //     assertEquals(DroneState.ARRIVING, dr.getCurrentState());
    // }

    // @Test
    // void arrivingToSearching(){
    //     Drone dr = new Drone(5000,"E");
    //     JSONObject job = new JSONObject();
    //     job.put("extras","");
    //     job.put("found","GROUND");
    //     Information info = new Information(15, job);

    //     // Exhaust all the initial "arriving" commands (8 times)
    //     dr.receiveInfo(info);
    //     dr.giveDecision();
    //     dr.giveDecision();
    //     dr.giveDecision();
    //     dr.giveDecision();
    //     dr.giveDecision(); 
    //     dr.giveDecision();
    //     dr.giveDecision();
    //     dr.giveDecision();
        
    //     assertEquals(DroneState.SEARCHING, dr.getCurrentState());
    // }

    @Test
    void recieveinfoTest(){
        Drone dr = new Drone(100, "E");
        Information info = new Information(5, null);
        dr.receiveInfo(info);
        assertEquals(info,dr.getInfo());
    }
}
