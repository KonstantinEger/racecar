package de.userk.consys.sil;

import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.SensorObserver;

public class SILSensor implements Sensor {
    private SensorObserver observer;

    void sendValue(int value) {
        if (observer == null) {
            return;
        }

        observer.newValue(value);
    }

    @Override
    public void registerObserver(SensorObserver observer) {
        this.observer = observer;
    }

    @Override
    public void startThread() {
        // Nothing because SILSensorActorFactory has thread
    }

    @Override
    public void stopSync() throws InterruptedException {
        // Nothing because SILSensorActorFactory has thread
    }
}
