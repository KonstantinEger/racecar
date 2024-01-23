package de.userk.consys.sil;

import de.userk.consys.SILTest;
import de.userk.consys.SensorActorFactory;
import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import de.userk.log.Logger;

public class SILSensorActorFactory implements SensorActorFactory {
    private static final Logger log = Logger.forClass(SILSensorActorFactory.class);
    private final SILSensor fl;
    private final SILSensor fr;
    private final SILSensor bl;
    private final SILSensor br;
    private final SILDriver driver;
    private final SILSteering steering;

    private SILTest.TimeStep currentStep;

    public SILSensorActorFactory() {
        this.fl = new SILSensor();
        this.fr = new SILSensor();
        this.bl = new SILSensor();
        this.br = new SILSensor();
        this.steering = new SILSteering(this);
        this.driver = new SILDriver(this);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SILSensorActorFactory.this.runScenario(SILTest.turnsLeftIfWallFrontRightScenario);
                    SILSensorActorFactory.this.runScenario(SILTest.turnsLeftIfWallFrontRightWithErrorsScenario);
                    SILSensorActorFactory.this.runScenario(SILTest.turnsRightIfWallFrontLeftScenario);
                    SILSensorActorFactory.this.runScenario(SILTest.turnsRightIfWallFrontLeftWithErrorsScenario);
                } catch (InterruptedException e) {
                    log.error("interrupted while running scenarios %s", e);
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    private void runScenario(SILTest.TimeStep[] scenario) throws InterruptedException {
        for (SILTest.TimeStep step : scenario) {
            currentStep = step;
            fl.sendValue(step.sensorData[0]);
            fr.sendValue(step.sensorData[1]);
            bl.sendValue(step.sensorData[2]);
            br.sendValue(step.sensorData[3]);
            Thread.sleep(step.sleepTime);
        }
    }

    SILTest.TimeStep getCurrenTimeStep() {
        return currentStep;
    }

    @Override
    public Driver getDriver() {
        return driver;
    }

    @Override
    public Steering getSteering() {
        return steering;
    }

    @Override
    public Sensor[] getSensors() {
        return new Sensor[] { fl, fr, bl, br };
    }
}
