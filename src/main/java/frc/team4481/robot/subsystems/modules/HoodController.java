package frc.team4481.robot.subsystems.modules;

public class HoodController {
    private double angle;
    public void setAngle(double pAngle) {
        angle = pAngle;
    }

    public double getAngle() {
        return angle;
    }


    private controlState currentControlState = controlState.DISABLED;

    public enum controlState {
        DISABLED,
        ENABLED
    }

    public void setControlState(controlState currentControlState) {
        this.currentControlState = currentControlState;
    }

    public controlState getControlState() {
        return currentControlState;
    }
}
