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

    public IslandArriver(Compass direction) {
        this.initialDir = direction;
    }

    public void setState(ArriverState state) {
        currentState = state;
    }

    public void updateInfo(Information info) {
        currentInfo = info;
    }

    @Override
    public Decision find() {
        JSONObject extras = currentInfo.getExtra();
        Decision decision = currentState.handle(this);
        if (findingDone) {
            if (echoingRight) {
                newDir = initialDir.right();
            } else {
                newDir = initialDir.left();
            }


            invoker = new Invoker();

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


    public interface ArriverState {
        public Decision handle(IslandArriver arriver);
    }

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

    public class FlyingCommand implements Command {
        @Override
        public Decision execute() {
            Decision decision;
    
            decision = new Decision("fly");
            return decision;
        }
    }

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
    
        public void addCommand(Command command) {
            commands.offer(command);
        }
    
        public Decision executeCommands() {
            if (commands.size() == 1) {
                arrivingDone = true;
            }
            Command command = commands.poll();
            return command.execute();
        }
    }
}
