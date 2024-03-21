package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public class Arriver {

    private ArriverState currentState = new FlyingArriver();
    public Information currentInfo;
    public boolean echoingRight = true;
    public int theRange;
    public Compass initialDir;
    public Compass newDir;

    public boolean findingDone = false;
    public boolean arrivingDone = false;
    
    private String magicWord = "action";

    private int moves_to_island = 0;
    private int range;

    private boolean echoFirst = true;

    private boolean firstTurn = true;
    private boolean secondTurn = false;
    private boolean thirdTurn = false;

    public Arriver(Compass direction) {
        this.initialDir = direction;
    }

    public void setState(ArriverState state) {
        currentState = state;
    }

    public JSONObject findIsland(Information info, Compass direction) {

        currentInfo = info;
        JSONObject decision = currentState.handle(this);
        // if (extra == null || extra.isEmpty() && !found_ground) { //If we just flew and didn't find ground yet
        //         //Keep echoing left and right
        //         if (echoingR) {
        //             echo = direction.right();
        //             decision.put(magicWord, "echo");
        //             decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
        //             echoingR = !echoingR;
        //         } else {
        //             echo = direction.left();
        //             decision.put(magicWord, "echo");
        //             decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS()));
        //             echoingR = !echoingR;
        //         }
        // } else if (extra.has("found") && extra.get("found").equals("GROUND")) {
        //     found_ground = true;
        //     decision.put(magicWord, "fly"); //If we found ground, fly one ahead
        // } else if (found_ground) {
        //     previous_dir = direction;
        //     decision.put(magicWord, "heading");
        //     decision.put("parameters", (new JSONObject()).put("direction", echo.CtoS())); //Turn towards the direction that gave us ground
        // } else {
        //     decision.put(magicWord, "fly");
        // }
        
        // if (decision.get(magicWord) == "heading") {
        //     finding_done = true;
        // }
        return decision;
    }



    public JSONObject moveToIsland(JSONObject extras, Compass direction) {
        JSONObject decision = new JSONObject();

        //Algorithm completes an efficient turn instead of missing the first column/row of the island
        if (firstTurn) {
            if (echoingRight) {
                newDir = initialDir.right();
            } else {
                newDir = initialDir.left();
            }
            decision.put(magicWord, "heading");
            decision.put("parameters", (new JSONObject()).put("direction", newDir.CtoS()));
            firstTurn = false;
            secondTurn = true;
            thirdTurn = true;
            return decision;
            } else if (secondTurn) {
            Compass oppTurn = initialDir.opposite();
            decision.put(magicWord, "heading");
            decision.put("parameters", (new JSONObject()).put("direction", oppTurn.CtoS()));
            secondTurn = false;
            return decision;
        } else if (thirdTurn) {
            decision.put(magicWord, "heading");
            decision.put("parameters", (new JSONObject()).put("direction", newDir.CtoS()));
            thirdTurn = false;
            return decision;
        }

        //Echo to see how far away the island is
        if (echoFirst) {
            decision.put(magicWord, "echo");
            decision.put("parameters", (new JSONObject()).put("direction", newDir.CtoS()));
            echoFirst = false;
            return decision;
        } 

        //Store the distance of the island from previous echo in range
        if (!extras.isEmpty()) {
            range = extras.getInt("range");
        }

        //fly until you arrive at island
        if (moves_to_island <= range) {
            decision.put(magicWord, "fly");
            moves_to_island++;
            return decision;
        }

        //scan when reached island
        decision.put(magicWord, "scan");
        arrivingDone = true;
        return decision;
    }

    public boolean findingIsDone() {
        return findingDone;
    }

    public boolean arrivingIsDone() {
        return arrivingDone;
    }


}
