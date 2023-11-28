package de.userk.consys.ctrl;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import de.userk.log.Logger;

/**
 * The Controller is a decision maker. It registers itself to sensors
 * passed to the constructor and saves their values for making a decision when
 * controller.step() is called. The respective commands are passed immediately
 * to the actors. A {@ Thread} can be started to call controller.step() in a
 * loop by calling controller.stepInLoop().
 */
public class Controller {
    private static final Logger log = Logger.forClass(Controller.class);
    private final Driver driver;
    private final Steering steering;
    private final SensorData sensorData;

    private boolean threadRunning = false;
    private Thread loopThread;

    public Controller(Driver driver, Steering steering, Sensor fLeft, Sensor fRight, Sensor bLeft, Sensor bRight) {
        this.driver = driver;
        this.steering = steering;
        this.sensorData = new SensorData();

        fLeft.registerObserver(value -> sensorData.frontLeft = value);
        bLeft.registerObserver(value -> sensorData.backLeft = value);
        fRight.registerObserver(value -> sensorData.frontRight = value);
        bRight.registerObserver(value -> sensorData.backRight = value);
    }

    public void step() {
        log.debug("starting decision process");
        if (!sensorData.isInitialized()) {
            log.debug("some sensor data invalid; aborting decision");
            return;
        }

        SteerCmd steerCmd = SteerCmd.STRAIGHT;
        DriverCmd driverCmd = DriverCmd.FORWARD;

        // Reading freshest sensor values. This is thread safe because its only reading,
        // never writing.
        int frontLeft = sensorData.frontLeft;
        int frontRight = sensorData.frontRight;
        int backLeft = sensorData.backLeft;
        int backRight = sensorData.backRight;
        log.debug("using values (fl, fr, bl, br): (%d, %d, %d, %d)", frontLeft, frontRight, backLeft, backRight);

        if (frontLeft < 50 || frontRight < 50) {
            // close to a wall, turn towards more space
            steerCmd = frontLeft < frontRight ? SteerCmd.RIGHT : SteerCmd.LEFT;
        }

        if (frontLeft < 20 && frontRight < 20) {
            driverCmd = DriverCmd.STOP;
        }

        driver.handle(driverCmd);
        log.debug("sent driver command: %s", driverCmd);
        steering.handle(steerCmd);
        log.debug("sent steering command: %s", steerCmd);
    }

    public void stepInLoop(long timeStep) {
        log.info("starting step loop; stepping every %d ms", timeStep);
        threadRunning = true;
        loopThread = new Thread(() -> {
            while (threadRunning) {
                try {
                    Thread.sleep(timeStep);
                } catch (InterruptedException e) {
                    log.warn("step loop thread stopped because of exception: %s", e);
                }

                Controller.this.step();
            }
        });
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
