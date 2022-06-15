package frc.team4481.robot.subsystems;

import frc.team4481.frclibrary4481.subsystems.SubsystemControllerBase;

public class IntakeController extends SubsystemControllerBase {

    //subsystem external inputs

    private boolean intakeDeployed;
    private boolean intakeForwardEnabled;
    private boolean intakeReversedEnabled;

    private boolean intakeReversedSlowEnabled;

    private controlState currentControlState = controlState.DISABLED;

    public enum controlState {
        DISABLED,
        ENABLED
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
     * get the control state
     * / OPEN_LOOP - DISABLED
     * @return currentControlState
     */
    public controlState getControlState() {
        return currentControlState;
    }
    /**
     * set the intake motor direction forward
     * @param  pEnable
     */
    public void setIntakeForwardEnabled(boolean pEnable) {
        intakeForwardEnabled = pEnable;
    }
    /**
     * get the intake motor forward
     * @return intakeForwardEnabled
     */
    public boolean getIntakeForwardEnabled(){
        return intakeForwardEnabled;
    }

    /**
     * set the intake motor direction backwards
     * @param  pEnable
     */
    public void setIntakeReversedEnabled(boolean pEnable) {
        intakeReversedEnabled = pEnable;
    }
    /**
     * get the intake motor direction backwards
     * @return intakeReversedEnabled
     */
    public boolean getIntakeReversedEnabled(){
        return intakeReversedEnabled;
    }

    /**
     * set if the intake solenoid needs to be extended
     * @param  pDeployed
     */
    public void setIntakeDeployed(boolean pDeployed){
        intakeDeployed = pDeployed;
    }
    /**
     * get if the intake solenoid needs to be extended
     * @return intakeDeployed
     */
    public boolean getIntakeDeployed(){return intakeDeployed;}

    /**
     * set if the intake motor is in slow speed
     * @param intakeReversedSlowEnabled
     */
    public void setIntakeReversedSlowEnabled(boolean intakeReversedSlowEnabled) {
        this.intakeReversedSlowEnabled = intakeReversedSlowEnabled;
    }
    /**
     * get if the intake motor is in slow speed
     * @return intakeReversedSlowEnabled
     */
    public boolean getIntakeReversedSlowEnabled() {
        return intakeReversedSlowEnabled;
    }
}
