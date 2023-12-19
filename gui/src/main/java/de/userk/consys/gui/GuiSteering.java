package de.userk.consys.gui;

import de.userk.consys.actors.SteerCmd;
import de.userk.consys.actors.Steering;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiSteering implements Steering {
    private final ImageView imageView;

    public GuiSteering(Image image) {
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(150);
        this.imageView.setFitHeight(100);
        this.imageView.rotateProperty().set(180);
    }

    @Override
    public void handle(SteerCmd cmd) {
        Platform.runLater(() -> {
            switch (cmd) {
                case STRAIGHT:
                    imageView.rotateProperty().set(180);
                    break;
                case LEFT:
                    imageView.rotateProperty().set(180 - 45);
                    break;
                case RIGHT:
                    imageView.rotateProperty().set(180 + 45);
                    break;
            }
        });
    }

    public Node getLabel() {
        return imageView;
    }
}
