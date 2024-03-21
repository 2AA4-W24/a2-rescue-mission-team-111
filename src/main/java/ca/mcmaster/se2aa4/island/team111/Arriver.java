package ca.mcmaster.se2aa4.island.team111;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Arriver {

    private final Logger logger = LogManager.getLogger();

    private ArriverState currentState = new FlyingArriver();
    private Information currentInfo;
    private Invoker invoker;
    private boolean echoingRight = true;
    private Compass initialDir;
    private Compass newDir;
    private boolean findingDone = false;
    private boolean arrivingDone = false;
    private String magicWord = "action";

    public Arriver(Compass direction) {
        this.initialDir = direction;
    }

    public void setState(ArriverState state) {
        currentState = state;
    }

    public JSONObject findIsland(Information info) {
        currentInfo = info;
        JSONObject decision = currentState.handle(this);
        if (findingDone) {
            if (echoingRight) {
                newDir = initialDir.right();
            } else {
                newDir = initialDir.left();
            }

            invoker = new Invoker();
            invoker.addCommand(new TurningCommand(newDir));
            logger.info(newDir);
            invoker.addCommand(new TurningCommand(initialDir.opposite()));
            logger.info(initialDir.opposite());
            invoker.addCommand(new TurningCommand(newDir));
            invoker.addCommand(new EchoingCommand(newDir));
            invoker.addCommand(new FlyingCommand());
        }
        return decision;
    }


    public interface ArriverState {
        public JSONObject handle(Arriver arriver);
    }

    public class FlyingArriver implements ArriverState {
        @Override
        public JSONObject handle(Arriver arriver) {
            JSONObject decision = new JSONObject();
    
            arriver.setState(new EchoingArriver());
            Compass echo = arriver.initialDir;
            Compass echoingDir = echo.right();
            decision.put("action", "echo");
            decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
    
            return decision;
        }
    
    }

    public class EchoingArriver implements ArriverState {
        @Override
        public JSONObject handle(Arriver arriver) {
            JSONObject decision = new JSONObject();
            Information I = arriver.currentInfo;
            JSONObject extras = I.getExtra();
    
            if (extras.get("found").equals("GROUND")) {
                arriver.findingDone = true;
                decision.put("action", "fly");
            } else {
                if (arriver.echoingRight) {
                    arriver.setState(new EchoingArriver());
                    Compass echo = arriver.initialDir;
                    Compass echoingDir = echo.left();
                    decision.put("action", "echo");
                    decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
                } else {
                    arriver.setState(new FlyingArriver());
                    decision.put("action", "fly");
                }
                arriver.echoingRight = !(arriver.echoingRight);
            }
            
            return decision;
        }
    }




    public JSONObject moveToIsland(JSONObject extras) {
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

    public boolean findingIsDone() {
        return findingDone;
    }

    public boolean arrivingIsDone() {
        return arrivingDone;
    }


    public interface Command {
        public JSONObject execute();
    }

    public class TurningCommand implements Command {
        private Compass turningDir;
    
        public TurningCommand(Compass newDir) {
            this.turningDir = newDir;
        }
    
        @Override
        public JSONObject execute() {
            JSONObject decision = new JSONObject();
    
            decision.put(magicWord, "heading");
            decision.put("parameters", (new JSONObject()).put("direction", turningDir.CtoS()));
    
            return decision;
        }
    
    }    

    public class FlyingCommand implements Command {
        @Override
        public JSONObject execute() {
            JSONObject decision = new JSONObject();
            decision.put(magicWord, "fly");
            return decision;
        }
    }

    public class EchoingCommand implements Command {
        private Compass echoingDir;
    
        public EchoingCommand(Compass newDir) {
            echoingDir = newDir;
        }
    
        @Override
        public JSONObject execute() {
            JSONObject decision = new JSONObject();
    
            decision.put(magicWord, "echo");
            decision.put("parameters", (new JSONObject()).put("direction", echoingDir.CtoS()));
    
            return decision;
        }
    
    }

    public class Invoker {
        private Queue<Command> commands = new LinkedList<>();
    
        public void addCommand(Command command) {
            commands.offer(command);
        }
    
        public JSONObject executeCommands() {
            if (commands.size() == 1) {
                arrivingDone = true;
            }
            Command command = commands.poll();
            return command.execute();
        }
    }
}
