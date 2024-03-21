package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;


class DroneTest {

    //If drone starts with no battery
    @Test
    void decisionLowBatteryTest() {
        Drone dr = new Drone(1,"E");
        JSONObject stopdec = new JSONObject();
        stopdec.put("action","stop");

        assertTrue(stopdec.similar(dr.giveDecision()));
        
    }

    @Test
    void recieveinfoTest(){
        Drone dr = new Drone(100, "E");
        Information info = new Information(5, null);
        dr.receiveInfo(info);
        assertEquals(info,dr.getInfo());
    }
}
