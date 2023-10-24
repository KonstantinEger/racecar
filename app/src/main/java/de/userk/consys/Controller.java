package de.userk.consys;

import java.util.Optional;

public class Controller extends Thread implements SensorObserver {
    private final Blackboard sensorBlackboard;
    private final Motor driveMotor;
    private final Motor turnMotor;
    private final Sensor frontSensor;
    private final Sensor leftSensor;
    private final Sensor backSensor;
    private final Sensor rightSensor;
    private final long sleepMillis = 100;
    private boolean running = true;

    public Controller(Blackboard sensorBlackboard, Sensor frontSensor, Sensor leftSensor, Sensor backSensor,
            Sensor rightSensor, Motor driveMotor,
            Motor turnMotor) {
        this.driveMotor = driveMotor;
        this.turnMotor = turnMotor;
        this.sensorBlackboard = sensorBlackboard;
        this.frontSensor = frontSensor;
        this.leftSensor = leftSensor;
        this.backSensor = backSensor;
        this.rightSensor = rightSensor;

        frontSensor.registerObserver(this);
        leftSensor.registerObserver(this);
        backSensor.registerObserver(this);
        rightSensor.registerObserver(this);
    }

    @Override
    public void newValue(int value, Sensor sensor) {
        sensorBlackboard.post(sensor, value);
    }

    @Override
    public void run() {
        try {
            controllerLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopControllerLoop() {
        running = false;
    }

    private void controllerLoop() throws InterruptedException {
        while (running) {
            Thread.sleep(sleepMillis);
            Optional<Integer> front = sensorBlackboard.read(frontSensor);

            if (front.isPresent() && front.get() < 10) {
                driveMotor.drive(0);
            }
        }
    }
}
