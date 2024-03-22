package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;

class DecisionTest {

    @Test
    public void decToJSON() {
        Decision dec = new Decision("fly");
        DecisionTranslator decT = new DecisionTranslator(dec);
        decT.translate();


        JSONObject expectedjob = new JSONObject();
        expectedjob.put("action","fly");
        assertEquals(expectedjob.toString(), decT.getDecision().toString());
    }
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
