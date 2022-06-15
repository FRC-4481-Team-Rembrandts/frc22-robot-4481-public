package frc.team4481.robot.subsystems.modules;

public class OuttakeController {
    private controlState currentControlState = controlState.IDLE;

    public enum controlState {
        IDLE,
        SHOOT,
    }

    public void setControlState(controlState currentControlState) {
        this.currentControlState = currentControlState;
    }

    public controlState getControlState() {
        return currentControlState;
    }
}