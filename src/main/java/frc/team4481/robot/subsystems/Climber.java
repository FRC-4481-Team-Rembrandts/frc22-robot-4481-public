package frc.team4481.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.components.MotorController;
import frc.team4481.frclibrary4481.subsystems.components.SingleSparkMaxMotorComponent;
import frc.team4481.frclibrary4481.subsystems.components.SolenoidComponent;
import frc.team4481.frclibrary4481.util.CountingDelay;

import static frc.team4481.robot.Constants.*;

public class Climber extends SubsystemBase<ClimberController> {

    //Subsystem Components
    private final SingleSparkMaxMotorComponent mClimberMotor;
    private final SolenoidComponent mClimberBrake;
    private final SolenoidComponent mHighClimberSolenoid;

    private final CountingDelay mBrakeDelay;
    private final CountingDelay mHighHookDelay;

    private final AnalogInput mLimitSensorInput;
    private final Compressor mCompressor;
    private final RelativeEncoder mClimberThroughBore;

    //Subsystem Local Variable
    private double climberPosition;
    private double climberMotorSpeed = 0;

    public Climber() {
        mName = "Climber";

        mSubsystemController = new ClimberController();
		mClimberMotor = new SingleSparkMaxMotorComponent(CLIMBER_MOTOR,true);
		mClimberBrake = new SolenoidComponent(PneumaticsModuleType.REVPH, CLIMBER_BRAKE_SOLENOID_PORT);
        mHighClimberSolenoid = new SolenoidComponent(PneumaticsModuleType.REVPH, CLIMBER_HIGH_SOLENOID_PORT);
        mBrakeDelay = new CountingDelay();
        mHighHookDelay = new CountingDelay();
		mLimitSensorInput = new AnalogInput(CLIMBER_ANALOG_PORT);
        mCompressor = new Compressor(PneumaticsModuleType.REVPH);

        mClimberThroughBore = mClimberMotor.getMotor().getAlternateEncoder(8192);
    }

	@Override
	public void onStart(double timestamp) {
        // Subsystem startup routines
		zeroSensors();
        mBrakeDelay.reset();
        mHighHookDelay.reset();
        mSubsystemController.resetAutoClimbingState();
        mSubsystemController.setAutoClimbing(false);
        mSubsystemController.setCalibrated(false);
        mClimberMotor.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);
		mClimberMotor.brakeMode();
		mClimberMotor.activate();
        mClimberMotor.resetEncoder();
		mClimberBrake.deactivate();
        mClimberThroughBore.setInverted(true);
        mHighClimberSolenoid.deactivate();
        mCompressor.enableAnalog(90, 110);

