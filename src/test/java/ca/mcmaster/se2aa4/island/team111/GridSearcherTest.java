package ca.mcmaster.se2aa4.island.team111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class GridSearcherTest {

    // @Test
    // void testCalculateClosest() {
    //     POI creek1 = new POI("id1",new Position(0, 0));
    //     POI creek2 = new POI("id2",new Position(5, 17));
    //     POI site = new POI("site baby",new Position(3, 4));

    //     GridSearcher gridsearch = new GridSearcher(Compass.EAST, Compass.SOUTH);
    //     gridsearch.addCreek(creek1);
    //     gridsearch.addCreek(creek2);
    //     gridsearch.setSite(site);

    //     assertEquals("id1",gridsearch.calculateClosest());
    // }

    // @Test 
    // void calculateClosestNoCreeks(){
    //     GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
    //     assertEquals("No creeks found", gs.calculateClosest());
    // }

    // @Test 
    // void calculateClosestNoSite(){
    //     GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
    //     POI creek = new POI("last",new Position(5, 17));
    //     gs.addCreek(creek);
    //     assertEquals("last",gs.calculateClosest());
    // }

    // @Test
    // void testCheckDistance(){
    //     GridSearcher gridsrch = new GridSearcher(Compass.EAST, Compass.SOUTH);
    //     gridsrch.setSite(new POI("id1",new Position(0,0))); 

    //     double result = gridsrch.getDistanceTest(new POI("id2",new Position(-2,0)));
    //     assertEquals(2,result);
    // }

    // @Test
    // void testCheckingDone() {
    //     GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
    //     gs.setStatePublic(gs.newCheckingDone());

    //     JSONObject job = new JSONObject();
    //     job.put("found","OUT_OF_RANGE");
    //     JSONObject expected = new JSONObject();
    //     expected.put("action","stop");
    //     Information info = new Information(15, job);

    //     Position pos = new Position(0, 0);
    //     assertEquals(expected.toString(),gs.performSearch(info, pos).toString());

    //     //Testing for if in range
    //     job = new JSONObject();
    //     job.put("found","");
    //     expected = new JSONObject();
    //     expected.put("action","fly");
    //     info = new Information(15, job);

    //     pos = new Position(0, 0);
    //     assertEquals(expected.toString(),gs.performSearch(info, pos).toString());
        
    // }

    // @Test
    // void testFindCreeks() {
    //     assertTrue(true);
    // }
}
