package de.userk.consys;

import static de.userk.testutils.Assert.*;

import de.userk.testutils.TestCase;

public class ControllerTest {
    @TestCase
    void testObstacleInFrontStops() throws Exception {
        SILTest silTest = new SILTest(5);
        // attach to front sensor and drive motor
        new Controller(silTest, new MockSensor(), new MockSensor(), new MockSensor(), silTest, new MockMotor());

        silTest.start();

        Thread.sleep(100);

        assertPresent(silTest.lastDriveValue, "drive was called");
        assertThat(silTest.lastDriveValue.get() == 0, "drive was called with 0 value");
        not(() -> assertPresent(silTest.lastTurnValue), "turn was not called");
    }

    @TestCase
    void testDoesNotStopIfNoObstacle() throws Exception {
        SILTest silTest = new SILTest(10);
        // attach to front sensor and drive motor
        new Controller(silTest, new MockSensor(), new MockSensor(), new MockSensor(), silTest, new MockMotor());

        silTest.start();

        Thread.sleep(100);

        not(() -> assertPresent(silTest.lastDriveValue), "drive was not called because it should not change");
        not(() -> assertPresent(silTest.lastTurnValue), "turn was not called");
    }
}
