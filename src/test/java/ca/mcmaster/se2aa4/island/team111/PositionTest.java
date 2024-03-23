package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class PositionTest {

    private Position pos = new Position(0, 0);

    @BeforeEach
    void init(){
        pos = new Position(0, 0);
    }

    // Check if forward changes the XY coordinates correctly
    @Test
    void ForwardTest() {
        assertEquals(1, pos.changePosition(Compass.NORTH).getY());
        assertEquals(-1, pos.changePosition(Compass.WEST).getX());
        assertEquals(-1, pos.changePosition(Compass.SOUTH).getY());
        assertEquals(1, pos.changePosition(Compass.EAST).getX());
    }

    // Same for the left/right turns of each Compass direction
    @Test
    void TurnLeftN() {
        Compass c = Compass.NORTH;
        assertEquals(-1, pos.changePosition(c, c.left()).getX());
        assertEquals(1, pos.changePosition(c, c.left()).getY());
    }
    @Test
    void TurnRightN() {
        Compass c = Compass.NORTH;
        assertEquals(1, pos.changePosition(c, c.right()).getX());
        assertEquals(1, pos.changePosition(c, c.right()).getY());
    }
    @Test
    void TurnLeftW() {
        Compass c = Compass.WEST;
        assertEquals(-1, pos.changePosition(c, c.left()).getX());
        assertEquals(-1, pos.changePosition(c, c.left()).getY());
    }
    @Test
    void TurnRightW() {
        Compass c = Compass.WEST;
        assertEquals(-1, pos.changePosition(c, c.right()).getX());
        assertEquals(1, pos.changePosition(c, c.right()).getY());
    }
    @Test
    void TurnLeftS() {
        Compass c = Compass.SOUTH;
        assertEquals(1, pos.changePosition(c, c.left()).getX());
        assertEquals(-1, pos.changePosition(c, c.left()).getY());
    }
    @Test
    void TurnRightS() {
        Compass c = Compass.SOUTH;
        assertEquals(-1, pos.changePosition(c, c.right()).getX());
        assertEquals(-1, pos.changePosition(c, c.right()).getY());
    }
    @Test
    void TurnLeftE() {
        Compass c = Compass.EAST;
        assertEquals(1, pos.changePosition(c, c.left()).getX());
        assertEquals(1, pos.changePosition(c, c.left()).getY());
    }
    @Test
    void TurnRightE() {
        Compass c = Compass.EAST;
        assertEquals(1, pos.changePosition(c, c.right()).getX());
        assertEquals(-1, pos.changePosition(c, c.right()).getY());
    }
}
