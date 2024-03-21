package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Translator {

    //Translate JSONObject response into Information project
    public Information translate(JSONObject response) {
        return new Information(response.getInt("cost"), response.getJSONObject("extras"));
    }
}