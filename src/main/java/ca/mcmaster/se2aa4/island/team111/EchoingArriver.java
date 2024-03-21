package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class EchoingArriver implements ArriverState {
    @Override
    public JSONObject handle(Arriver arriver) {
        JSONObject decision = new JSONObject();
        Information I = arriver.currentInfo;
        JSONObject extras = I.getExtra();

        if (extras.get("found").equals("GROUND")) {
            arriver.findingDone = true;
            decision.put("action", "fly");
        } else {
            if (arriver.echoingRight) {
                arriver.setState(new EchoingArriver());
                Compass echo = arriver.initialDir;
                Compass echoingDir = echo.left();
                decision.put("action", "echo");
                decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
            } else {
                arriver.setState(new FlyingArriver());
                decision.put("action", "fly");
            }
            arriver.echoingRight = !(arriver.echoingRight);
        }
        
        return decision;
    }
}
