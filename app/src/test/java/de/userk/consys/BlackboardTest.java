package de.userk.consys;

import java.util.HashMap;

import de.userk.testutils.TestCase;

import static de.userk.testutils.Assert.*;

public class BlackboardTest {
    @TestCase
    void testHappyCase() {
        Sensor mockSensor = new MockSensor();
        Sensor dummySensor = new MockSensor();

        Blackboard blackboard = new Blackboard(new HashMap<>());

        blackboard.post(mockSensor, 5);
        blackboard.post(dummySensor, 8);

        assertThat(blackboard.read(mockSensor) == 5, "reads correct value");
        assertThat(blackboard.read(dummySensor) == 8, "reads correct value");
    }

    @TestCase
    void testUpdate() {
        Sensor mockSensor = new MockSensor();

        Blackboard board = new Blackboard(new HashMap<>());

        board.post(mockSensor, 5);
        board.post(mockSensor, 8);
        assertThat(board.read(mockSensor) == 8, "reads correct value after update");
    }

    @TestCase
    void testPostThrowsIfNull() {
        Blackboard board = new Blackboard(new HashMap<>());

        assertThrows(NullPointerException.class, () -> {
            board.post(null, 10);
        }, "throws if null");
    }
}
