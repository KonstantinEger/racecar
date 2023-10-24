package de.userk.consys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SILTest extends Thread implements Sensor, Motor {
    Optional<Integer> lastTurnValue = Optional.empty();
    Optional<Integer> lastDriveValue = Optional.empty();
    final Collection<SensorObserver> observers = new ArrayList<>();
    final int testValueToSend;

    public SILTest(int testValueToSend) {
        this.testValueToSend = testValueToSend;
    }

    public static void main(String[] args) throws InterruptedException {
        testStopsIfDistanceFrontToShort();
    }

    static void testStopsIfDistanceFrontToShort() throws InterruptedException {
    }

    @Override
    public void run() {
        for (SensorObserver o : observers) {
            o.newValue(testValueToSend, this);
        }
    }

    @Override
    public void turn(int amount) {
        lastTurnValue = Optional.of(amount);
    }

    @Override
    public void drive(int speed) {
        lastDriveValue = Optional.of(speed);
    }

    @Override
    public void registerObserver(SensorObserver observer) {
        observers.add(observer);
    }
}
