package de.userk.consys.sensors;

public interface Sensor {
    void registerObserver(SensorObserver observer);
}
