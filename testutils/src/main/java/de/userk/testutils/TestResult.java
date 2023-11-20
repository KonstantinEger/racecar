package de.userk.testutils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestResult {
    private final Map<String, ResultEntry> results;

    public TestResult() {
        this.results = new HashMap<>();
    }

    public void registerSuccess(String testCase, String stdout) {
        results.put(testCase, new ResultEntry(false, true, null, stdout));
    }

    public void registerFailed(String testCase, Throwable exception, String stdout) {
        results.put(testCase, new ResultEntry(false, false, exception, stdout));
    }

    public void registerIgnored(String testCase) {
        results.put(testCase, new ResultEntry(true, false, null, null));
    }

    public void registerAll(TestResult other) {
        results.putAll(other.results);
    }

    public int ignoredCount() {
        int sum = 0;
        for (ResultEntry entry : results.values()) {
            sum += entry.ignored ? 1 : 0;
        }
        return sum;
    }

    public int successCount() {
        int sum = 0;
        for (ResultEntry entry : results.values()) {
            sum += !entry.ignored && entry.success ? 1 : 0;
        }
        return sum;
    }

    public int errorCount() {
        int sum = 0;
        for (ResultEntry entry : results.values()) {
            sum += !entry.ignored && !entry.success ? 1 : 0;
        }
        return sum;
    }

    public void print(PrintStream o) {
        int success = successCount();
        int err = errorCount();

        for (Entry<String, ResultEntry> entry : results.entrySet()) {
            if (entry.getValue().ignored) {
                o.println("❔ " + entry.getKey() + ": ignored");
            } else if (entry.getValue().success) {
                o.println("✅ " + entry.getKey() + ": successful");
            } else {
                o.println("❌ " + entry.getKey() + ": failed\n" + entry.getValue().capturedOutput);
            }
        }
        o.println("\n\t" + success + " successful\n\t" + err + " failed\n\t" + ignoredCount() + " ignored");
    }

    private static class ResultEntry {
        public final boolean ignored;
        public final boolean success;
        public final Throwable throwable;
        public final String capturedOutput;

        public ResultEntry(boolean i, boolean s, Throwable t, String o) {
            ignored = i;
            success = s;
            throwable = t;
            capturedOutput = o;
        }
    }
}
