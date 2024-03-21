package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class FlyingArriver implements ArriverState {
    @Override
    public JSONObject handle(Arriver arriver) {
        JSONObject decision = new JSONObject();

        arriver.setState(new EchoingArriver());
        Compass echo = arriver.initialDir;
        Compass echoingDir = echo.right();
        decision.put("action", "echo");
        decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));

        return decision;
    }

}
