package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

public class AreaMap {
    private Map<List<String>, Position> biomes = new HashMap<>();
    private List<POI> creeks;
    private POI site;
    
    public AreaMap(Map<List<String>, Position> biomeMap, List<POI> creekList, POI emergencySite) {
        this.biomes = biomeMap;
        this.creeks = creekList;
        this.site = emergencySite;
    }

    public Map<List<String>, Position> getBiomes() {
        return biomes;
    }

    public String calculateClosest() {
        if (creeks.isEmpty()) {
            return "No creeks found";
        } else if (site.getID().equals("null")) {
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

    //Uses pythagorean mathematics to check distance
    public double getDistance(POI creek) {
        double x = Math.abs(site.getXvalue()-creek.getXvalue());
        double y = Math.abs(site.getYvalue()-creek.getYvalue());
        return Math.sqrt((x*x) + (y*y));
    }
}
