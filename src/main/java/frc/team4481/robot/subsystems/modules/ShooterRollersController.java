package frc.team4481.robot.subsystems.modules;

public class ShooterRollersController {
    private double ball1ShooterSpeed;
    private double ball2ShooterSpeed;
    private double ball1TopRollerSpeed;
    private double ball2TopRollerSpeed;
    private boolean velocityCheckDisabled;
    private boolean atSetpoint;

    void setAtSetpoint(boolean pAtSetpoint) {
        atSetpoint = pAtSetpoint;
    }

    boolean getAtSetpoint(){
        return atSetpoint;
    }

    public void setBall1Speed(double pBall1ShooterSpeed, double pBall1TopRollerSpeed) {
        ball1ShooterSpeed = pBall1ShooterSpeed;
        ball1TopRollerSpeed = pBall1TopRollerSpeed;
    }

    public double getBall1ShooterSpeed() {
        return ball1ShooterSpeed;
    }

    public double getBall1TopRollerSpeed() {
        return ball1TopRollerSpeed;
    }

    public void setBall2Speed(double pBall2ShooterSpeed, double pBall2TopRollerSpeed) {
        ball2ShooterSpeed = pBall2ShooterSpeed;
        ball2TopRollerSpeed = pBall2TopRollerSpeed;
    }

    public double getBall2ShooterSpeed() {
        return ball2ShooterSpeed;
    }

    public double getBall2TopRollerSpeed() {
        return ball2TopRollerSpeed;
    }

    public void setVelocityCheckDisabled(boolean pVelocityCheckDisabled) {
        velocityCheckDisabled = pVelocityCheckDisabled;
    }
    public boolean getVelocityCheckDisabled() {
        return velocityCheckDisabled;
    }

    private controlState currentControlState = controlState.IDLE;

    public enum controlState {
        IDLE,
        ENABLED
    }

    public void setControlState(controlState currentControlState) {
        this.currentControlState = currentControlState;
    }

    public controlState getControlState() {
        return currentControlState;
    }
}
