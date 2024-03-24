package ca.mcmaster.se2aa4.island.team111;

public class Position {

    private int x;
    private int y;

    public Position(int xPosition, int yPosition) {
        this.x = xPosition;
        this.y = yPosition;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //Change position depending on turn directions
    public Position changePosition(Compass old_dir, Compass direction) {
        int xPosition = this.getX();
        int yPosition = this.getY();
        switch(direction) {
            case NORTH: 
                if (old_dir == Compass.EAST) {
                    xPosition = xPosition + 1;
                    yPosition = yPosition + 1;
                    return new Position(xPosition, yPosition);
                } else {
                    xPosition = xPosition - 1;
                    yPosition = yPosition + 1;
                    return new Position(xPosition, yPosition);
                } 
            case WEST: 
                if (old_dir == Compass.NORTH) {
                    xPosition = xPosition - 1;
                    yPosition = yPosition + 1;
                    return new Position(xPosition, yPosition);
                } else {
                    xPosition = xPosition - 1;
                    yPosition = yPosition - 1;
                    return new Position(xPosition, yPosition);
                }
            case EAST: 
                if (old_dir == Compass.NORTH) {
                    xPosition = xPosition + 1;
                    yPosition = yPosition + 1;
                    return new Position(xPosition, yPosition);
                } else {
                    xPosition = xPosition + 1;
                    yPosition = yPosition - 1;
                    return new Position(xPosition, yPosition);
                }
            case SOUTH: 
                if (old_dir == Compass.EAST) {
                    xPosition = xPosition + 1;
                    yPosition = yPosition - 1;
                    return new Position(xPosition, yPosition);
                } else {
                    xPosition = xPosition - 1;
                    yPosition = yPosition - 1;
                    return new Position(xPosition, yPosition);
                }
            default: return this;
        }
    }
    
    //Change position if drone flew
    public Position changePosition(Compass direction) {
        int xPosition = this.getX();
        int yPosition = this.getY();
        switch(direction) {
            case NORTH:
            yPosition = yPosition + 1;
             return new Position(xPosition, yPosition);
            case WEST: 
            xPosition = xPosition - 1;
            return new Position(xPosition, yPosition);
            case EAST: 
            xPosition = xPosition + 1;
            return new Position(xPosition, yPosition);
            case SOUTH: 
            yPosition = yPosition - 1;
            return new Position(xPosition, yPosition);
            default: return this;
        }
    }
}  