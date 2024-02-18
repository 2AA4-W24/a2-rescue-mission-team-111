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
}
