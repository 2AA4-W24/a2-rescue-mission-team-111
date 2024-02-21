package ca.mcmaster.se2aa4.island.team111;

public enum DroneState {
    MEASURING, FINDING, ARRIVING, SEARCHING;

    public DroneState nextState() {
        switch (this) {
            case MEASURING: return FINDING;
            case FINDING: return ARRIVING;
            case ARRIVING: return SEARCHING;
            default: return SEARCHING;
        }
    }
}