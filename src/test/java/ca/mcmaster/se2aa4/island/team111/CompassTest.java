package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CompassTest {

    @Test
    public void leftTurn() {
        Compass c = Compass.EAST;
        assertEquals(c.left(),Compass.NORTH);
    }

    @Test
    public void CtoSNorthString(){
        Compass c = Compass.NORTH;
        assertEquals(c.CtoS(),"N");
    }
}
