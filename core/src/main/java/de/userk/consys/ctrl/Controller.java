package de.userk.consys.ctrl;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.SensorObserver;
import de.userk.log.Logger;

import java.util.Properties;

/**
 * The Controller is a decision maker. It registers itself to sensors
 * passed to the constructor and saves their values for making a decision when
 * controller.step() is called. The respective commands are passed immediately
 * to the actors. A Thread can be started to call controller.step() in a
 * loop by calling controller.stepInLoop().
 */
public class Controller {
    private static final Logger log = Logger.forClass(Controller.class);
    private final int SENSOR_LOWER_LIMIT;
    private final Driver driver;
    private final Steering steering;
    private final SensorData sensorData;
    private ControllerState state = ControllerState.ForwardStraight;

    private boolean threadRunning = false;
    private Thread loopThread;

    public Controller(Properties props, Driver driver, Steering steering, Sensor fLeft, Sensor fRight, Sensor bLeft, Sensor bRight) {
        this.driver = driver;
        this.steering = steering;
        this.sensorData = new SensorData();
        this.SENSOR_LOWER_LIMIT = Integer.parseInt(props.getProperty("racecar.ctrl.sensor-lower-limit", "40"));

        fLeft.registerObserver(new SensorObserver() { public void newValue(int value) { sensorData.frontLeft = value; } });
        fRight.registerObserver(new SensorObserver() { public void newValue(int value) { sensorData.frontRight = value; } });
        bLeft.registerObserver(new SensorObserver() { public void newValue(int value) { sensorData.backLeft = value; } });
        bRight.registerObserver(new SensorObserver() { public void newValue(int value) { sensorData.backRight = value; } });

        log.info("controller sensor limit: %d", SENSOR_LOWER_LIMIT);
    }

    public void step() {
        log.debug("starting decision process");
        if (!sensorData.isInitialized()) {
            log.warn("some sensor data invalid; aborting decision");
            return;
        }

        int frontLeft = sensorData.frontLeft;
        int frontRight = sensorData.frontRight;
        int backLeft = sensorData.backLeft;
        int backRight = sensorData.backRight;
        log.debug("using values: (%d, %d, %d, %d)", frontLeft, frontRight, backLeft, backRight);

        switch (state) {
            case ForwardStraight:
            case ForwardRight:
            case ForwardLeft:
                if (frontRight < SENSOR_LOWER_LIMIT && frontLeft < SENSOR_LOWER_LIMIT) {
                    if (backLeft <= backRight) {
                        state = ControllerState.BackwardLeft;
                    } else {
                        state = ControllerState.BackwardRight;
                    }
                }
                if (frontRight < SENSOR_LOWER_LIMIT && frontLeft > SENSOR_LOWER_LIMIT) {
                    state = ControllerState.ForwardLeft;
                }
                if (frontRight > SENSOR_LOWER_LIMIT && frontLeft < SENSOR_LOWER_LIMIT) {
                    state = ControllerState.ForwardRight;
                }
                if (frontRight > SENSOR_LOWER_LIMIT && frontLeft > SENSOR_LOWER_LIMIT) {
                    state = ControllerState.ForwardStraight;
                }
                break;
            case BackwardLeft:
                if (backLeft < SENSOR_LOWER_LIMIT || backRight < SENSOR_LOWER_LIMIT) {
                    state = ControllerState.ForwardStraight;
                }
                break;
            case BackwardRight:
                if (backRight < SENSOR_LOWER_LIMIT || backLeft < SENSOR_LOWER_LIMIT) {
                    state = ControllerState.ForwardStraight;
                }
                break;
        }

        DriverCmd driverCmd = driverCmdFromState(state);
        SteerCmd steerCmd = steerCmdFromState(state);

        driver.handle(driverCmd);
        log.debug("sent driver command: %s", driverCmd);
        steering.handle(steerCmd);
        log.debug("sent steering command: %s", steerCmd);
    }

    private static DriverCmd driverCmdFromState(ControllerState state) {
        switch(state) {
            case ForwardStraight:
            case ForwardLeft:
            case ForwardRight:
                return DriverCmd.FORWARD;
            case BackwardLeft:
            case BackwardRight:
                return DriverCmd.BACKWARD;
            default:
                return DriverCmd.STOP;
        }
    }

    private static SteerCmd steerCmdFromState(ControllerState state) {
        switch (state) {
            case ForwardRight:
            case BackwardRight:
                return SteerCmd.RIGHT;
            case ForwardLeft:
            case BackwardLeft:
                return SteerCmd.LEFT;
            default:
                return SteerCmd.STRAIGHT;
        }
    }

    public void stepInLoop(final long timeStep) {
        log.info("starting step loop; stepping every %d ms", timeStep);
        threadRunning = true;
        loopThread = new Thread() {
            public void run() {
                while (threadRunning) {
                    try {
                        Thread.sleep(timeStep);
                    } catch (InterruptedException e) {
                        log.warn("step loop thread stopped because of exception: %s", e);
                    }

                    Controller.this.step();
                }
            }
        };
        loopThread.start();
    }

    public void stopStepLoopAsync() {
        log.info("signaling step loop to stop");
        threadRunning = false;
    }

    public void stopStepLoopSync() throws InterruptedException {
        stopStepLoopAsync();
        loopThread.join();
    }
}
