package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BatteryTest {

    @Test
    void checkLow() {
        Position pos = new Position(5000, 0); //Some far away position
        Battery bat = new Battery(10);
        assertEquals(true,bat.isLow(pos));
    }

    @Test
    void depleteTest(){
        Battery bat = new Battery(10);
        bat.depleteCharge(5);
        assertEquals(5,bat.getCharge());
    }
}
