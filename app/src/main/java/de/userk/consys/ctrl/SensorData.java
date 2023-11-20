package de.userk.consys.ctrl;

class SensorData {
    public int frontLeft = 255;
    public int frontRight = 255;
    public int backLeft = 255;
    public int backRight = 255;

    public boolean isInitialized() {
        return frontLeft != 255
                && frontRight != 255
                && backLeft != 255
                && backRight != 255;
    }
}
