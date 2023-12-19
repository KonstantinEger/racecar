package de.userk.consys.ev3;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import de.userk.log.Logger;
import lejos.robotics.RegulatedMotor;

public class EV3Driver implements Driver {
    private static final Logger log = Logger.forClass(EV3Driver.class);
    private final RegulatedMotor motor;
    private final int speed;

    public EV3Driver(RegulatedMotor motor, int speed) {
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
