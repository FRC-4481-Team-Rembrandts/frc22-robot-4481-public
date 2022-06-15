package frc.team4481.frclibrary4481.subsystems.components;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.team4481.frclibrary4481.throwable.HardwareException;


public class SingleVictorMotorComponent extends MotorComponent { // SimpleSingleMotorSubsystem?
    VictorSPX mVictor = null;
    public SingleVictorMotorComponent(int pPortID, boolean pInverted){
        super();
        mVictor = new VictorSPX(pPortID);
        mInverted = pInverted;
        mMotorType = MotorController.VICTORSPX;
    }

    @Override
    public void setControlMode(MotorController.ControlMode pControlMode) {
        super.setControlMode(pControlMode);
        if(mControlMode == MotorController.ControlMode.FOLLOWER){
            mVictor.follow((IMotorController) mFollowTarget.getMotor());
            if(isInverted()){
                mVictor.setInverted(InvertType.OpposeMaster);
            }else{
                mVictor.setInverted(InvertType.FollowMaster);
            }
        }else{
            mVictor.setInverted(isInverted());
        }
    }

    @Override
    public void setInverted(boolean pInverted) {
        super.setInverted(pInverted);
        mVictor.setInverted(isInverted());
    }

    @Override
    public void update() {
        if(isActivated()){
            switch (mControlMode){
                case FOLLOWER:
                    break;
                case PERCENTOUTPUT:
                    mVictor.set(VictorSPXControlMode.PercentOutput, mTargetValue);
                    break;
                case PID:
                    mVictor.set(ControlMode.PercentOutput, mPidController.calculate(mFeedback, getTargetValue()));
            }
        }
    }

    @Override
    public VictorSPX getMotor() {
        return mVictor;
    }

    @Override
    public double getEncoderValue(MotorController.EncoderValue pValue) {
        try {
            throw new HardwareException("Victor Encoder", "A Victor does not have an onboard Encoder");
        } catch (HardwareException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void resetEncoder() {
        try {
            throw new HardwareException("Victor Encoder", "A Victor does not have an onboard Encoder");
        } catch (HardwareException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void brakeMode() {
        mVictor.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void coastMode() {
        mVictor.setNeutralMode(NeutralMode.Coast);
    }

}
