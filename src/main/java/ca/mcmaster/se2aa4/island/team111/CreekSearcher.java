package ca.mcmaster.se2aa4.island.team111;

public interface CreekSearcher extends Searcher {
    void updateInfo(Information info, Position pos);
    String giveClosestCreek();
}
