package de.userk.testutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestRunner {
    private final Class<?>[] testClasses;

    public TestRunner(Class<?>... testClasses) {
        this.testClasses = testClasses;
    }

    public void runAll() {
        for (Class<?> c : testClasses) {
            runAll(c);
        }
    }

    private void runAll(Class<?> testClass) {
        List<Method> annotatedMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(TestCase.class))
                .collect(Collectors.toList());

        Object testInstance = null;
        try {
            testInstance = testClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int failedCount = 0;
        int successCount = 0;

        for (Method testCase : annotatedMethods) {
            testCase.setAccessible(true);
            boolean failed = false;

            try {
                testCase.invoke(testInstance);
            } catch (InvocationTargetException ite) {
                failed = true;
                System.out.println("❌ FAILED: " + testClass.getName() + "::" + testCase.getName());
                ite.getCause().printStackTrace();
                System.out.println();
            } catch (Exception e) {
                failed = true;
                System.out.println("❌ FAILED: " + testClass.getName() + "::" + testCase.getName());
                e.printStackTrace();
                System.out.println();
            }

            if (!failed) {
                System.out.println("✅ PASSED: " + testClass.getName() + "::" + testCase.getName());
                successCount++;
            } else {
                failedCount++;
            }
        }

        String endMessage = new StringBuilder("\n\nTestRunner for class ")
                .append(testClass.getTypeName())
                .append(" ran a total of ")
                .append(successCount + failedCount)
                .append(" tests:")
                .append("\n\tpassed: ").append(successCount)
                .append("\n\tfailed: ").append(failedCount).toString();
        System.out.println(endMessage);
    }

}