		if (DriverStation.isAutonomous() || DriverStation.isTeleop()) {
			mSubsystemController.setControlState(ClimberController.controlState.OPEN_LOOP);
		} else {
			mSubsystemController.setControlState(ClimberController.controlState.DISABLED);
		}
	}

    @Override
    public void readPeriodicInputs() {
        climberPosition = -mClimberThroughBore.getPosition();
    }
    @Override
    public void onLoop(double timestamp) {
        switch (mSubsystemController.getControlState()) {
            case OPEN_LOOP:
                // Calibrate climber + encoder if needed
                if (!mSubsystemController.isCalibrated()) {
                    if (mCompressor.getPressure() > 60) { //don't calibrate until the compressor reaches this threshold
                        calibrateEncoder();
                    }
                } else if (mSubsystemController.getAutoClimbing()) {
                        FollowAutoClimberActions();
                } else {
                    // Move climber
                    if(mBrakeDelay.delay(CLIMBER_BRAKE_DELAY)){ //wait to take off mechanical break
                        mClimberMotor.setTargetValue(clampMotorInput(mSubsystemController.getTurnSpeed()));
                    }
                    if(mSubsystemController.getTurnSpeed()==0){ //set mechanical lock
                        mBrakeDelay.reset();
                        mSubsystemController.setLocked(true);
                    }else{
                        mSubsystemController.setLocked(false);
                    }
                    if (mSubsystemController.getResetAutoClimber()) { //reset auto climber
                        mSubsystemController.setResetAutoClimber(false);
                        mSubsystemController.resetAutoClimbingState();
                        ResetAutoClimber();
                    }

                    mHighHookDelay.reset();
                }
                // Mechanically lock motor with solenoid depending on state
                if (mSubsystemController.isLocked()) {
                    mClimberBrake.deactivate();
                } else {
                    mClimberBrake.activate();
                }


                if(mSubsystemController.getManualFlipOut()){
                    mHighClimberSolenoid.activate();
                }
                break;
        }
    }

    @Override
    public void writePeriodicOutputs() {
        // Update components
        mClimberMotor.update();
        mClimberBrake.update();
        mHighClimberSolenoid.update();
    }

    @Override
    public void onStop(double timestamp) {
        terminate();
    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void terminate() {
        mClimberMotor.setTargetValue(0);
        mClimberMotor.deactivate();
    }

    @Override
    public void outputData() {
        SmartDashboard.putString("MH_state", mSubsystemController.getControlState().toString());
        SmartDashboard.putNumber("MH_climber position", climberPosition);
        SmartDashboard.putNumber("MH_Sensor", mLimitSensorInput.getValue());
        SmartDashboard.putNumber("MH_Sensor_POS", mClimberMotor.getEncoderValue(MotorController.EncoderValue.POSITION));
        SmartDashboard.putBoolean("MH_CAL", mSubsystemController.isCalibrated());
        SmartDashboard.putString("MH_AutoState", mSubsystemController.getAutoClimbingState().toString());
        SmartDashboard.putBoolean("MH_AutoClimbing", mSubsystemController.getAutoClimbing());
        SmartDashboard.putNumber("MH_Encoder", mClimberThroughBore.getPosition());
        SmartDashboard.putNumber("Compressor", mCompressor.getPressure());
        SmartDashboard.putNumber("Input", mSubsystemController.getTurnSpeed());
    }


    private void ResetAutoClimber(){
        mHighClimberSolenoid.deactivate();
    }

    /**
     * if Solenoid is extended lower robot
     * if Soleniod is contracted raise robot
     */
    private void FollowAutoClimberActions(){
        switch (mSubsystemController.getAutoClimbingState()){
            case READY:
                mSubsystemController.setAutoClimbingState(ClimberController.AUTO_CLIMBING_STATES.CLIMBING);
                break;
            case CLIMBING:
                mSubsystemController.setLocked(false);
                 climberMotorSpeed = CLIMBER_AUTO_DOWN_SPEED;
                if(climberPosition <= CLIMBER_AUTO_HOOK_POINT){//wait for Hook to be below correct height
                    mSubsystemController.setAutoClimbingState(ClimberController.AUTO_CLIMBING_STATES.HOOKING);
                }
                break;
            case HOOKING:
                mHighClimberSolenoid.activate();
                mSubsystemController.setLocked(false);
                if(climberPosition <= CLIMBER_AUTO_LOWER_LIMIT){
                    climberMotorSpeed = 0;
                }else{
                    climberMotorSpeed = CLIMBER_AUTO_DOWN_SPEED;
                }
                if(mHighHookDelay.delay(CLIMBER_HIGH_HOOK_DELAY)){ //wait for hook to be extended
                    mSubsystemController.setAutoClimbingState(ClimberController.AUTO_CLIMBING_STATES.EXTENDING);
                }
                break;
            case EXTENDING:
                climberMotorSpeed = CLIMBER_AUTO_UP_SPEED;
                mSubsystemController.setLocked(false);
                if(climberPosition >= CLIMBER_AUTO_UPPER_LIMIT){ //wait for Hook to be above correct height
                    mSubsystemController.setAutoClimbingState(ClimberController.AUTO_CLIMBING_STATES.DONE);
                }
                break;
            case DONE:
                climberMotorSpeed = 0;
                mSubsystemController.setAutoClimbing(false);
                mSubsystemController.setLocked(true);
                break;
        }
        if(climberMotorSpeed != 0){
            if(mBrakeDelay.delay(CLIMBER_BRAKE_DELAY)){ //wait to take off mechanical break
                mClimberMotor.setTargetValue(clampMotorInput(climberMotorSpeed));
            }
        } else {
            mBrakeDelay.reset();
            mClimberMotor.setTargetValue(0);
        }
    }

    /**
     * If the climber has reached a limit, output 0, if not, output input
     *
     * @param input unclamped input to be sent to the motor
     * @return clamped motor input that adheres to set limits
     */
    private double clampMotorInput(double input) {
        boolean belowUpperLimit = climberPosition < CLIMBER_UPPER_LIMIT || mLimitSensorInput.getValue() >= CLIMBER_SENSOR_LIMIT;
        boolean aboveLowerLimit = climberPosition > CLIMBER_LOWER_LIMIT;
        boolean positiveInput = input > 0;
        boolean negativeInput = input < 0;

        if ((negativeInput && belowUpperLimit) || (positiveInput && aboveLowerLimit)) {
            return input;
        } else {
            return 0;
        }
    }

    /**
     * Retracts the climber to the lowest point and resets the encoder when at the bottom
     */
    private void calibrateEncoder() {
        if (mLimitSensorInput.getValue() >= CLIMBER_SENSOR_LIMIT) {
            mSubsystemController.setCalibrated(true);

            mClimberMotor.setTargetValue(0.0);
            climberPosition = 0;
            mSubsystemController.setLocked(true);
            mClimberThroughBore.setPosition(0.0);
            mClimberMotor.resetEncoder();
            System.out.println("RESET");
            return;
        }
        mSubsystemController.setLocked(false);

        if(mBrakeDelay.delay(CLIMBER_BRAKE_DELAY)) {
            mClimberMotor.setTargetValue(CLIMBER_MOTOR_CALIBRATION_POWER);
        }

        SmartDashboard.putBoolean("MH_MotorAct", mBrakeDelay.delay(CLIMBER_BRAKE_DELAY));
    }
}
