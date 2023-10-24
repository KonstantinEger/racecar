package de.userk.consys;

import static de.userk.testutils.Assert.not;
import static de.userk.testutils.Assert.assertThat;

public class MockMotor implements Motor {
    private boolean turnCalled = false;
    private boolean driveCalled = false;

    @Override
    public void turn(int amount) {
        turnCalled = true;
    }

    @Override
    public void drive(int speed) {
        driveCalled = true;
    }

    public void verifyNoInteractions() {
        not(() -> assertThat(driveCalled), "drive was not called");
        not(() -> assertThat(turnCalled), "turn was not called");
    }
}
