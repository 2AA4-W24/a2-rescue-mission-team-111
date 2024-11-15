package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;


class BatteryTest {

    private Battery bat;

    @BeforeEach
    void init(){
        bat = new Battery(10);
    }

    // Check if the battery will be considered "low" 5000 units away
    @Test
    void checkLow() {
        Position pos = new Position(5000, 0); //Some far away position 
        assertEquals(true,bat.isLow(pos));
    }

    // Check if depleting the battery is accurate
    @Test
    void depleteTest(){ 
        bat.depleteCharge(5);
        assertEquals(5,bat.getCharge());
    }
}
