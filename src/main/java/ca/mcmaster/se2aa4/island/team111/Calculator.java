package ca.mcmaster.se2aa4.island.team111;

import java.util.List;

public class Calculator {
    private List<POI> creeks;
    private POI site;
    
    public Calculator(List<POI> creekList, POI emergencySite) {
        this.creeks = creekList;
        this.site = emergencySite;
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

        public double getDistanceTest(POI creek){
        return getDistance(creek);
    }

    //Uses pythagorean mathematics to check distance
    private double getDistance(POI creek) {
        double x = Math.abs(site.getXvalue()-creek.getXvalue());
        double y = Math.abs(site.getYvalue()-creek.getYvalue());
        return Math.sqrt((x*x) + (y*y));
    }
}
