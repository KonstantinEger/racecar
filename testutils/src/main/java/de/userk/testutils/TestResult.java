package de.userk.testutils;

import java.util.Map;

class TestResult {
    private final Class<?> testClass;
    private final Map<String, Boolean> results;

    public TestResult(Class<?> testClass, Map<String, Boolean> results) {
        this.testClass = testClass;
        this.results = results;
    }
}
