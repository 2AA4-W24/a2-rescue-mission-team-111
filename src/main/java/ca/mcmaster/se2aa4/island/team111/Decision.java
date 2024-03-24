package ca.mcmaster.se2aa4.island.team111;

public class Decision {
    private String action;
    private Compass direction = Compass.NORTH;

    public Decision(String theAction) {
        this.action = theAction;
    }

    public Decision(String theAction, Compass dir) {
        this.action = theAction;
        this.direction = dir;
    }

    public String getAction() {
        return this.action;
    }

    public Compass getDir() {
        return this.direction;
    }

}
