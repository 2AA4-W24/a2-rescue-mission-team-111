package ca.mcmaster.se2aa4.island.team111;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class AreaMapTest {

    // Try to find the closest creek where no creeks were found
    // @Test 
    // void calculateClosestNoCreeks(){
    //     AreaMap calc = new AreaMap(new ArrayList<POI>(), new POI("site", new Position(0, 0)));
    //     assertEquals("No creeks found", calc.calculateClosest());
    // }

    // // Get the correct closest creek 
    // @Test
    // void calculateClosestWithCreeks(){
    //     List<POI> creeks = new ArrayList<POI>();
    //     creeks.add(new POI("creek1", new Position(50, 69)));
    //     creeks.add(new POI("creek2", new Position(200, 80)));
    //     creeks.add(new POI("creek3", new Position(1, 0)));
    //     AreaMap calc = new AreaMap(creeks, new POI("site", new Position(0, 0)));
        
    //     assertEquals("creek3", calc.calculateClosest());
    // }

    // // Crosscheck with pythagoras if the distance calculation is correct
    // @Test
    // void testCheckDistance(){
    //     AreaMap calc = new AreaMap(null, new POI("site", new Position(0, 0)));

    //     Position pos = new Position(2, 3);
    //     double expected = Math.sqrt((2*2)+(3*3));

    //     assertEquals(expected, calc.getDistance(new POI("creek", pos)));
        
    // }

}
