package de.userk.consys.sil;

import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import de.userk.log.Logger;

public class SILSteering implements Steering {
    private static final Logger log = Logger.forClass(SILSteering.class);
    private final SILSensorActorFactory originatingFactory;

    public SILSteering(SILSensorActorFactory originatingFactory) {
        this.originatingFactory = originatingFactory;
    }

    @Override
    public void handle(SteerCmd cmd) {
        SteerCmd expected = originatingFactory.getCurrenTimeStep().steerCmd;

        if (cmd.equals(expected)) {
            log.debug("assertion matches");
        } else {
            log.error("assertion missmatch %s (is) != %s (expected)", cmd, expected);
        }
    }
}
