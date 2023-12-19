package de.userk.consys.ev3;

import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import de.userk.log.Logger;
import lejos.robotics.RegulatedMotor;

public class EV3Steering implements Steering {
    public static final Logger log = Logger.forClass(EV3Steering.class);
    public final RegulatedMotor motor;
    private final int angle;

    public EV3Steering(RegulatedMotor motor, int angle) {
        this.motor = motor;
        this.angle = angle;
    }

	@Override
	public void handle(SteerCmd cmd) {
        // FIXME: does not work because doesn't take current angle into account.

        if (cmd.equals(SteerCmd.LEFT)) {
            log.debug("rotating %d°", angle);
            motor.rotate(angle);
        } else if (cmd.equals(SteerCmd.RIGHT)) {
            log.debug("rotating -%d°", angle);
            motor.rotate(-angle);
        }
	}

}

