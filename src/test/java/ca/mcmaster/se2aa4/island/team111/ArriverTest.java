package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team111.Arriver.*;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;

class ArriverTest {

    private Arriver arriver;

    @BeforeEach
    void init(){
        arriver = new Arriver(Compass.EAST);
    }
    @Test
    void TurningCommandTest() {
        Arriver.TurningCommand tc = arriver.new TurningCommand(Compass.EAST);
        assertEquals("heading", tc.execute().getAction());
    }
    @Test
    void FlyingCommandTest() {
        Arriver.FlyingCommand fc = arriver.new FlyingCommand();
        assertEquals("fly", fc.execute().getAction());
    }
    @Test
    void EchoingCommand() {
        Arriver.EchoingCommand ec = arriver.new EchoingCommand(Compass.EAST);
        assertEquals("echo", ec.execute().getAction());
    }
    @Test
    void InvokerExecuteTest() {
        Arriver.TurningCommand tc = arriver.new TurningCommand(Compass.EAST);
        Arriver.Invoker inv = arriver.new Invoker();
        inv.addCommand(tc);
        assertEquals("heading", inv.executeCommands().getAction());
    }

    @Test
    void findIslandInitialTest(){
        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("range",5);

        Information info = new Information(15, job);

        assertEquals("echo", arriver.findIsland(info).getAction());
    }

    @Test
    void echoingArriverFoundGroundTest(){
        Arriver.EchoingArriver ea = arriver.new EchoingArriver();

        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("found","GROUND");
        Information info = new Information(15, job);

        arriver.findIsland(info); //Deposit current fake info into arriver

        assertEquals("fly", ea.handle(arriver).getAction());
        assertTrue(arriver.findingIsDone());
    }
    @Test
    void echoingArriverFoundNothingTest(){
        Arriver.EchoingArriver ea = arriver.new EchoingArriver();

        JSONObject job = new JSONObject();
        job.put("extras","");
        job.put("found","WHATEVER");
        Information info = new Information(15, job);

        arriver.findIsland(info); //Deposit current fake info into arriver

        assertEquals("echo", ea.handle(arriver).getAction());
        assertFalse(arriver.findingIsDone());
    }
}
