package frc.team4481.frclibrary4481.subsystems.components;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team4481.robot.Constants;


public class SingleTalonMotorComponent extends MotorComponent { // SimpleSingleMotorSubsystem?
    enum EncoderType{
        Quad
    }

    TalonSRX mTalon = null;
    public SingleTalonMotorComponent(int pPortID, boolean pInverted){
        super();
        mTalon = new TalonSRX(pPortID);
        mInverted = pInverted;
        mMotorType = MotorController.TALONSRX;
    }

    @Override
    public void setControlMode(MotorController.ControlMode pControlMode) {
        super.setControlMode(pControlMode);
        if(mControlMode == MotorController.ControlMode.FOLLOWER){
            mTalon.follow((IMotorController) mFollowTarget.getMotor());
            if(isInverted()){
                mTalon.setInverted(InvertType.OpposeMaster);
            }else{
                mTalon.setInverted(InvertType.FollowMaster);
            }
        }else{
            mTalon.setInverted(isInverted());
        }
    }

    @Override
    public void setInverted(boolean pInverted) {
        super.setInverted(pInverted);
        mTalon.setInverted(isInverted());
    }

    public void setSensor(EncoderType pEncoderType){
        mTalon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, (int) Math.round(Constants.kLooperDt * 1000));
        switch (pEncoderType){
            case Quad:
                mTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
                break;
        }
    }

    @Override
    public void update() {
        if(isActivated()){
            switch (mControlMode){
                case CURRENT:
                    mTalon.set(TalonSRXControlMode.Current, mTargetValue);
                    break;
                case FOLLOWER:
                    break;
                case PERCENTOUTPUT:
                    mTalon.set(TalonSRXControlMode.PercentOutput, mTargetValue);
                    break;
                case PID:
                    //TODO Integrated Talon PID Mode
                    mTalon.set(ControlMode.PercentOutput, mPidController.calculate(mFeedback,getTargetValue()));
                    break;
            }
        }
    }

    @Override
    public TalonSRX getMotor() {
        return mTalon;
    }

    public double getEncoderValue(MotorController.EncoderValue pValue){
        switch (pValue){
            default:
                return 0.0;
            case POSITION:
                return mTalon.getSelectedSensorPosition();
            case VELOCITY:
                return mTalon.getSelectedSensorVelocity();
        }
    }

    @Override
    public void resetEncoder() {
        mTalon.setSelectedSensorPosition(0);
    }


    @Override
    public void brakeMode() {
        mTalon.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void coastMode() {
        mTalon.setNeutralMode(NeutralMode.Coast);
    }
}
