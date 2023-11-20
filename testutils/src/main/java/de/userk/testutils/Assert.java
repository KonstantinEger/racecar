package de.userk.testutils;

import java.util.Optional;

public class Assert {

    public static class AssertionFailedException extends RuntimeException {
        public AssertionFailedException(String message) {
            super("Assertion Failed: " + message);
        }
    }

    @FunctionalInterface
    public interface AssertSupplier {
        void call();
    }

    public static void assertThat(boolean condition) {
        assertThat(condition, "condition should be true but was not");
    }

    public static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionFailedException(message);
        }
    }

    public static <T> void assertEq(T a, T b) {
        assertEq(a, b, "a and b should be equal");
    }

    public static <T> void assertEq(T a, T b, String message) {
        if (!a.equals(b)) {
            throw new AssertionFailedException(message + " (expected " + a + " to equal " + b + ")");
        }
    }

    public static void assertPresent(Optional<?> value) {
        assertPresent(value, "value should be present but was empty");
    }

    public static void assertPresent(Optional<?> value, String message) {
        if (!value.isPresent()) {
            throw new AssertionFailedException(message);
        }
    }

    public static void assertThrows(Class<? extends Exception> exceptionClass, Runnable fn, String message) {
        boolean threw = false;
        try {
            fn.run();
        } catch (Exception e) {
            if (!e.getClass().equals(exceptionClass)) {
                throw new AssertionFailedException(message);
            }
            threw = true;
        }
        if (!threw) {
            throw new AssertionFailedException(message);
        }
    }

    public static void not(Runnable fn, String message) {
        boolean threw = false;
        try {
            fn.run();
        } catch (AssertionFailedException e) {
            threw = true;
        }

        if (!threw) {
            throw new AssertionFailedException(message);
        }
    }

    public static void assertUnreachable(String message) {
        throw new AssertionFailedException(message);
    }

    public static void assertUnreachable() {
        assertUnreachable("reached unreachable code");
    }
}
