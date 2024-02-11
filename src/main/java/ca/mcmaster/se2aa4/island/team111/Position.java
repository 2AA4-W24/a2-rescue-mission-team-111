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

    public void increaseY(int y) {
        this.y = y + 1;
    }

    public void increaseX(int x) {
        this.x = x + 1;
    }

    public void decreaseY(int y) {
        this.y = y - 1;
    }

    public void decreaseX(int x) {
        this.x = x - 1;
    }

}  