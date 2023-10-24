package de.userk.consys;

import static de.userk.testutils.Assert.*;

import de.userk.testutils.TestCase;

public class ControllerTest {
    @TestCase
    void testObstacleInFrontStops() throws Exception {
        SILTest silTest = new SILTest(5);
        Blackboard bb = new Blackboard();
        // attach to front sensor and drive motor
        Controller ctrl = new Controller(bb, silTest, new MockSensor(), new MockSensor(), new MockSensor(), silTest,
                new MockMotor());

        ctrl.start();
        silTest.start();

        Thread.sleep(500);

        assertPresent(silTest.lastDriveValue, "drive was called");
        assertThat(silTest.lastDriveValue.get() == 0, "drive was called with 0 value");
        not(() -> assertPresent(silTest.lastTurnValue), "turn was not called");

        ctrl.stopControllerLoop();
    }

    @TestCase
    void testContinuesDrivingIfNoObstacle() throws Exception {
        SILTest silTest = new SILTest(50);
        Blackboard bb = new Blackboard();
        // attach to front sensor and drive motor
        Controller ctrl = new Controller(bb, silTest, new MockSensor(), new MockSensor(), new MockSensor(), silTest,
                new MockMotor());

        ctrl.start();
        silTest.start();

        Thread.sleep(500);

        not(() -> assertPresent(silTest.lastDriveValue), "drive was never called");
        not(() -> assertPresent(silTest.lastTurnValue), "turn was not called");

        ctrl.stopControllerLoop();
    }
}
