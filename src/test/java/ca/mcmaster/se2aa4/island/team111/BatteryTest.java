package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BatteryTest {

    @Test
    public void checkLow() {
        Battery bat = new Battery(500);
        bat.depleteCharge(500);
        assertTrue(bat.isLow());
    }
}
