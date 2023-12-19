package de.userk.consys;

import java.lang.reflect.InvocationTargetException;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.ctrl.Controller;
import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.filter.AllFilters;
import de.userk.consys.sensors.filter.SensorFilterAdapter;
import de.userk.log.Logger;

public class App {
    private static final Logger log = Logger.forClass(App.class);

    public static void main(String[] args) throws Exception {
        if (!(args.length == 1 || args.length == 2)) {
            throw new IllegalArgumentException("pass factories as arguments");
        }

        String sensorFactoryClassName = args[0];
        String actorFactoryClassName = args.length > 1 ? args[1] : args[0];

        SensorActorFactory sensorFactory = loadAndInstantiate(sensorFactoryClassName);
        SensorActorFactory actorFactory = sensorFactoryClassName.equals(actorFactoryClassName)
                ? sensorFactory
                : loadAndInstantiate(actorFactoryClassName);

        log.info("instanciated SensorFactory (%s) and ActorFactory (%s)", sensorFactory, actorFactory);

        Driver driver = actorFactory.getDriver();
        Steering steering = actorFactory.getSteering();
        Sensor[] sensors = sensorFactory.getSensors();

        AllFilters filters = new AllFilters();
        Controller ctrl = new Controller(
                driver,
                steering,
                new SensorFilterAdapter(sensors[0], filters),
                new SensorFilterAdapter(sensors[1], filters),
                new SensorFilterAdapter(sensors[2], filters),
                new SensorFilterAdapter(sensors[3], filters));

        for (Sensor s : sensors) {
            s.startThread();
        }

        ctrl.stepInLoop(500);
    }

    private static SensorActorFactory loadAndInstantiate(String clazz)
            throws InvocationTargetException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, ClassNotFoundException {
        return (SensorActorFactory) App.class
                .getClassLoader()
                .loadClass(clazz)
                .getConstructor()
                .newInstance();
    }
}
