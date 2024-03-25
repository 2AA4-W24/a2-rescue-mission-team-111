package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

public class AreaMap {
    private Map<Position, List<String>> biomes = new HashMap<>();
    private List<POI> creeks;
    private POI site;

    //AreaMap contains map of positions of the map associated with their positions

    public AreaMap(Map<Position, List<String>> biomeMap, List<POI> creekList, POI emergencySite) {
        this.biomes = biomeMap;
        this.creeks = creekList;
        this.site = emergencySite;
    }

    //Useful for future rescue actions
    public Map<Position, List<String>> getBiomes() {
        return biomes;
    }

    //Calculates the closest creek to the site
    public String calculateClosestCreek() {
        if (creeks.isEmpty()) {
            return "No creeks found";
        } else if (site.getID().equals("null")) { //if site not found
            POI lastCreek = creeks.get(creeks.size()-1);
            return lastCreek.getID();
        }

        POI closestCreek = creeks.get(0);
        for (int i = 1; i<creeks.size(); i++) {
            POI thisCreek = creeks.get(i);
            if (getDistance(thisCreek) < getDistance(closestCreek)) {
                closestCreek = thisCreek;
            }
        }
        return closestCreek.getID();
    }

    //Uses euclidean distance 
    public double getDistance(POI creek) {
        double x = Math.abs(site.getXvalue()-creek.getXvalue());
        double y = Math.abs(site.getYvalue()-creek.getYvalue());
        return Math.sqrt((x*x) + (y*y));
    }
}
