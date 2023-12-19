package de.userk.consys.gui;

import de.userk.consys.sensors.Sensor;
import de.userk.consys.sensors.SensorObserver;
import de.userk.log.Logger;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class SliderSensor implements Sensor {
    private final Slider slider;
    private final String name;
    private final Logger log;
    private SensorObserver observer;
    private boolean threadRunning = false;
    private Thread thread;
    private double lastValue;

    public SliderSensor(String name, Slider slider) {
        this.slider = slider;
        this.lastValue = this.slider.getValue();
        this.name = name;
        this.log = Logger.withName(SliderSensor.class.getName() + "(" + name + ")");
    }

    public SliderSensor(String name) {
        this(name, sliderFactory());
    }

    private static Slider sliderFactory() {
        Slider s = new Slider(0, 255, 1);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        s.setValue(100);
        return s;
    }

    @Override
    public void registerObserver(SensorObserver observer) {
        this.observer = observer;
    }

    public void overwriteValue(double value) {
        slider.setValue(value);
    }

    @Override
    public void startThread() {
        final int interval = 100;
        log.info("starting sensor loop with interval of %d ms", interval);
        if (threadRunning) {
            log.info("already running");
            return;
        }

        threadRunning = true;

        thread = new Thread(() -> {
            while (threadRunning) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    log.error("error in sensor loop thread: %s", e);
                    return;
                }

                if (observer == null) {
                    continue;
                }

                int value = (int) Math.round(lastValue);
                log.debug("sending value %d", value);
                observer.newValue(value);
            }
        });
        thread.start();
    }

    public void stopAsync() {
        log.info("signaling step loop to stop");
        threadRunning = false;
    }

    @Override
    public void stopSync() throws InterruptedException {
        stopAsync();
        thread.join();
    }

    public Node getView() {
        final Label text = new Label(name + ": " + slider.getValue());
        slider.valueProperty().addListener((_obs, old, newVal) -> {
            lastValue = (double) newVal;
            text.setText(name + ": " + Math.round((double) newVal));
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(slider, text);
        return vBox;
    }
}
