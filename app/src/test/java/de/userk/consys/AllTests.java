package de.userk.consys;

import de.userk.consys.ctrl.ControllerTest;
import de.userk.consys.sensors.SensorFilterAdapterTest;
import de.userk.testutils.TestRunner;

public class AllTests {
    public static void main(String[] args) {
        TestRunner runner = new TestRunner(ControllerTest.class, SensorFilterAdapterTest.class, SILTest.class);
        runner.runAll(args);
    }
}
