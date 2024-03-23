package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import org.json.JSONObject;
import org.json.JSONTokener;


class DecisionTranslatorTest {

    @Test
    void checkTranslateCorrectness() {
        Decision dec = new Decision("fly");
        DecisionTranslator dtrans = new DecisionTranslator(dec);
        dtrans.translate();

        JSONObject expected = new JSONObject();
        expected.put("action", "fly");

        assertEquals(expected.toString(), dtrans.getDecision().toString());
    }
}