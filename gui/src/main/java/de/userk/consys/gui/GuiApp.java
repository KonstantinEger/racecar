package de.userk.consys.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class GuiApp extends Application {
    private final Gui gui = new Gui();

    @Override
    public void start(Stage stage) throws Exception {
        gui.start(stage);
    }
}
