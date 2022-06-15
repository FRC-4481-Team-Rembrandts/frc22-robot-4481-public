package frc.team4481.frclibrary4481.subsystems.components;

import edu.wpi.first.math.controller.PIDController;

import java.util.Arrays;
import java.util.List;

public class MultiMotorComponent extends Component{
    List<MotorComponent> mMotors;

    public MultiMotorComponent(MotorComponent... pMotors) {
        mMotors = Arrays.asList(pMotors);
        for (int i = 1; i < mMotors.size(); i++) {
            mMotors.get(i).setFollowTarget(mMotors.get(0));
            mMotors.get(i).setControlMode(MotorController.ControlMode.FOLLOWER);
        }
    }

    @Override
    public void activate() {
        super.activate();
        mMotors.forEach(Component::activate);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        mMotors.forEach(Component::deactivate);
    }

    @Override
    public void update() {
        if (isActivated()) {
            mMotors.forEach(Component::update);
        }
    }

    public void setControlMode(MotorController.ControlMode pControlMode) {
        mMotors.get(0).setControlMode(pControlMode);
    }

    public void setInverted(boolean pInverted){
        mMotors.get(0).setInverted(pInverted);
    }
    public boolean isInverted(){
        return mMotors.get(0).isInverted();
    }

    public List<MotorComponent> getMotors() {
        return mMotors;
    }

    
    public double getEncoderValue(MotorController.EncoderValue pValue) {
        return mMotors.get(0).getEncoderValue(pValue);
    }

    public void resetEncoder() {
        mMotors.get(0).resetEncoder();
    }

    public MotorComponent getLeadMotorComponent(){

        return mMotors.get(0);
    }
    public Object getLeadMotor(){
        return mMotors.get(0).getMotor();
    }

    public void brakeMode() {
        mMotors.forEach(MotorComponent::brakeMode);
    }

    public void coastMode() {
        mMotors.forEach(MotorComponent::coastMode);
    }

    public void setPidController(double pP, double pI, double pD, MotorController.EncoderValue pEncoderValue) {
        mMotors.get(0).setPidController(pP, pI, pD, pEncoderValue);
    }
    public void setPidController(double pP, double pI, double pD, double pF, MotorController.EncoderValue pEncoderValue) {
        mMotors.get(0).setPidController(pP, pI, pD, pF, pEncoderValue);
    }
    public PIDController getPidController() {
        return mMotors.get(0).getPidController();
    }
    public boolean atSetpoint() {
        return  mMotors.get(0).atSetpoint();
    }

    public void feedback(double pFeedback) {
        mMotors.get(0).feedback(pFeedback);
    }

    public void setTargetValue(double pTargetSpeed) {
        mMotors.get(0).setTargetValue(pTargetSpeed);
    }
    public double getTargetValue() {
        return mMotors.get(0).getTargetValue();
    }
}
