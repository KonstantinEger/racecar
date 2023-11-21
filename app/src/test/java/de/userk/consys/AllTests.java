package de.userk.consys;

import de.userk.testutils.TestResult;
import de.userk.testutils.TestRunner;

public class AllTests {
    public static void main(String[] args) {
        TestRunner runner = new TestRunner(SILTest.class);
        TestResult result = runner.runAll(args);

        System.exit(result.errorCount() == 0 ? 0 : 1);
    }
}
