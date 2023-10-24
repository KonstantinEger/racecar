package de.userk.consys;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Blackboard {
    private final Map<Sensor, Integer> data;

    public Blackboard(Map<Sensor, Integer> data) {
        this.data = data;
    }

    public Blackboard() {
        this.data = new HashMap<>();
    }

    /**
     * Posts a new value to the blackboard
     * 
     * @param sensor associated sensor
     * @param value  new value
     * @throws NullPointerException if sensor is null
     */
    public synchronized void post(Sensor sensor, int value) throws NullPointerException {
        if (sensor == null)
            throw new NullPointerException("sensor must not be null");

        data.put(sensor, value);
    }

    /**
     * Reads a sensor value.
     * 
     * @param sensor sensor value to read
     * @returns last known value
     */
    public synchronized Optional<Integer> read(Sensor sensor) {
        return Optional.ofNullable(data.get(sensor));
    }
}
