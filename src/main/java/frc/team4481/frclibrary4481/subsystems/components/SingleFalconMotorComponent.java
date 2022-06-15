package frc.team4481.frclibrary4481.subsystems.components;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class SingleFalconMotorComponent extends MotorComponent{
	WPI_TalonFX mFalcon;
	public SingleFalconMotorComponent(int pPortID, boolean pInverted){
		super();
		mFalcon = new WPI_TalonFX(pPortID);
		mFalcon.configFactoryDefault();
		mInverted = pInverted;
		mMotorType = MotorController.FALCON;
	}

	@Override
	public void setControlMode(MotorController.ControlMode pControlMode) {
		super.setControlMode(pControlMode);
		if(mControlMode == MotorController.ControlMode.FOLLOWER){
			mFalcon.follow((IMotorController) mFollowTarget.getMotor());
			if(isInverted()){
				mFalcon.setInverted(InvertType.OpposeMaster);
			}else{
				mFalcon.setInverted(InvertType.FollowMaster);
			}
		}else{
			mFalcon.setInverted(isInverted());
		}
	}

	@Override
	public void setInverted(boolean pInverted) {
		super.setInverted(pInverted);
		mFalcon.setInverted(isInverted());
	}

	@Override
	public void update() {
		if(isActivated()){
			switch (mControlMode){
				case FOLLOWER:
					break;
				case PERCENTOUTPUT:
					mFalcon.set(TalonFXControlMode.PercentOutput, mTargetValue);
					break;
				case PID:
					mFalcon.set(ControlMode.PercentOutput, mPidController.calculate(mFeedback, getTargetValue()));
			}

		}
	}

	@Override
	public WPI_TalonFX getMotor() { return mFalcon; }

	@Override
	public double getEncoderValue(MotorController.EncoderValue pValue) {
		switch (pValue){
			default:
				return 0.0;
			case POSITION:
				return mFalcon.getSelectedSensorPosition();
			case VELOCITY:
				return mFalcon.getSelectedSensorVelocity();
		}
	}

	@Override
	public void resetEncoder() {
		mFalcon.setSelectedSensorPosition(0);
	}
	@Override
	public void brakeMode() { mFalcon.setNeutralMode(NeutralMode.Brake); }

	@Override
	public void coastMode() { mFalcon.setNeutralMode(NeutralMode.Coast); }
}
