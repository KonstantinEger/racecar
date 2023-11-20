package de.userk.testutils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.userk.log.Logger;

public class TestRunner {
    private final Class<?>[] testClasses;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public TestRunner(Class<?>... testClasses) {
        this.testClasses = testClasses;
    }

    public void runAll(String[] stringArgs) {
        Args args = parseArgs(stringArgs);

        Logger.config.minLevel = args.verbose ? Logger.Level.DEBUG : Logger.Level.INFO;

        TestResult result = new TestResult();
        for (Class<?> c : testClasses) {
            try {
                TestResult newResult = runForClass(c, args);
                result.registerAll(newResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.print(System.out);

        pool.shutdown();
    }

    private TestResult runForClass(Class<?> testClass, Args args)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        TestResult result = new TestResult();
        List<Method> annotatedMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(TestCase.class))
                .collect(Collectors.toList());

        Object testInstance = testClass.getConstructor().newInstance();

        // invoke all test methods as futures
        List<Tuple<String, Future<?>>> futures = new ArrayList<>();
        for (Method testCase : annotatedMethods) {
            testCase.setAccessible(true);
            String testCaseName = testClass.getName() + "::" + testCase.getName();
            if (!args.regex.matcher(testCaseName).matches()) {
                continue;
            }

            Future<?> fut = pool.submit(() -> {
                try {
                    testCase.invoke(testInstance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            futures.add(new Tuple<>(testCaseName, fut));
        }

        // join all futures and record results
        for (Tuple<String, Future<?>> fut : futures) {
            try {
                fut.second.get();
                result.registerSuccess(fut.first, "");
            } catch (ExecutionException execException) {
                Throwable t = execException.getCause() // RuntimeException from Runnable in .submit()
                        .getCause(); // maybe InvokationTargetException from Method.invoke()
                if (t instanceof InvocationTargetException) {
                    Throwable actualCause = t.getCause();
                    result.registerFailed(fut.first, actualCause, stackTraceToString(actualCause));
                } else {
                    result.registerFailed(fut.first, t, stackTraceToString(t));
                }
            } catch (Exception e) {
                result.registerFailed(fut.first, e, stackTraceToString(e));
            }
        }

        return result;
    }

    private Args parseArgs(String[] args) {
        Args result = new Args();
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if (s.equals("-v") || s.equals("--verbose")) {
                result.verbose = true;
            } else if (s.equals("-t") || s.equals("--test")) {
                if (args.length <= i + 1) {
                    throw new IllegalArgumentException("-t or --test flag requires regex pattern");
                }
                result.regex = Pattern.compile(".*" + args[i + 1] + ".*");
            }
        }
        return result;
    }

    private final class Args {
        public boolean verbose = false;
        public Pattern regex = Pattern.compile(".*");
    }

    private final class Tuple<A, B> {
        public A first;
        public B second;

        public Tuple(A a, B b) {
            this.first = a;
            this.second = b;
        }
    }

    private static String stackTraceToString(Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(out));
        return out.toString();
    }
}
