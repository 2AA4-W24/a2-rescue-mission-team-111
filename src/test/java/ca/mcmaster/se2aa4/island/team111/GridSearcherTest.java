package ca.mcmaster.se2aa4.island.team111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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

    // Tests if drone will stop if it gets OUT_OF_RANGE during CheckingDone state
    @Test
    void testCheckingDoneOutOfRange() {
        gs.setStatePublic(gs.newCheckingDone());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("stop",dec.getAction());

    }

    // Tests if drone will continue flying during CheckingDrone state
    @Test
    void testCheckingDoneInRange(){
        gs.setStatePublic(gs.newCheckingDone());

        JSONObject job = new JSONObject();
        job.put("found","");
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("fly",dec.getAction());
    }

    // Tests if the EchoingForward state will continue flying in long range
    @Test
    void testEchoingForwardStateSafeRange(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("fly",dec.getAction());
    }

    // Tests if the EchoingForward state will change it's heading if flying in short range
    @Test
    void testEchoingForwardStateOutofRange(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",1);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("heading",dec.getAction());
        assertEquals(Compass.EAST,dec.getDir());
    }

    // Tests if the EchoingForward state will continue flying if GROUND has been scanned in front of it
    // Ensures skipping useless scans over ocean in between ground
    @Test
    void testEchoingForwardStateNormal(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","GROUND");
        job.put("range",5);

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("fly",dec.getAction());
    }

    // Edge case for FirstTurn state
    @Test
    void testFirstTurn(){
        gs.setStatePublic(gs.newFirstTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("scan",dec.getAction());
    }

    // Edge case for SecondTurn state
    @Test
    void testSecondTurn(){
        gs.setStatePublic(gs.newSecondTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("heading",dec.getAction());
        assertEquals(Compass.WEST, dec.getDir());
    }

    // Edge case for ThirdTurn state
    @Test
    void testThirdTurn(){
        gs.setStatePublic(gs.newThirdTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);
        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();

        assertEquals("heading",dec.getAction());
        assertEquals(Compass.NORTH, dec.getDir());
    }

    // Edge case for FourthTurn state
    @Test
    void testFourthTurn(){
        gs.setStatePublic(gs.newFourthTurn());

        JSONObject job = new JSONObject();
        job.put("found","");
        job.put("range",5);

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("scan",dec.getAction());
    }

    // Small simulation of the Scanning when creeks were found
    // Checks if the creeks were added to the memory of the drone
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

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        gs.performSearch();
        
        assertTrue(gs.creeksAmount() > 0);
    }

    // Simulation of the ScanningState finding a site
    // Checks if the site was updated during ScanningStates
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
        gs.updateInfo(info, pos);
        gs.performSearch();

        POI newsite = gs.giveSite();
        
        assertEquals("examplesite",newsite.getID());
        assertEquals(5, newsite.getXvalue());
        assertEquals(9, newsite.getYvalue());
    }

    // Small simulation of the scanning state finding the ocean
    // Expected to echo
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

        Information info = new Information(15, job);

        Position pos = new Position(0, 0);
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("echo",dec.getAction());
        assertEquals(Compass.SOUTH, dec.getDir());
    }

    // Small simulation of the scanning state finding something not ocean (ground)
    // Expected to fly
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
        gs.updateInfo(info, pos);
        Decision dec = gs.performSearch();
        assertEquals("fly",dec.getAction());
    }

    // Checks if FlyWideTurn will fly if the edge of the map is far
    @Test
    void testFlyWideTurnBigRange(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",100);

        Information info = new Information(15, job);
        Position pos = new Position(0, 0);

        //Simulate echoing forward out of range to set range in GridSearcher.java
        gs.updateInfo(info, pos);
        gs.performSearch();

        gs.setStatePublic(gs.newFlyWideTurn());
        Decision dec = gs.performSearch();
        assertEquals("fly",dec.getAction());
    }

    // Checks if FlyWideTurn will turn if the edge of the map is in medium range 
    @Test
    void testFlyWideTurnMediumRange(){
        gs.setStatePublic(gs.newEchoingForwardState());

        JSONObject job = new JSONObject();
        job.put("found","OUT_OF_RANGE");
        job.put("range",3);

        Information info = new Information(15, job);
        Position pos = new Position(0, 0);

        //Simulate echoing forward out of range to set range in GridSearcher.java
        gs.updateInfo(info, pos);
        gs.performSearch();

        
        gs.setStatePublic(gs.newFlyWideTurn());
        Decision dec = gs.performSearch();
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
        gs.updateInfo(info, pos);
        gs.performSearch();
        
        gs.setStatePublic(gs.newFlyWideTurn());
        Decision dec = gs.performSearch();
        assertEquals("heading",dec.getAction());
    }
}
