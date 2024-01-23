package de.userk.consys.ev3;

import de.userk.consys.App;
import de.userk.consys.SensorActorFactory;
import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import de.userk.log.Logger;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTUltrasonicSensor;

public class EV3SensorActorFactory implements SensorActorFactory {
    private static final Logger log = Logger.forClass(EV3SensorActorFactory.class);
    private final int MOTOR_SPEED = Integer.parseInt(App.getProps().getProperty("racecar.ev3.motorspeed", "250"));
    private final int STEERING_ANGLE = Integer.parseInt(App.getProps().getProperty("racecar.ev3.steering-angle", "60"));
    private final int SENSING_INTERVAL = Integer.parseInt(App.getProps().getProperty("racecar.ev3.sensing-interval", "50"));

    private final EV3LargeRegulatedMotor driveMotor1;
    private final EV3LargeRegulatedMotor driveMotor2;
    private final EV3MediumRegulatedMotor steerMotor;
    private final NXTUltrasonicSensor frontLeft;
    private final NXTUltrasonicSensor frontRight;
    private final NXTUltrasonicSensor backLeft;
    private final NXTUltrasonicSensor backRight;

    // Needs default constructor for init in app
    public EV3SensorActorFactory() {
        driveMotor1 = new EV3LargeRegulatedMotor(MotorPort.C);
        driveMotor2 = new EV3LargeRegulatedMotor(MotorPort.D);
        steerMotor = new EV3MediumRegulatedMotor(MotorPort.B);
        frontLeft = new NXTUltrasonicSensor(SensorPort.S3);
        frontRight = new NXTUltrasonicSensor(SensorPort.S2);
        backLeft = new NXTUltrasonicSensor(SensorPort.S4);
        backRight = new NXTUltrasonicSensor(SensorPort.S1);

        log.info("motor speed: %d", MOTOR_SPEED);
        log.info("steering angle: %d", STEERING_ANGLE);
        log.info("sensing interval: %d", SENSING_INTERVAL);
    }

	@Override
	public Driver getDriver() {
        return new EV3Driver(driveMotor1, driveMotor2, MOTOR_SPEED);
	}

	@Override
	public Steering getSteering() {
        return new EV3Steering(steerMotor, STEERING_ANGLE);
	}

	@Override
	public Sensor[] getSensors() {
        return new Sensor[] {
            new EV3Sensor(frontLeft, SENSING_INTERVAL),
            new EV3Sensor(frontRight, SENSING_INTERVAL),
            new EV3Sensor(backLeft, SENSING_INTERVAL),
            new EV3Sensor(backRight, SENSING_INTERVAL),
        };
	}
}

