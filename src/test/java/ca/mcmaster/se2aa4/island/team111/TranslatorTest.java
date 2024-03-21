package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import org.json.JSONObject;
import org.json.JSONTokener;


class TranslatorTest {

    @Test
    void checkTranslateCorrectness() {
        Translator trans = new Translator();
        String example = "{\"cost\":16,\"extras\":{},\"status\":\"OK\"}";

        JSONObject extra = new JSONObject();

        Information info = new Information(16, extra);
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(example)));

        assertEquals(info.getCost(),trans.translate(response).getCost());
        assertEquals(info.getExtra().toString(),trans.translate(response).getExtra().toString());
    }
}
