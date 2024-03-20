package ca.mcmaster.se2aa4.island.team111;

public class Position {

    private int x_position;
    private int y_position;

    public Position(int x_position, int y_position) {
        this.x_position = x_position;
        this.y_position = y_position;
    }

    public int getX() {
        return x_position;
    }

    public int getY() {
        return y_position;
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
            case NORTH: return new Position(x_position, y_position += 1);
            case WEST: return new Position(x_position -= 1, y_position);
            case EAST: return new Position(x_position += 1, y_position);
            case SOUTH: return new Position(x_position, y_position -= 1);
            default: return this;
        }
    }
}  