package ca.mcmaster.se2aa4.island.team111;

import org.json.JSONObject;

public interface Discoverer {
    void findIsland();
    JSONObject moveToIsland(); //Decision object might replace JSONObject type
}
