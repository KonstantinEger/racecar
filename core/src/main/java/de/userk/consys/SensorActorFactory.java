package de.userk.consys;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;

public interface SensorActorFactory {
    Driver getDriver();
    Steering getSteering();
    Sensor[] getSensors();
}
