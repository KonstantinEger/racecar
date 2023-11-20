package de.userk.consys.sensors;

import static de.userk.testutils.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.userk.consys.Helper.MockSensor;
import de.userk.testutils.TestCase;

public class SensorFilterAdapterTest {
    @TestCase
    void testIgnores255() {
        MockSensor mockSensor = new MockSensor();
        Sensor adapter = new SensorFilterAdapter(mockSensor);
        List<Integer> expected = Arrays.asList(100, 100);
        List<Integer> received = new ArrayList<>();

        adapter.registerObserver(v -> received.add(v));

        mockSensor.sense(100);
        mockSensor.sense(255);
        mockSensor.sense(100);

        assertEq(received, expected);
    }
}
