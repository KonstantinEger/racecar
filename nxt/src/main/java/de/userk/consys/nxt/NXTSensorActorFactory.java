package de.userk.consys.nxt;

import de.userk.consys.SensorActorFactory;
import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class NXTSensorActorFactory implements SensorActorFactory {
    private final int MOTOR_SPEED = 40;
    private final int STEERING_ANGLE = 40;
    private final int SENSING_INTERVAL = 100;

    private final NXTDriver driver = new NXTDriver(new NXTRegulatedMotor(MotorPort.A), MOTOR_SPEED);
    private final NXTSteering steering = new NXTSteering(new NXTRegulatedMotor(MotorPort.B), STEERING_ANGLE);
    private final NXTSensor fl = new NXTSensor(new UltrasonicSensor(SensorPort.S1), SENSING_INTERVAL);
    private final NXTSensor fr = new NXTSensor(new UltrasonicSensor(SensorPort.S2), SENSING_INTERVAL);
    private final NXTSensor bl = new NXTSensor(new UltrasonicSensor(SensorPort.S3), SENSING_INTERVAL);
    private final NXTSensor br = new NXTSensor(new UltrasonicSensor(SensorPort.S4), SENSING_INTERVAL);

    @Override
    public Driver getDriver() {
        return driver;
    }

    @Override
    public Steering getSteering() {
        return steering;
    }

    @Override
    public Sensor[] getSensors() {
        return new Sensor[] { fl, fr, bl, br };
    }

}
