package de.userk.consys.ev3;

import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import de.userk.log.Logger;
import lejos.robotics.RegulatedMotor;

public class EV3Steering implements Steering {
    public static final Logger log = Logger.forClass(EV3Steering.class);
    public final RegulatedMotor motor;
    private final int angle;
    private SteerCmd lastCmd = SteerCmd.STRAIGHT;

    public EV3Steering(RegulatedMotor motor, int angle) {
        this.motor = motor;
        this.angle = angle;
    }

	@Override
	public void handle(SteerCmd cmd) {
        int rotateAngle = 0;

        if (cmd.equals(SteerCmd.LEFT)) {
            if (lastCmd.equals(SteerCmd.STRAIGHT)) {
                rotateAngle = -angle;
            }
            if (lastCmd.equals(SteerCmd.RIGHT)) {
                rotateAngle = -angle * 2;
            }
        }
        if (cmd.equals(SteerCmd.STRAIGHT)) {
            if (lastCmd.equals(SteerCmd.LEFT)) {
                rotateAngle = angle;
            }
            if (lastCmd.equals(SteerCmd.RIGHT)) {
                rotateAngle = -angle;
            }
        }
        if (cmd.equals(SteerCmd.RIGHT)) {
            if (lastCmd.equals(SteerCmd.STRAIGHT)) {
                rotateAngle = angle;
            }
            if (lastCmd.equals(SteerCmd.LEFT)) {
                rotateAngle = angle * 2;
            }
        }

        log.debug("rotating %d°", rotateAngle);
        motor.rotate(rotateAngle);

        lastCmd = cmd;
	}

}

