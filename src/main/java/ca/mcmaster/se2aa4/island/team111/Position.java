package ca.mcmaster.se2aa4.island.team111;

public class Position {

    private int x;
    private int y;

    public Position(int x_position, int y_position) {
        this.x = x_position;
        this.y = y_position;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //Change position depending on turn directions
    public Position changePositionTurn(Compass old_dir, Compass direction) {
        int x_position = this.getX();
        int y_position = this.getY();
        int new_x;
        int new_y;
        switch(direction) {
            case NORTH: 
                if (old_dir == Compass.EAST) {
                    new_y = y_position + 1;
                    new_x = x_position + 1;
                    return new Position(new_x, new_y);
                } else {
                    new_y = y_position + 1;
                    new_x = x_position - 1;
                    return new Position(new_x, new_y);
                } 
            case WEST: 
                if (old_dir == Compass.NORTH) {
                    new_y = y_position + 1;
                    new_x = x_position - 1;
                    return new Position(new_x, new_y);
                } else {
                    new_y = y_position - 1;
                    new_x = x_position - 1;
                    return new Position(new_x, new_y);
                }
            case EAST: 
                if (old_dir == Compass.NORTH) {
                    new_y = y_position + 1;
                    new_x = x_position + 1;
                    return new Position(new_x, new_y);
                } else {
                    new_y = y_position - 1;
                    new_x = x_position + 1;
                    return new Position(new_x, new_y);
                }
            case SOUTH: 
                if (old_dir == Compass.EAST) {
                    new_y = y_position - 1;
                    new_x = x_position + 1;
                    return new Position(new_x, new_y);
                } else {
                    new_y = y_position - 1;
                    new_x = x_position - 1;
                    return new Position(new_x, new_y);
                }
            default: return this;
        }
    }
    
    //Change position if drone flew
    public Position changePositionFly(Compass direction) {
        int x_position = this.getX();
        int y_position = this.getY();
        switch(direction) {
            case NORTH:
            y_position = y_position - 1;
             return new Position(x_position, y_position);
            case WEST: 
            x_position = x_position - 1;
            return new Position(x_position, y_position);
            case EAST: 
            x_position = x_position + 1;
            return new Position(x_position, y_position);
            case SOUTH: 
            y_position = y_position + 1;
            return new Position(x_position, y_position);
            default: return this;
        }
    }
}  