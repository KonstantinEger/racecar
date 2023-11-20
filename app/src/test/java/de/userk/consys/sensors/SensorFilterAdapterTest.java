package de.userk.consys.sensors;

import static de.userk.testutils.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.userk.testutils.TestCase;

public class SensorFilterAdapterTest {
    @TestCase
    void testIgnores255() {
        MockSensor mockSensor = new MockSensor();
        Sensor adapter = new SensorFilterAdapter(mockSensor);
        List<Integer> expected = Arrays.asList(100, 100);
        List<Integer> received = new ArrayList<>();

        adapter.registerObserver(v -> received.add(v));

        mockSensor.send(100);
        mockSensor.send(255);
        mockSensor.send(100);

        assertEq(received, expected);
    }

    private static class MockSensor implements Sensor {
        private SensorObserver observer;

        @Override
        public void registerObserver(SensorObserver observer) {
            this.observer = observer;
        }

        public void send(int value) {
            observer.newValue(value);
        }
    }
}
