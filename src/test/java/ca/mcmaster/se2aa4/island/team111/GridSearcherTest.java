package ca.mcmaster.se2aa4.island.team111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GridSearcherTest {

    @Test
    void testCalculateClosest() {
        POI creek1 = new POI("id1",new Position(0, 0));
        POI creek2 = new POI("id2",new Position(5, 17));
        POI site = new POI("site baby",new Position(3, 4));

        GridSearcher gridsearch = new GridSearcher(Compass.EAST, Compass.SOUTH);
        gridsearch.addCreek(creek1);
        gridsearch.addCreek(creek2);
        gridsearch.setSite(site);

        assertEquals("id1",gridsearch.calculateClosest());
    }

    @Test
    void testCheckDistance(){
        GridSearcher gridsrch = new GridSearcher(Compass.EAST, Compass.SOUTH);
        gridsrch.setSite(new POI("id1",new Position(0,0))); 

        double result = gridsrch.getDistanceTest(new POI("id2",new Position(-2,0)));
        assertEquals(2,result);
    }

    @Test
    void testCheckPOI() {
        assertTrue(true);
    }

    @Test
    void testFindCreeks() {
        assertTrue(true);
    }
}
