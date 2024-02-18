package ca.mcmaster.se2aa4.island.team111;

public interface DroneInfo {
    boolean lowBattery();
    Compass currentDirection(); //changes to Direction type when Direction enum implemented
}
