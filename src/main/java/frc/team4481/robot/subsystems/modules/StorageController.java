package frc.team4481.robot.subsystems.modules;

import frc.team4481.frclibrary4481.subsystems.SubsystemControllerBase;

public class StorageController extends SubsystemControllerBase {
    private boolean mismatchBall1;
    private boolean firstBallPositioned;
    private boolean ballInOuttake;

    public void setMismatchBall1(boolean pMismatchBall1) {
        mismatchBall1 = pMismatchBall1;
    }
    public boolean getMismatchBall1() {
        return mismatchBall1;
    }

    public void setFirstBallPositioned(boolean pFirstBallPositioned) {
        firstBallPositioned = pFirstBallPositioned;
    }
    public boolean getFirstBallPositioned() {
        return firstBallPositioned;
    }

    //TODO: program sensor to call this setter
    public void setBallInOuttake(boolean pBallInOuttake) {
        ballInOuttake = pBallInOuttake;
    }
    public boolean getBallInOuttake() {
        return ballInOuttake;
    }

    private controlState currentControlState = controlState.IDLE;

    public enum controlState {
        IDLE,
        FEED,
        REVERSE,
        FILTER,
        SHOOT
    }

    public void setControlState(StorageController.controlState currentControlState) {
        this.currentControlState = currentControlState;
    }

    public StorageController.controlState getControlState() {
        return currentControlState;
    }
}
