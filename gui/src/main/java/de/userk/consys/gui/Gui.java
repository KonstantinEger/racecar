package de.userk.consys.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gui {
    public SliderSensor fl;
    public SliderSensor fr;
    public SliderSensor bl;
    public SliderSensor br;
    public GuiDriver driver;
    public GuiSteering steering;

    public void start(Stage stage) throws Exception {
        fl = new SliderSensor("Front Left");
        fr = new SliderSensor("Front Right");
        bl = new SliderSensor("Back Left");
        br = new SliderSensor("Back Right");
        driver = new GuiDriver(new Label());
        steering = new GuiSteering(new Image("car.png"));

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
