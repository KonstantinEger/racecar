package de.userk.consys.ev3;

import de.userk.consys.SensorActorFactory;
import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class EV3SensorActorFactory implements SensorActorFactory {
    private final int MOTOR_SPEED = 40;
    private final int STEERING_ANGLE = 40;
    private final int SENSING_INTERVAL = 100;

    private final EV3LargeRegulatedMotor driveMotor;
    private final EV3MediumRegulatedMotor steerMotor;
    private final EV3UltrasonicSensor frontLeft;
    private final EV3UltrasonicSensor frontRight;
    private final EV3UltrasonicSensor backLeft;
    private final EV3UltrasonicSensor backRight;


    // Needs default constructor for init in app
    public EV3SensorActorFactory() {
        // TODO: set correct ports
        driveMotor = new EV3LargeRegulatedMotor(MotorPort.A);
        steerMotor = new EV3MediumRegulatedMotor(MotorPort.B);
        frontLeft = new EV3UltrasonicSensor(SensorPort.S1);
        frontRight = new EV3UltrasonicSensor(SensorPort.S2);
        backLeft = new EV3UltrasonicSensor(SensorPort.S3);
        backRight = new EV3UltrasonicSensor(SensorPort.S4);
    }

	@Override
	public Driver getDriver() {
        return new EV3Driver(driveMotor, MOTOR_SPEED);
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

