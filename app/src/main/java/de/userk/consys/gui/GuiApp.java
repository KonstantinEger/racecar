package de.userk.consys.gui;

import de.userk.consys.ctrl.Controller;
import de.userk.consys.sensors.filter.AllFilters;
import de.userk.consys.sensors.filter.SensorFilterAdapter;
import de.userk.log.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiApp extends Application {
    private SliderSensor fl;
    private SliderSensor fr;
    private SliderSensor bl;
    private SliderSensor br;
    private Controller ctrl;

    @Override
    public void init() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        Logger.config.minLevel = Logger.Level.DEBUG;

        fl = new SliderSensor("Front Left");
        fr = new SliderSensor("Front Right");
        bl = new SliderSensor("Back Left");
        br = new SliderSensor("Back Right");
        GuiDriver driver = new GuiDriver(new Label());
        GuiSteering steering = new GuiSteering(new Image("car.png"));

        AllFilters allFilters = new AllFilters();
        ctrl = new Controller(
                driver,
                steering,
                new SensorFilterAdapter(fl, allFilters),
                new SensorFilterAdapter(fr, allFilters),
                new SensorFilterAdapter(bl, allFilters),
                new SensorFilterAdapter(br, allFilters));

        ctrl.stepInLoop(1000);
        fl.startThread(50);
        fr.startThread(50);
        bl.startThread(50);
        br.startThread(50);

        GridPane grid = new GridPane();
        grid.add(fl.getView(), 0, 0);
        grid.add(fr.getView(), 2, 0);
        grid.add(bl.getView(), 0, 2);
        grid.add(br.getView(), 2, 2);
        grid.add(new VBox(driver.getLabel(), steering.getLabel()), 1, 1);
        grid.add(hSpacer(), 1, 0);
        grid.add(vSpacer(), 0, 1);

        Scene scene = new Scene(grid, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        fl.stopAsync();
        fr.stopAsync();
        bl.stopAsync();
        br.stopAsync();
        ctrl.stopStepLoopAsync();
    }

    private static Pane hSpacer() {
        Pane spacer = new Pane();
        GridPane.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private static Pane vSpacer() {
        Pane spacer = new Pane();
        GridPane.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}