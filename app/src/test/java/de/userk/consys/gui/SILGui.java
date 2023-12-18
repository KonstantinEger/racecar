package de.userk.consys.gui;

import de.userk.consys.SILTest;
import de.userk.consys.SILTest.TimeStep;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class SILGui extends Application {
    private final Gui gui = new Gui();

    @Override
    public void start(Stage stage) throws Exception {
        new Thread(() -> {
            try {
                runScenario(SILTest.turnsLeftIfWallFrontRightScenario);
                runScenario(SILTest.turnsLeftIfWallFrontRightWithErrorsScenario);
                runScenario(SILTest.turnsRightIfWallFrontLeftScenario);
                runScenario(SILTest.turnsRightIfWallFrontLeftWithErrorsScenario);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        gui.start(stage);
    }

    @Override
    public void stop() {
        gui.stop();
    }

    private void runScenario(TimeStep[] scenario) throws InterruptedException {
        for (TimeStep step : scenario) {
            Platform.runLater(() -> {
                gui.fl.overwriteValue(step.sensorData[0]);
                gui.fr.overwriteValue(step.sensorData[1]);
                gui.bl.overwriteValue(step.sensorData[2]);
                gui.br.overwriteValue(step.sensorData[3]);
            });
            Thread.sleep(step.sleepTime);
        }
    }
}
