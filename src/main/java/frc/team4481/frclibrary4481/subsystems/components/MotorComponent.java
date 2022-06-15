package frc.team4481.frclibrary4481.subsystems.components;

import edu.wpi.first.math.controller.PIDController;
import frc.team4481.robot.Constants;

public abstract class MotorComponent extends Component {
    double mNomVoltage, mFequency, mHorsepower, mMaxRPM, mKv, mStallTorque, mStallCurrent;

    PIDController mPidController;
    public double mFeedback;
    public MotorController.EncoderValue mExpectedValueType;

    public double mTargetValue;
    public double mkFeedforward;
    boolean mInverted;

    MotorComponent mFollowTarget;
    MotorController.ControlMode mControlMode;
    MotorController mMotorType;

    public MotorComponent(){
        mPidController = new PIDController(1,0,0, Constants.kLooperDt);
        mInverted=false;
        mFeedback = 0.0;
        mTargetValue = 0.0;

        mActivated = false;
        mControlMode = MotorController.ControlMode.PERCENTOUTPUT;
        mExpectedValueType = MotorController.EncoderValue.VELOCITY;
        System.out.println("Created: "+getClass());
    }

    /*
    Component Functions
     */

    @Override
    public void deactivate() {
        super.deactivate();
        mTargetValue = 0.0;
    }

    /*
    Motor Functions
     */
    //PID
    public PIDController getPidController(){return mPidController;}

    public void setPidController(double pP, double pI, double pD, double pFF, MotorController.EncoderValue pEncoderValue){
        mPidController.setPID(pP,pI,pD);
        mkFeedforward = pFF;
        mExpectedValueType = pEncoderValue;
    }
    public void setPidController(double pP, double pI, double pD, MotorController.EncoderValue pEncoderValue){
        mPidController.setPID(pP,pI,pD);
        mkFeedforward = 0;
        mExpectedValueType = pEncoderValue;
   }
    public void feedback(double pFeedback){
        mFeedback = pFeedback;
    }
    public boolean atSetpoint(){
        return mPidController.atSetpoint();
    }

    //Speed
    public double getTargetValue() {
        return mTargetValue;
    }
    public void setTargetValue(double pTargetSpeed) {
        mTargetValue = pTargetSpeed;
    }


    //ControlMode
    public void setControlMode(MotorController.ControlMode pControlMode){
        mControlMode = pControlMode;
    }
    public MotorController.ControlMode getControlMode() {
        return mControlMode;
    }

    //MultiMotor
    public void setFollowTarget(MotorComponent pFollowTarget) {
        mFollowTarget = pFollowTarget;
    }
    public MotorComponent getFollowTarget() {
        return mFollowTarget;
    }

    //MotorType
    public MotorController getMotorType(){
        return mMotorType;
    }
    abstract public Object getMotor();

    //Inversion
    public boolean isInverted() {
        return mInverted;
    }
    public void setInverted(boolean pInverted) {
        mInverted = pInverted;
    }

    //Encoders
    abstract public double getEncoderValue(MotorController.EncoderValue pValue);
    abstract public void resetEncoder();

    //BrakeModes

    abstract public void brakeMode();
    abstract public void coastMode();
}
