package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BatteryTest {

    @Test
    void checkLow() {
        Battery bat = new Battery(20);
        assertEquals(true,bat.isLow());
        Battery bat2 = new Battery(1000);
        assertEquals(false,bat2.isLow());
    }

    @Test
    void depleteTest(){
        Battery bat = new Battery(10);
        bat.depleteCharge(5);
        assertEquals(5,bat.getCharge());
    }
}
