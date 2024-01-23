package de.userk.consys.ev3;

import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.SensorObserver;
import de.userk.log.Logger;
import lejos.hardware.port.I2CException;
import lejos.hardware.sensor.NXTUltrasonicSensor;

public class EV3Sensor implements Sensor {
    private static final Logger log = Logger.forClass(EV3Sensor.class);
    private final NXTUltrasonicSensor sensor;
    private final long sensingInterval;

    private SensorObserver observer = null;
    private Thread thread = null;
    private boolean running = false;

    public EV3Sensor(NXTUltrasonicSensor sensor, long sensingInterval) {
        this.sensor = sensor;
        this.sensingInterval = sensingInterval;
    }

    @Override
    public void registerObserver(SensorObserver observer) {
        log.info("registering observer %s", observer);
        this.observer = observer;
    }

    private int senseValue() {
        try {
            float[] value = new float[sensor.sampleSize()];
            sensor.fetchSample(value, 0);
            return Math.round(value[0] * 100);
        } catch (I2CException ex) {
            log.warn("encountered I2C Exception: %s", ex);
            return 255;
        }
    }

    @Override
    public void startThread() {
        log.info("starting sensor thread");
        running = true;

        thread = new Thread(new Runnable() {
			@Override
			public void run() {
			    try {
			        while (running) {
			            int value = senseValue();
			            observer.newValue(value);
			            Thread.sleep(sensingInterval);
			        }
			    } catch (InterruptedException e) {
			        log.error("sensor thread interrupted: %s", e);
			    }
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
