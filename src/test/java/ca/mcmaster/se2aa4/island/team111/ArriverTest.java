package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team111.IslandArriver.*;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;

class ArriverTest {

    private IslandArriver arriver;

    @BeforeEach
    void init(){
        arriver = new IslandArriver(Compass.EAST);
    }
    @Test
    void TurningCommandTest() {
        IslandArriver.TurningCommand tc = arriver.new TurningCommand(Compass.EAST);
        assertEquals("heading", tc.execute().getAction());
    }
    @Test
    void FlyingCommandTest() {
        IslandArriver.FlyingCommand fc = arriver.new FlyingCommand();
        assertEquals("fly", fc.execute().getAction());
    }
    @Test
    void EchoingCommand() {
        IslandArriver.EchoingCommand ec = arriver.new EchoingCommand(Compass.EAST);
        assertEquals("echo", ec.execute().getAction());
    }
    @Test
    void InvokerExecuteTest() {
        IslandArriver.TurningCommand tc = arriver.new TurningCommand(Compass.EAST);
        IslandArriver.Invoker inv = arriver.new Invoker();
        inv.addCommand(tc);
        assertEquals("heading", inv.executeCommands().getAction());
    }

    @Test
    void findIslandInitialTest(){
        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("range",5);

        Information info = new Information(15, job);

        arriver.updateInfo(info);
        assertEquals("echo", arriver.find().getAction());
    }

    @Test
    void echoingArriverFoundGroundTest(){
        IslandArriver.EchoingArriver ea = arriver.new EchoingArriver();

        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("found","GROUND");
        Information info = new Information(15, job);

        arriver.updateInfo(info);
        arriver.find(); //Deposit current fake info into arriver

        assertEquals("fly", ea.handle(arriver).getAction());
        assertTrue(arriver.findingIsDone());
    }
    @Test
    void echoingArriverFoundNothingTest(){
        IslandArriver.EchoingArriver ea = arriver.new EchoingArriver();

        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("found","WHATEVER");
        Information info = new Information(15, job);

        arriver.updateInfo(info);
        arriver.find(); //Deposit current fake info into arriver

        assertEquals("echo", ea.handle(arriver).getAction());
        assertFalse(arriver.findingIsDone());
    }
}
