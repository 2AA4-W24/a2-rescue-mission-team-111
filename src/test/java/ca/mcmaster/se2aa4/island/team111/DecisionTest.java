package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;

class DecisionTest {

    // Testing the conversion from Decision to JSON action
    @Test
    public void decToJSON() {
        Decision dec = new Decision("fly");
        DecisionTranslator decT = new DecisionTranslator(dec);
        decT.translate();


        JSONObject expectedjob = new JSONObject();
        expectedjob.put("action","fly");
        assertEquals(expectedjob.toString(), decT.getDecision().toString());
    }

    // Testing the conversion from Decision to JSON action, including the heading (N,W,E,S)
    @Test
    public void decToJSONWithHeading() {
        Decision dec = new Decision("echo",Compass.EAST);
        DecisionTranslator decT = new DecisionTranslator(dec);
        decT.translate();

        JSONObject expectedjob = new JSONObject();
        expectedjob.put("action","echo");
        expectedjob.put("parameters", (new JSONObject()).put("direction", "E"));
        assertEquals(expectedjob.toString(), decT.getDecision().toString());
    }
}
