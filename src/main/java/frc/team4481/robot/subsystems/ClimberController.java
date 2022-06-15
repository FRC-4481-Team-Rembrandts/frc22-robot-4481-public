package frc.team4481.robot.subsystems;

import frc.team4481.frclibrary4481.subsystems.SubsystemControllerBase;

public class ClimberController extends SubsystemControllerBase {

	private double turnSpeed = 0;
	private boolean isLocked = false;
	private boolean manualFlipOut = false;

	private boolean calibrated = false;
	private boolean autoClimbing = false;
	private boolean resetAutoClimber = false;
	private AUTO_CLIMBING_STATES AutoClimbingState = AUTO_CLIMBING_STATES.READY;

	private controlState currentControlState = controlState.DISABLED;

	public enum controlState {
		OPEN_LOOP,
		DISABLED
	}

	public enum AUTO_CLIMBING_STATES{
		READY(0),
		CLIMBING(1),
		HOOKING(2),
		EXTENDING(3),
		DONE(4);
		private int state;
		AUTO_CLIMBING_STATES(int state){
			this.state = state;
		}
	}

	/**
	 * get if the climber encoder calibrated?
	 * @return calibrated
	 */
	public boolean isCalibrated() {
		return calibrated;
	}
	/**
	 * set if the climber encoder calibrated?
	 * @param calibrated
	 */
	public void setCalibrated(boolean calibrated) {
		this.calibrated = calibrated;
	}

	/**
	 * get the current control state
	 * @return currentControlState
	 */
	public controlState getControlState() {
		return currentControlState;
	}
	/**
	 * set the current control state
	 * @param pControlState
	 */
	public void setControlState(controlState pControlState) {
		this.currentControlState = pControlState;
	}

	/**
	 * get the speed the climber arm needs to extend
	 * @return turnSpeed
	 */
	public double getTurnSpeed() {
		return turnSpeed;
	}
	/**
	 * set the speed the climber arm needs to extend
	 * @param turnSpeed
	 */
	public void setTurnSpeed(double turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	/**
	 * get the state of the mechanical lock for the arm
	 * / locked - unlocked
	 * @return isLocked
	 */
	public boolean isLocked() {
		return isLocked;
	}
	/**
	 * set the state of the mechanical lock for the arm
	 * / locked - unlocked
	 * @param locked
	 */
	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	/**
	 * get the state of the climber
	 * / auto - manual
	 * @return autoClimber
	 */
	public boolean getAutoClimbing(){return autoClimbing;}
	/**
	 * set the state of the climber
	 * / auto - manual
	 * @param AutoClimbing
	 */
	public void setAutoClimbing(boolean AutoClimbing){autoClimbing = AutoClimbing;}

	/**
	 * get the state if the climber need to be reset
	 * @return resetAutoClimber
	 */
	public boolean getResetAutoClimber(){return resetAutoClimber;}
	/**
	 * set the state if the climber need to be reset
	 * @param ResetAutoClimber
	 */
	public void setResetAutoClimber(boolean ResetAutoClimber){
		resetAutoClimber = ResetAutoClimber;
		resetAutoClimbingState();
	}

	/**
	 * get the state of the auto climber
	 * / ready - climbing - hooking - extending - done
	 * @return AutoClimbingState
	 */
	public AUTO_CLIMBING_STATES getAutoClimbingState(){return AutoClimbingState;}
	/**
	 * set the state of the auto climber
	 * / ready - climbing - hooking - extending - done
	 * @param state
	 */
	public void setAutoClimbingState(AUTO_CLIMBING_STATES state){
		if(AutoClimbingState.state < state.state){ //never go back;
			AutoClimbingState = state;
		}
	}

	/**
	 * reset auto climbing
	 */
	public void resetAutoClimbingState(){setAutoClimbingState(AUTO_CLIMBING_STATES.READY);}

	/**
	 * get if the flip out arm is extended
	 * @return manualFlipOut
	 */
	public boolean getManualFlipOut(){
		return manualFlipOut;
	}
	/**
	 * set if the flip out arm is extended
	 * / in - out
	 * @param pManualFlipOut
	 */
	public void setManualFlipOut(boolean pManualFlipOut){
		manualFlipOut = pManualFlipOut;
	}
}
