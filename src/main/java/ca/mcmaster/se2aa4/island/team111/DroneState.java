package ca.mcmaster.se2aa4.island.team111;

public enum DroneState {
    FINDING, ARRIVING, SEARCHING, CALCULATING;

    public DroneState nextState(DroneState state) {
        switch (state) {
            case FINDING: return ARRIVING;
            case ARRIVING: return SEARCHING;
            case SEARCHING: return CALCULATING;
            default: return CALCULATING;
        }
    }
}
