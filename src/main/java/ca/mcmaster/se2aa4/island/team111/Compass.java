package ca.mcmaster.se2aa4.island.team111;

public enum Compass 
{
    NORTH,
    WEST,
    SOUTH,
    EAST;

    public Compass turnRight(Compass heading){
        switch(heading){
            case NORTH: return Compass.EAST;
            case WEST: return Compass.NORTH;
            case SOUTH: return Compass.WEST;
            case EAST: return Compass.SOUTH;
            default: return heading;
        }
    }
    public Compass turnLeft(Compass heading){
        switch(heading){
            case NORTH: return Compass.WEST;
            case WEST: return Compass.SOUTH;
            case SOUTH: return Compass.EAST;
            case EAST: return Compass.NORTH;
            default: return heading;
        }
    }

    public String CtoS(Compass heading){
        switch(heading){
            case NORTH: return "N";
            case WEST: return "W";
            case SOUTH: return "S";
            case EAST: return "E";
            default: return "";
        }
    }
    public Compass StoC(String heading){
        switch(heading){
            case "N": return Compass.NORTH;
            case "W": return Compass.WEST;
            case "S": return Compass.SOUTH;
            case "E": return Compass.EAST;
            default: return Compass.NORTH;
        }
    }
}
