package de.userk.consys.nxt;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import de.userk.log.Logger;
import lejos.nxt.NXTRegulatedMotor;

public class NXTDriver implements Driver {
    private static final Logger log = Logger.forClass(NXTDriver.class);
    private final NXTRegulatedMotor motor;
    private final int speed;

    public NXTDriver(NXTRegulatedMotor motor, int speed) {
        this.motor = motor;
        this.speed = speed;
    }

    @Override
    public void handle(DriverCmd cmd) {
        if (cmd.equals(DriverCmd.STOP)) {
            log.debug("stopping motor");
            motor.setSpeed(0);
            return;
        }

        if (cmd.equals(DriverCmd.FORWARD)) {
            log.debug("setting speed positive");
            motor.setSpeed(speed);
        } else {
            log.debug("setting speed negative");
            motor.setSpeed(-speed);
        }
        motor.forward();
    }
}
