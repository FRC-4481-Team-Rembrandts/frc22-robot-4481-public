package frc.team4481.robot.subsystems;

import frc.team4481.frclibrary4481.path.AdaptivePurePursuitController;
import frc.team4481.frclibrary4481.path.Odometry;
import frc.team4481.frclibrary4481.subsystems.SubsystemControllerBase;

public class DrivetrainController extends SubsystemControllerBase {
    private double heading = 0;
    private double forward = 0;
    private double backward = 0;
    private boolean boost = false;
    private boolean brakeMode = true;
    private boolean snap;

    private controlState currentControlState = controlState.DISABLED;

    private AdaptivePurePursuitController mAPPController;
    private Odometry mOdometry;

    public enum controlState {
        DISABLED,
        GTA,
        PATH_FOLLOWING,
        VISION
    }

    /**
     * get if there needs to be made a foto
     * @return snap
     */
    public boolean getMakeSnapshot() {return snap;}
    /**
     * set if there needs to be made a foto
     * @param pSnap
     */
    public void setMakeSnapshot(boolean pSnap) {
        snap = pSnap;
    }

    /**
     * get the control state
     * / OPEN_LOOP - DISABLED
     * @return currentControlState
     */
    public controlState getControlState() {
        return currentControlState;
    }
    /**
     * set the control state
     * / OPEN_LOOP - DISABLED
     * @param pControlState
     */
    public void setControlState(controlState pControlState) {
        currentControlState = pControlState;
    }

    /**
     * get the input of the steering joystick
     * @return pHeading
     */
    public double getHeading() {
        return heading;
    }
    /**
     * set the input of the steering joystick
     * @param pHeading
     */
    public void setHeading(double pHeading) {
        heading = pHeading;
    }

    /**
     * get the input of the backwards button
     * @return pBackward
     */
    public double getBackward() {
        return backward;
    }
    /**
     * set the input of the backwards button
     * @param pBackward
     */
    public void setBackward(double pBackward) {
        backward = pBackward;
    }

    /**
     * get the input of the forward button
     * @return pforward
     */
    public double getForward() {
        return forward;
    }
    /**
     * set the input of the forward button
     * @param pForward
     */
    public void setForward(double pForward) {
        forward = pForward;
    }

    /**
     * get the input of the boost button
     * @return pBoost
     */
    public boolean isBoost() {
        return boost;
    }
    /**
     * set the input of the boost button
     * @param pBoost
     */
    public void setBoost(boolean pBoost) {
        boost = pBoost;
    }

    /**
     * get AdaptivePurePursuitController
     * @return mAPPController
     */
    public AdaptivePurePursuitController getAPPController() {
        return mAPPController;
    }
    /**
     * set AdaptivePurePursuitController
     * @param pPpController
     */
    public void setAPPController(AdaptivePurePursuitController pPpController) {
        mAPPController = pPpController;
    }

    /**
     * get the position and rotation of the robot
     * @return mOdometry
     */
    public Odometry getOdometry() {
        return mOdometry;
    }
    /**
     * set the position an rotation of the robot
     * @param pOdometry
     */
    public void setOdometry(Odometry pOdometry) {
        mOdometry = pOdometry;
    }

    public boolean isBrakeMode() {
        return brakeMode;
    }
    public void setBrakeMode(boolean brakeMode) {
        this.brakeMode = brakeMode;
    }
}
