package frc.team4481.frclibrary4481.subsystems.components;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class SingleSparkMaxMotorComponent extends MotorComponent { // SimpleSingleMotorSubsystem?

    CANSparkMax mSpark = null;
    public double mSpeed =0.0;
    public SingleSparkMaxMotorComponent(int pPortID, boolean pInverted){
        super();
        mSpark = new CANSparkMax(pPortID, CANSparkMaxLowLevel.MotorType.kBrushless);
        mSpark.restoreFactoryDefaults();
        mSpark.follow(CANSparkMax.ExternalFollower.kFollowerDisabled, 0);

        mInverted = pInverted;

        mMotorType = MotorController.SPARKMAX;

        mNomVoltage = 12;   //V
        mMaxRPM = 6000;     //RPM
        mStallCurrent = 105; //A
        mStallTorque = 2.6; //Nm


    }

    @Override
    public void setControlMode(MotorController.ControlMode pControlMode) {
        super.setControlMode(pControlMode);
        if(mControlMode == MotorController.ControlMode.FOLLOWER){
            mSpark.follow((CANSparkMax) mFollowTarget.getMotor(),isInverted());
        }else{
            mSpark.setInverted(isInverted());
        }
    }

    @Override
    public void setInverted(boolean pInverted) {
        super.setInverted(pInverted);
        mSpark.setInverted(isInverted());
    }

    @Override
    public void update() {
        if(isActivated()){
            switch (mControlMode){
                case FOLLOWER:
                    break;
                case PERCENTOUTPUT:
                    mSpark.set(mTargetValue);
                    break;
                case PID:
                    mSpeed = mPidController.calculate(mFeedback, mTargetValue);
                    mSpark.set(mSpeed);
		            break;
                case PIDF:
                    mSpark.set(mPidController.calculate(mFeedback, mTargetValue) + mkFeedforward * mTargetValue);
		//TODO Fix low level PID(F) for faster computing and remove super

                    //LOW level
                    //switch (mExpectedValueType){
                    //    case POSITION:
                    //        mSpark.getPIDController().setReference(mTargetValue, ControlType.kPosition);
                    //        break;
                    //    case VELOCITY:
                    //        mSpark.getPIDController().setReference(mTargetValue, ControlType.kVelocity);
                    //        break;
                    //}

                    //PID Component
                    //mSpeed = mPidController.calculate(mFeedback, mTargetValue);
                    //mSpark.set(mSpeed);
            }
        }
    }


    @Override
    public CANSparkMax getMotor() {
        return mSpark;
    }

    @Override
    public void setPidController(double pP, double pI, double pD, MotorController.EncoderValue pEncoderValue) {
        //TODO remove super
        super.setPidController(pP, pI, pD, pEncoderValue);
        //LOW level
        mSpark.getPIDController().setP(mPidController.getP());
        mSpark.getPIDController().setI(mPidController.getI());
        mSpark.getPIDController().setD(mPidController.getD());
    }

    public double getEncoderValue(MotorController.EncoderValue pValue){
        switch (pValue){
            default:
                return 0.0;
            case POSITION:
                return mSpark.getEncoder().getPosition();
            case VELOCITY:
                return mSpark.getEncoder().getVelocity();
        }
    }
    @Override
    public void resetEncoder(){
        mSpark.getEncoder().setPosition(0.0);
        feedback(0.0);
        mSpeed = 0;
    }
    @Override
    public void brakeMode(){
        mSpark.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }
    @Override
    public void coastMode(){
        mSpark.setIdleMode(CANSparkMax.IdleMode.kCoast);
    }

    @Override
    public boolean atSetpoint() {
        return Math.abs(getEncoderValue(mExpectedValueType) - getTargetValue())< 20;
    }
}
