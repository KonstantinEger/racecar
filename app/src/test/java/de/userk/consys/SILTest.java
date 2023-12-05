package de.userk.consys;

import de.userk.consys.Helper.MockSteering;
import de.userk.consys.actors.DriverCmd;
import de.userk.consys.actors.SteerCmd;

import static de.userk.testutils.Assert.assertEq;

import java.util.Optional;

import de.userk.consys.Helper.MockAllSensors;
import de.userk.consys.Helper.MockDriver;
import de.userk.consys.ctrl.Controller;
import de.userk.consys.sensors.filter.AllFilters;
import de.userk.consys.sensors.filter.SensorFilterAdapter;
import de.userk.consys.sensors.filter.ValueFiltersFactory;
import de.userk.testutils.TestCase;

public class SILTest {
    public static final TimeStep[] turnsRightIfWallFrontLeftScenario = new TimeStep[] {
            new TimeStep(new int[] { 70, 100, 100, 100 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 60, 90, 110, 110 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 50, 80, 120, 120 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 40, 70, 130, 130 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 30, 60, 140, 140 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 20, 50, 150, 150 }, SteerCmd.RIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 24, 40, 160, 160 }, SteerCmd.RIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 30, 40, 170, 170 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
    };

    @TestCase
    void testTurnsRightIfWallFrontLeft() throws InterruptedException {
        runTestcase(turnsRightIfWallFrontLeftScenario);
    }

    public static final TimeStep[] turnsRightIfWallFrontLeftWithErrorsScenario = new TimeStep[] {
            new TimeStep(new int[] { 70, 100, 100, 100 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 60, 90, 110, 110 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 245, 80, 120, 120 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 40, 70, 130, 130 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 30, 60, 140, 255 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 20, 50, 150, 150 }, SteerCmd.RIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 255, 40, 160, 160 }, SteerCmd.RIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 30, 40, 170, 170 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
    };

    @TestCase
    void testTurnsRightIfWallFrontLeftWithErrors() throws InterruptedException {
        runTestcase(turnsRightIfWallFrontLeftWithErrorsScenario);
    }

    public static final TimeStep[] turnsLeftIfWallFrontRightScenario = new TimeStep[] {
            new TimeStep(new int[] { 100, 70, 100, 100 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 90, 60, 110, 110 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 80, 50, 120, 120 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 70, 40, 130, 130 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 60, 30, 140, 140 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 50, 20, 150, 150 }, SteerCmd.LEFT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 40, 24, 160, 160 }, SteerCmd.LEFT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 40, 30, 170, 170 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
    };

    @TestCase
    void testTurnsLeftIfWallFrontRight() throws InterruptedException {
        runTestcase(turnsLeftIfWallFrontRightScenario);
    }

    public static final TimeStep[] turnsLeftIfWallFrontRightWithErrorsScenario = new TimeStep[] {
            new TimeStep(new int[] { 100, 70, 100, 100 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 90, 60, 110, 110 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 255, 50, 120, 120 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 70, 40, 253, 130 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 60, 30, 140, 140 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 50, 20, 150, 150 }, SteerCmd.LEFT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 40, 245, 160, 160 }, SteerCmd.LEFT, DriverCmd.FORWARD, 1000),
            new TimeStep(new int[] { 40, 30, 170, 170 }, SteerCmd.STRAIGHT, DriverCmd.FORWARD, 1000),
    };

    @TestCase
    void testTurnsLeftIfWallFrontRightWithErrors() throws InterruptedException {
        runTestcase(turnsLeftIfWallFrontRightWithErrorsScenario);
    }

    private void runTestcase(TimeStep[] testCase) throws InterruptedException {
        MockDriver driver = new MockDriver();
        MockSteering steering = new MockSteering();
        MockAllSensors sensors = new MockAllSensors();

        ValueFiltersFactory filtersFactory = new AllFilters();
        Controller ctrl = new Controller(
                driver,
                steering,
                new SensorFilterAdapter(sensors.frontLeft, filtersFactory),
                new SensorFilterAdapter(sensors.frontRight, filtersFactory),
                new SensorFilterAdapter(sensors.backLeft, filtersFactory),
                new SensorFilterAdapter(sensors.backRight, filtersFactory));

        for (int i = 0; i < testCase.length; i++) {
            TimeStep step = testCase[i];
            int[] d = step.sensorData;
            sensors.allSense(d[0], d[1], d[2], d[3]);
            ctrl.step();
            assertEq(driver.getAndReset(), Optional.of(step.driverCmd), "failed at step " + i);
            assertEq(steering.getAndReset(), Optional.of(step.steerCmd), "failed at step " + i);
            Thread.sleep(step.sleepTime);
        }
    }

    public static final class TimeStep {
        public final int[] sensorData;
        public final SteerCmd steerCmd;
        public final DriverCmd driverCmd;
        public final long sleepTime;

        public TimeStep(int[] s, SteerCmd sc, DriverCmd dc, long st) {
            this.sensorData = s;
            this.steerCmd = sc;
            this.driverCmd = dc;
            this.sleepTime = st;
        }
    }
}
