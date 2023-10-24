package de.userk.consys;

public class Controller implements SensorObserver {
    private final Sensor frontSensor;
    private final Sensor leftSensor;
    private final Sensor backSensor;
    private final Sensor rightSensor;

    private final Motor driveMotor;
    private final Motor turnMotor;

    public Controller(Sensor frontSensor, Sensor leftSensor, Sensor backSensor, Sensor rightSensor, Motor driveMotor,
            Motor turnMotor) {
        this.frontSensor = frontSensor;
        this.leftSensor = leftSensor;
        this.backSensor = backSensor;
        this.rightSensor = rightSensor;

        this.driveMotor = driveMotor;
        this.turnMotor = turnMotor;

        frontSensor.registerObserver(this);
        leftSensor.registerObserver(this);
        backSensor.registerObserver(this);
        rightSensor.registerObserver(this);
    }

    @Override
    public void newValue(int value, Sensor sensor) {
        if (sensor == frontSensor && value < 10) {
            driveMotor.drive(0);
        }
    }
}
