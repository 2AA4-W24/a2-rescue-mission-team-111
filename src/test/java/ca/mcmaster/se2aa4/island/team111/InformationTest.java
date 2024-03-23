package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;


class InformationTest {

    // Check if Information returns the correct extras in the JSON object
    @Test
    void extrasTest() {
        JSONObject job = new JSONObject();
        job.put("extras","testextras");
        
        Information info = new Information(5, job);
        assertEquals(job,info.getExtra());
    }

    // Check if Information returns the correct input cost
    @Test
    void costTest(){
        Information info = new Information(5, null);
        assertEquals(5, info.getCost());
    }
}
