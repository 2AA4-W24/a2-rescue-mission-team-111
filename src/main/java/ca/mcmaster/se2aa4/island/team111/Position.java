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

    public void changePosition(Compass direction) {
        switch(direction) {
            case NORTH: y++; break;
            case WEST: x--; break;
            case EAST: x++; break;
            case SOUTH: y--; break;
        }
    }
}  