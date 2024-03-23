package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import org.json.JSONObject;
import org.json.JSONTokener;


class ResponseTranslatorTest {

    // Test correctness for translating from JSONObject response to type Decision 
    @Test
    void checkTranslateCorrectness() {
        String example = "{\"cost\":16,\"extras\":{},\"status\":\"OK\"}";

        JSONObject extra = new JSONObject();

        Information info = new Information(16, extra);
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(example)));
        ResponseTranslator rtrans = new ResponseTranslator(response);
        rtrans.translate();
        Information newinfo = rtrans.getInfo();

        assertEquals(info.getCost(),newinfo.getCost());
        assertEquals(info.getExtra().toString(),newinfo.getExtra().toString());
    }
}
