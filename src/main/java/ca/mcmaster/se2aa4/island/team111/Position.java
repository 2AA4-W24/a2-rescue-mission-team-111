package ca.mcmaster.se2aa4.island.team111;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Position changePositionTurn(Compass old_dir, Compass direction) {
        int x = this.getX();
        int y = this.getY();
        switch(direction) {
            case NORTH: 
                if (old_dir == Compass.EAST) {
                    return new Position(x += 1, y += 1);
                } else {
                    return new Position(x -= 1, y += 1);
                } 
            case WEST: 
                if (old_dir == Compass.NORTH) {
                    return new Position(x -= 1, y += 1);
                } else {
                    return new Position(x -= 1, y -= 1);
                }
            case EAST: 
                if (old_dir == Compass.NORTH) {
                    return new Position(x += 1, y += 1);
                } else {
                    return new Position(x += 1, y -= 1);
                }
            case SOUTH: 
                if (old_dir == Compass.EAST) {
                    return new Position(x += 1, y -= 1);
                } else {
                    return new Position(x -= 1, y -= 1);
                }
            default: return this;
        }
    }
    
    public Position changePositionFly(Compass direction) {
        int x = this.getX();
        int y = this.getY();
        switch(direction) {
            case NORTH: return new Position(x, y += 1);
            case WEST: return new Position(x -= 1, y);
            case EAST: return new Position(x += 1, y);
            case SOUTH: return new Position(x, y -= 1);
            default: return this;
        }
    }
}  