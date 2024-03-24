package ca.mcmaster.se2aa4.island.team111;

public interface CreekSearchable extends Searchable {
    void updateInfo(Information info, Position pos);
    String giveClosestCreek();
}
