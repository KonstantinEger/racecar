package de.userk.consys.nxt;

import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.SensorObserver;
import de.userk.log.Logger;
import lejos.nxt.UltrasonicSensor;

public class NXTSensor implements Sensor {
    private static final Logger log = Logger.forClass(NXTSensor.class);
    private final UltrasonicSensor sensor;
    private final long sensingInterval;

    private SensorObserver observer = null;
    private Thread thread = null;
    private boolean running = false;

    public NXTSensor(UltrasonicSensor sensor, long sensingInterval) {
        this.sensingInterval = sensingInterval;
        this.sensor = sensor;
    }

    private int senseValue() {
        return sensor.getDistance();
    }

    @Override
    public void registerObserver(SensorObserver observer) {
        this.observer = observer;
    }

    @Override
    public void startThread() {
        log.info("starting sensor thread");
        running = true;

        thread = new Thread(() -> {
            try {
                while (running) {
                    int value = senseValue();
                    observer.newValue(value);
                    Thread.sleep(sensingInterval);
                }
            } catch (InterruptedException e) {
                log.error("sensor thread interrupted: %s", e);
            }
        });
        thread.start();
    }

    public void stopAsync() {
        if (thread == null) {
            return;
        }

        log.info("sending stop signal to thread %s", thread.getName());
        running = false;
    }

    @Override
    public void stopSync() throws InterruptedException {
        if (thread == null) {
            return;
        }

        stopAsync();
        log.info("waiting for thread %s to finish", thread.getName());
        thread.join();
    }
}
