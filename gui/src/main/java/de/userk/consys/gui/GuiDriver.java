package de.userk.consys.gui;

import de.userk.consys.actors.Driver;
import de.userk.consys.actors.DriverCmd;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class GuiDriver implements Driver {
    private final Label label;

    public GuiDriver(Label label) {
        this.label = label;
        this.label.setText("currently driving: STOP");
    }

    @Override
    public void handle(DriverCmd cmd) {
        Platform.runLater(() -> {
            label.setText("currently driving: " + cmd.toString());
        });
    }

    public Label getLabel() {
        return label;
    }
}
