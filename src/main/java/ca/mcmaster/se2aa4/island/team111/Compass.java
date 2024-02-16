package ca.mcmaster.se2aa4.island.team111;

public interface Compass {
    enum Turn{L,R,F};
    default String headingRotate(String heading, Turn turn){
        String newHeading = heading;
        switch(turn){
            case L:
                switch(heading){
                    case "N":newHeading = "W"; break;
                    case "W":newHeading = "S"; break;
                    case "S":newHeading = "E"; break;
                    case "E":newHeading = "N"; break;
                    default: break;
                }
            case R:
                switch(heading){
                    case "N":newHeading = "E"; break;
                    case "W":newHeading = "N"; break;
                    case "S":newHeading = "W"; break;
                    case "E":newHeading = "S"; break;
                    default: break;
                }
            default: break;
        }
        return newHeading;
    }
}
