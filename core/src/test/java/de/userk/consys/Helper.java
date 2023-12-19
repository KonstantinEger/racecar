package de.userk.consys;

import java.util.Optional;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.SensorObserver;

public class Helper {
    public static class MockSensor implements Sensor {
        public SensorObserver observer;

        @Override
        public void registerObserver(SensorObserver observer) {
            this.observer = observer;
        }

        public void sense(int value) {
            observer.newValue(value);
        }

        @Override
        public void startThread() {
            // Mocks do nothing
        }

        @Override
        public void stopSync() throws InterruptedException {
            // Mocks do nothing
        }
    }

    public static class MockAllSensors {
        public final MockSensor frontLeft = new MockSensor();
        public final MockSensor frontRight = new MockSensor();
        public final MockSensor backLeft = new MockSensor();
        public final MockSensor backRight = new MockSensor();

        public void allSense(int fl, int fr, int bl, int br) {
            frontLeft.sense(fl);
            frontRight.sense(fr);
            backLeft.sense(bl);
            backRight.sense(br);
        }
    }

    public static class MockDriver implements Driver {
        public Optional<DriverCmd> lastCmd = Optional.empty();

        @Override
        public void handle(DriverCmd cmd) {
            lastCmd = Optional.of(cmd);
        }

        public Optional<DriverCmd> getAndReset() {
            Optional<DriverCmd> cmd = lastCmd;
            lastCmd = Optional.empty();
            return cmd;
        }
    }

    public static class MockSteering implements Steering {
        public Optional<SteerCmd> lastCmd = Optional.empty();

        @Override
        public void handle(SteerCmd cmd) {
            lastCmd = Optional.of(cmd);
        }

        public Optional<SteerCmd> getAndReset() {
            Optional<SteerCmd> cmd = lastCmd;
            lastCmd = Optional.empty();
            return cmd;
        }
    }
}
