package de.userk.consys.nxt;

import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import de.userk.log.Logger;
import lejos.nxt.NXTRegulatedMotor;

public class NXTSteering implements Steering {
    public static final Logger log = Logger.forClass(NXTSteering.class);
    public final NXTRegulatedMotor motor;
    private final int angle;

    public NXTSteering(NXTRegulatedMotor motor, int angle) {
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
