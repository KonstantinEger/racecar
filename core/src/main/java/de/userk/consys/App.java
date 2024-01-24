package de.userk.consys;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.Properties;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.ctrl.Controller;
import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.filter.AllFilters;
import de.userk.consys.sensors.filter.SensorFilterAdapter;
import de.userk.log.Logger;
import de.userk.log.NetOutStream;

public class App {
    private static final Logger log = Logger.forClass(App.class);
    private static Properties props;

    public static void main(String[] args) throws Exception {
        props = loadProperties("application.properties");

        NetLogServer logServer = configureLogServer();
        logServer.setPriority(1);
        logServer.start();

        Logger.config.minLevel = Logger.Level.fromString(props.getProperty("log.level", "INFO"));
        Logger.config.ouStream = new PrintStream(logServer.getOutputStream());

        String sensorFactoryClassName = props.getProperty("racecar.sensor.factory");
        String actorFactoryClassName = props.getProperty("racecar.actor.factory", sensorFactoryClassName);

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
                props,
                driver,
                steering,
                new SensorFilterAdapter(sensors[0], filters),
                new SensorFilterAdapter(sensors[1], filters),
                new SensorFilterAdapter(sensors[2], filters),
                new SensorFilterAdapter(sensors[3], filters));

        for (Sensor s : sensors) {
            s.startThread();
        }

        long ctrlStepInterval = Long.parseLong(props.getProperty("racecar.ctrl.interval", "100"));
        ctrl.stepInLoop(ctrlStepInterval);
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

    private static Properties loadProperties(String filename) throws IOException {
        Properties props = new Properties();
        props.load(App.class.getClassLoader().getResourceAsStream(filename));
        props.putAll(System.getProperties());
        return props;
    }

    private static NetLogServer configureLogServer() throws IOException {
        NetOutStream outStream = new NetOutStream();
        ServerSocket serverSocket = new ServerSocket(8080);
        return new NetLogServer(serverSocket, outStream);
    }

    public static Properties getProps() {
        return props;
    }
}
