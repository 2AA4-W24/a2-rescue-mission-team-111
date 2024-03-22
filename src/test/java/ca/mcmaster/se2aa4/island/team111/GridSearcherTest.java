package ca.mcmaster.se2aa4.island.team111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridSearcherTest {
    
    private GridSearcher gs;

    @BeforeEach
    void init(){
        gs = new GridSearcher(Compass.EAST, Compass.SOUTH);
    }

    @Test 
    void calculateClosestNoCreeks(){
        assertEquals("No creeks found", gs.calculateClosest());
    }

    @Test
    void calculateClosestWithCreeks(){
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
        double result = gs.getDistanceTest(new POI("id1",new Position(-2,0)));
        assertEquals(2,result);
    }

    @Test
    void testCheckingDone() {
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
        gs.setStatePublic(gs.newFirstTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("echo",dec.getAction());
        assertEquals(Compass.NORTH, dec.getDir());
    }
    @Test
    void testSecondTurn(){
        gs.setStatePublic(gs.newSecondTurn());

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
    void testThirdTurn(){
        gs.setStatePublic(gs.newThirdTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        Decision dec = gs.performSearch(info,pos);
        assertEquals("heading",dec.getAction());
        assertEquals(Compass.NORTH, dec.getDir());
    }
    @Test
    void testFourthTurn(){
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
        assertEquals(Compass.SOUTH, dec.getDir());
    }
    @Test
    void testScanningStateNotOcean(){
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

    @Test
    void testFlyWideTurnBigRange(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",100);

        Information info = new Information(15, job);
        Position pos = new Position(0, 0);

        //Simulate echoing forward out of range to set range in GridSearcher.java
        gs.performSearch(info, pos);

        
        gs.setStatePublic(gs.newFlyWideTurn());
        Decision dec = gs.performSearch(info,pos);
        assertEquals("fly",dec.getAction());
    }
    @Test
    void testFlyWideTurnMediumRange(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",3);

        Information info = new Information(15, job);
        Position pos = new Position(0, 0);

        //Simulate echoing forward out of range to set range in GridSearcher.java
        gs.performSearch(info, pos);

        
        gs.setStatePublic(gs.newFlyWideTurn());
        Decision dec = gs.performSearch(info,pos);
        assertEquals("heading",dec.getAction());
    }
    @Test
    void testFlyWideTurnSmallRange(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",0);

        Information info = new Information(15, job);
        Position pos = new Position(0, 0);

        //Simulate echoing forward out of range to set range in GridSearcher.java
        gs.performSearch(info, pos);

        
        gs.setStatePublic(gs.newFlyWideTurn());
        Decision dec = gs.performSearch(info,pos);
        assertEquals("heading",dec.getAction());
    }
}
