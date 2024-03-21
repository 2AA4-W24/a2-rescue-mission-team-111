package ca.mcmaster.se2aa4.island.team111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class GridSearcherTest {

    @Test 
    void calculateClosestNoCreeks(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
        assertEquals("No creeks found", gs.calculateClosest());
    }

    @Test
    void calculateClosestWithCreeks(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newScanningState());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        JSONArray creekArray = new JSONArray();
        JSONArray siteArray = new JSONArray();
        JSONArray biomeArray = new JSONArray();

        job.put("creeks",creekArray);
        job.put("sites",siteArray);
        job.put("biomes",biomeArray);

        Information info = new Information(15, job);

        // Simulate at position
        creekArray.put("creekfar");
        gs.performSearch(info, new Position(52, 1000));
        creekArray.clear();

        JSONObject job2 = new JSONObject();
        job2.put("found","");
        job2.put("range",5);

        creekArray = new JSONArray();
        siteArray = new JSONArray();
        biomeArray = new JSONArray();

        job2.put("creeks",creekArray);
        job2.put("sites",siteArray);
        job2.put("biomes",biomeArray);

        info = new Information(15, job2);
        // Simulate at position 2
        creekArray.put("creekclose");
        gs.setStatePublic(gs.newScanningState());
        gs.performSearch(info, new Position(1, 1));

        //After scanning, check the closest creek
        assertEquals("creekclose", gs.calculateClosest());
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
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        assertEquals("stop",gs.performSearch(info, pos).getAction());

        //Testing for if in range
        job = new JSONObject();
        job.put("found","");
        info = new Information(15, job);

        pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("fly",dec.getAction());
        
    }

    @Test
    void testEchoingForwardStateInRange(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("fly",dec.getAction());
    }
    @Test
    void testEchoingForwardStateOutofRange(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",1);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("heading",dec.getAction());
        assertEquals(Compass.EAST,dec.getDir());
    }

    @Test
    void testEchoingForwardStateNormal(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("fly",dec.getAction());
    }

    @Test
    void testFirstTurn(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newFirstTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("echo",dec.getAction());
        assertEquals(Compass.WEST, dec.getDir());
    }
    @Test
    void testSecondTurn(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newSecondTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        JSONObject expected = new JSONObject();
        expected.put("action","heading");
        expected.put("parameters", (new JSONObject()).put("direction", "W"));
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("heading",dec.getAction());
        assertEquals(Compass.WEST, dec.getDir());
    }
    @Test
    void testThirdTurn(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newThirdTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("heading",dec.getAction());
        assertEquals(Compass.WEST, dec.getDir());
    }
    @Test
    void testFourthTurn(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newFourthTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("scan",dec.getAction());
    }

    @Test
    void testScanningStateFoundCreeks(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newScanningState());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        JSONArray creekArray = new JSONArray();
        creekArray.put("examplecreek");

        JSONArray siteArray = new JSONArray();
        JSONArray biomeArray = new JSONArray();

        job.put("creeks",creekArray);
        job.put("sites",siteArray);
        job.put("biomes",biomeArray);

        JSONObject expected = new JSONObject();
        expected.put("action","scan");

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.performSearch(info, pos);
        
        assertTrue(gs.creeksAmount() > 0);
    }
    @Test
    void testScanningStateFoundSites(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newScanningState());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        JSONArray creekArray = new JSONArray();
        JSONArray siteArray = new JSONArray();
        siteArray.put("examplesite");

        JSONArray biomeArray = new JSONArray();

        job.put("creeks",creekArray);
        job.put("sites",siteArray);
        job.put("biomes",biomeArray);

        Information info = new Information(15, job);

        Position pos = new Position(5, 9);
        gs.performSearch(info, pos);
        
        assertEquals(5, gs.sitePosition().getX());
        assertEquals(9, gs.sitePosition().getY());
    }
    @Test
    void testScanningStateFoundOcean(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newScanningState());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        JSONArray creekArray = new JSONArray();
        JSONArray siteArray = new JSONArray();
        JSONArray biomeArray = new JSONArray();
        biomeArray.put("OCEAN");

        job.put("creeks",creekArray);
        job.put("sites",siteArray);
        job.put("biomes",biomeArray);

        JSONObject expected = new JSONObject();
        expected.put("action","echo");
        expected.put("parameters", (new JSONObject()).put("direction", "E"));

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("echo",dec.getAction());
        assertEquals(Compass.EAST, dec.getDir());
    }
    @Test
    void testScanningStateNotOcean(){
        GridSearcher gs = new GridSearcher(Compass.EAST, Compass.EAST);
        gs.setStatePublic(gs.newScanningState());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        JSONArray creekArray = new JSONArray();
        JSONArray siteArray = new JSONArray();
        JSONArray biomeArray = new JSONArray();
        biomeArray.put("NOT OCEAN");

        job.put("creeks",creekArray);
        job.put("sites",siteArray);
        job.put("biomes",biomeArray);

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("fly",dec.getAction());
    }
}
