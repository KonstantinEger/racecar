package de.userk.consys.sil;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import de.userk.log.Logger;

public class SILDriver implements Driver {
    private static final Logger log = Logger.forClass(SILDriver.class);
    private final SILSensorActorFactory originatingFactory;

    public SILDriver(SILSensorActorFactory originatingFactory) {
        this.originatingFactory = originatingFactory;
    }

    @Override
    public void handle(DriverCmd cmd) {
        DriverCmd expected = originatingFactory.getCurrenTimeStep().driverCmd;

        if (cmd.equals(expected)) {
            log.debug("assertion matches");
        } else {
            log.error("assertion missmatch %s (is) != %s (expected)", cmd, expected);
        }
    }
}
