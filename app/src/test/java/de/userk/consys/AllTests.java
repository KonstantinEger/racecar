package de.userk.consys;

import de.userk.testutils.TestRunner;

public class AllTests {
    public static void main(String[] args) {
        TestRunner runner = new TestRunner(ControllerTest.class);
        runner.runAll();
    }
}
