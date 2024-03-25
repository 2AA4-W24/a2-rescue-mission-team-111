package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.json.JSONObject;

public class IslandArriver implements Finder, Arriver {

    private ArriverState currentState = new FlyingArriver();
    private Information currentInfo;
    private Invoker invoker;
    private boolean echoingRight = true;
    private Compass initialDir;
    private Compass newDir;
    private boolean findingDone = false;
    private boolean arrivingDone = false;

    //Initial direction of drone is passed in for IslandArriver algorithm to work
    public IslandArriver(Compass direction) {
        this.initialDir = direction; 
    }

    //Sets next state in the state pattern
    public void setState(ArriverState state) {
        currentState = state;
    }

    //Receives information from drone about the information of the previous decision
    public void updateInfo(Information info) {
        currentInfo = info;
    }

    @Override
    public Decision find() {
        JSONObject extras = currentInfo.getExtra();
        Decision decision = currentState.handle(this);
        //If our finding is done, take whether we are echoing right or left of current direction as the new direction
        if (findingDone) {
            if (echoingRight) {
                newDir = initialDir.right();
            } else {
                newDir = initialDir.left();
            }


            invoker = new Invoker();

            //Add commands of a big turn into the invoker, but if we're at the island, just turn
            if (extras.getInt("range") == 0) {
                invoker.addCommand(new TurningCommand(newDir)); 
            } else {
                invoker.addCommand(new TurningCommand(newDir));
                invoker.addCommand(new TurningCommand(initialDir.opposite()));
                invoker.addCommand(new TurningCommand(newDir));
                invoker.addCommand(new EchoingCommand(newDir));
                invoker.addCommand(new FlyingCommand());
            }
        }
        return decision;
    }

    //This is the start of the finding state pattern
    public interface ArriverState {
        public Decision handle(IslandArriver arriver);
    }

    //If we just flew, echo right
    public class FlyingArriver implements ArriverState {
        @Override
        public Decision handle(IslandArriver arriver) {
            Decision decision;
    
            arriver.setState(new EchoingArriver());
            Compass echo = arriver.initialDir;
            Compass echoingDir = echo.right();
            decision = new Decision("echo", echoingDir);
    
            return decision;
        }
    
    }

    //If we found ground, we're done the finding phase
    //Otherwise, echo left if you were echoing right, and fly if you were echoing left
    public class EchoingArriver implements ArriverState {
        @Override
        public Decision handle(IslandArriver arriver) {
            Decision decision;
            Information I = arriver.currentInfo;
            JSONObject extras = I.getExtra();
    
            if (extras.get("found").equals("GROUND")) {
                arriver.findingDone = true;
                decision = new Decision("fly");
            } else {
                if (arriver.echoingRight) {
                    arriver.setState(new EchoingArriver());
                    Compass echo = arriver.initialDir;
                    Compass echoingDir = echo.left();
                    decision = new Decision("echo", echoingDir);
                } else {
                    arriver.setState(new FlyingArriver());
                    decision = new Decision("fly");
                }
                arriver.echoingRight = !(arriver.echoingRight);
            }
            
            return decision;
        }
    }

    @Override
    public Decision moveTo() {
        JSONObject extras = currentInfo.getExtra();
        //Once we reach the echoing forward command to find how far away the island is, add that many flying commands to the invoker
        //If the island is right in front, just add one flying command
        if (extras.has("range")) {
            int range = extras.getInt("range");
            if (range == 0) {
                invoker.addCommand(new FlyingCommand());
            } else {
                for (int i = 0; i<range; i++) {
                    invoker.addCommand(new FlyingCommand());
                }
            }
        }

        //Execute the invoker's commands
        return invoker.executeCommands();
    }

    @Override
    public boolean findingIsDone() {
        return findingDone;
    }

    @Override
    public boolean arrivingIsDone() {
        return arrivingDone;
    }


    public interface Command {
        public Decision execute();
    }

    //Turn towards the new direction
    public class TurningCommand implements Command {
        private Compass turningDir;
    
        public TurningCommand(Compass newDir) {
            this.turningDir = newDir;
        }
    
        @Override
        public Decision execute() {
            Decision decision;
            decision = new Decision("heading", turningDir);
    
            return decision;
        }
    
    }    

    //Fly ahead
    public class FlyingCommand implements Command {
        @Override
        public Decision execute() {
            Decision decision;
    
            decision = new Decision("fly");
            return decision;
        }
    }

    //Echo forwards
    public class EchoingCommand implements Command {
        private Compass echoingDir;
    
        public EchoingCommand(Compass newDir) {
            echoingDir = newDir;
        }
    
        @Override
        public Decision execute() {
            Decision decision;
    
            decision = new Decision("echo", echoingDir);
    
            return decision;
        }
    
    }

    public class Invoker {
        private Queue<Command> commands = new LinkedList<>();
    
        public void addCommand(Command command) { //To add a command, add it to the back
            commands.offer(command);
        }
    
        public Decision executeCommands() {
            //If we only have one command left, we've arrived
            if (commands.size() == 1) {
                arrivingDone = true;
            } 
            Command command = commands.poll(); //Remove the first command (FIFO)
            return command.execute();
        }
    }
}
