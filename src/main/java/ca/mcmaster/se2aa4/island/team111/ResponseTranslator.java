package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class ResponseTranslator implements Translator {

    private Information info;
    private JSONObject response;

    public ResponseTranslator(JSONObject droneResponse) {
        this.response = droneResponse;
    }

    //Translate JSONObject response into Information project
    @Override
    public void translate() {
        info = new Information(response.getInt("cost"), response.getJSONObject("extras"));
    }

    public Information getInfo() {
        return info;
    }
}