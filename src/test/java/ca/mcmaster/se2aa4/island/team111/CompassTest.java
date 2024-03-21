package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CompassTest {

    @Test
    void leftTurnTest() {
        Compass c = Compass.EAST;
        assertEquals(Compass.NORTH,c.left());
        c = Compass.WEST;
        assertEquals(Compass.SOUTH,c.left());
        c = Compass.NORTH;
        assertEquals(Compass.WEST,c.left());
        c = Compass.SOUTH;
        assertEquals(Compass.EAST,c.left());
    }

    @Test
    void rightTurnTest() {
        Compass c = Compass.EAST;
        assertEquals(Compass.SOUTH,c.right());
        c = Compass.WEST;
        assertEquals(Compass.NORTH,c.right());
        c = Compass.NORTH;
        assertEquals(Compass.EAST,c.right());
        c = Compass.SOUTH;
        assertEquals(Compass.WEST,c.right());
    }

    @Test
    void oppositeTest() {
        Compass c = Compass.EAST;
        assertEquals(Compass.WEST,c.opposite());
        c = Compass.WEST;
        assertEquals(Compass.EAST,c.opposite());
        c = Compass.NORTH;
        assertEquals(Compass.SOUTH,c.opposite());
        c = Compass.SOUTH;
        assertEquals(Compass.NORTH,c.opposite());
    }

    @Test
    void CtoSTest(){
        Compass c = Compass.NORTH;
        assertEquals("N",c.CtoS());
        c = Compass.WEST;
        assertEquals("W",c.CtoS());
        c = Compass.SOUTH;
        assertEquals("S",c.CtoS());
        c = Compass.EAST;
        assertEquals("E",c.CtoS());
    }

    @Test
    void StoCTest(){
        Compass c = Compass.NORTH;
        String cs = "N";
        assertEquals(Compass.NORTH,c.StoC(cs));
        cs = "W";
        assertEquals(Compass.WEST,c.StoC(cs));
        cs = "S";
        assertEquals(Compass.SOUTH,c.StoC(cs));
        cs = "E";
        assertEquals(Compass.EAST,c.StoC(cs));
    }
}
