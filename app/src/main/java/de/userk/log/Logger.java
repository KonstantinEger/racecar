package de.userk.log;

import java.io.PrintStream;

public class Logger {
    public static enum Level {
        DEBUG, INFO, WARN, ERROR
    }

    public static class LoggerConfig {
        public PrintStream ouStream = System.out;
        public Level minLevel = Level.DEBUG;
    }

    public static final LoggerConfig config = new LoggerConfig();
    private static final Object LOG_LOCK = new Object();

    private final String name;

    private Logger(String name) {
        this.name = name;
    }

    public static Logger forClass(Class<?> clazz) {
        return new Logger(clazz.getName());
    }

    public static Logger withName(String name) {
        return new Logger(name);
    }

    public void log(Level level, String message, Object... data) {
        if (level.compareTo(config.minLevel) < 0)
            return;

        synchronized (LOG_LOCK) {
            config.ouStream.print(level.toString() + "\t" + shortPackageNames(name) + " ["
                    + Thread.currentThread().getName() + "]" + ":\t");
            config.ouStream.format(message, data);
            config.ouStream.println();
        }
    }

    public void debug(String message, Object... data) {
        log(Level.DEBUG, message, data);
    }

    public void info(String message, Object... data) {
        log(Level.INFO, message, data);
    }

    public void warn(String message, Object... data) {
        log(Level.WARN, message, data);
    }

    public void error(String message, Object... data) {
        log(Level.ERROR, message, data);
    }

    private static String shortPackageNames(String className) {
        String[] split = className.split("\\.");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            result.append(split[i].substring(0, 1));
            result.append(".");
        }
        result.append(split[split.length - 1]);
        return result.toString();
    }
}
