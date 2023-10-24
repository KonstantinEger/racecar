package de.userk.consys;

public interface SensorObserver {
    void newValue(int value, Sensor sensor);
}
