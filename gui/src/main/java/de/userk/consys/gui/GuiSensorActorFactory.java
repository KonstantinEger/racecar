package de.userk.consys.gui;

import de.userk.consys.SensorActorFactory;
import de.userk.consys.actors.Driver;
import de.userk.consys.actors.Steering;
import de.userk.consys.sensors.Sensor;
import de.userk.log.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

public class GuiSensorActorFactory implements SensorActorFactory {
    private static final Logger log = Logger.forClass(GuiSensorActorFactory.class);
    private static Gui gui;

    private Thread thread;

    public static class TempApp extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            log.info("starting gui");
            GuiSensorActorFactory.gui.start(stage);
        }
    }

    public GuiSensorActorFactory() {
        log.info("instanciating gui");
        gui = new Gui();
        thread = new Thread(() -> {
            Application.launch(TempApp.class);
        });
        thread.start();

        blockUntilComponentsInitialized();
    }

    private void blockUntilComponentsInitialized() {
        final int MAX_TRIES = 10;
        final int BACKOFF_PERIOD = 500;

        log.info("waiting for GUI components to initialize: retries=%d, backoff=%d ms", MAX_TRIES, BACKOFF_PERIOD);

        boolean completed = false;

        try {
            for (int i = 0; !completed && i < MAX_TRIES; i++) {
                Thread.sleep(BACKOFF_PERIOD);
                log.debug("attempt %d", i+1);

                if (gui.driver == null) {
                    log.debug("still not initialized; retrying");
                    continue;
                }

                completed = true;
            }
        } catch (InterruptedException e) {
            log.error("interrupted while waiting for gui components %s", e);
            throw new RuntimeException(e);
        }

        if (completed) {
            log.info("initialization of GUI components completed");
        } else {
            throw new RuntimeException("failed to init GUI components in time");
        }
    }

	@Override
	public Driver getDriver() {
        return gui.driver;
	}

	@Override
	public Steering getSteering() {
        return gui.steering;
	}

	@Override
	public Sensor[] getSensors() {
        return new Sensor[]{
            gui.fl,
            gui.fr,
            gui.bl,
            gui.br
        };
	}
}
