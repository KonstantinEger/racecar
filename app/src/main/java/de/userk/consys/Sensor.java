package de.userk.consys;

public interface Sensor {
    void registerObserver(SensorObserver observer);
}