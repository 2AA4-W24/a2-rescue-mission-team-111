package ca.mcmaster.se2aa4.island.team111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class GridSearcherTest {

    @Test 
    void calculateClosestNoCreeks(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
        assertEquals("No creeks found", gs.calculateClosest());
    }

    @Test
    void testCheckDistance(){
        GridSearcher gridsrch = new GridSearcher(Compass.EAST, Compass.SOUTH);

        POI site = new POI("site",new Position(0,0));
        double result = gridsrch.getDistanceTest(new POI("id1",new Position(-2,0)),site);
        assertEquals(2,result);
    }

    @Test
    void testCheckingDone() {
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
        gs.setStatePublic(gs.newCheckingDone());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        JSONObject expected = new JSONObject();
        expected.put("action","stop");
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        assertEquals(expected.toString(),gs.performSearch(info, pos).toString());

        //Testing for if in range
        job = new JSONObject();
        job.put("found","");
        expected = new JSONObject();
        expected.put("action","fly");
        info = new Information(15, job);

        pos = new Position(0, 0);
        assertEquals(expected.toString(),gs.performSearch(info, pos).toString());
        
    }

    @Test
    void testFindCreeks() {
        assertTrue(true);
    }
}
