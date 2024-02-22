package ca.mcmaster.se2aa4.island.team111;

public enum DroneState {
    FINDING, ARRIVING, SEARCHING;

    public DroneState nextState() {
        switch (this) {
            case FINDING: return ARRIVING;
            case ARRIVING: return SEARCHING;
            default: return SEARCHING;
        }
    }
}