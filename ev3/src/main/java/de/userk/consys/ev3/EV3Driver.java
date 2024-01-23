package de.userk.consys.ev3;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import de.userk.log.Logger;
import lejos.robotics.RegulatedMotor;

public class EV3Driver implements Driver {
    private static final Logger log = Logger.forClass(EV3Driver.class);
    private final RegulatedMotor motor1;
    private final RegulatedMotor motor2;
    private final int speed;
    private DriverCmd lastCmd = null;

    public EV3Driver(RegulatedMotor motor1, RegulatedMotor motor2, int speed) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.speed = speed;
    }

    @Override
    public void handle(DriverCmd cmd) {
        if (cmd.equals(lastCmd)) {
            return;
        }

        if (cmd.equals(DriverCmd.STOP)) {
            log.debug("stopping motor");
            motor1.setSpeed(0);
            motor2.setSpeed(0);
            return;
        }

        motor1.setSpeed(speed);
        motor2.setSpeed(speed);

        if (cmd.equals(DriverCmd.FORWARD)) {
            log.debug("setting speed positive");
            motor1.backward();
            motor2.backward();
        } else {
            log.debug("setting speed negative");
            motor1.forward();
            motor2.forward();
        }
        lastCmd = cmd;
    }
}
