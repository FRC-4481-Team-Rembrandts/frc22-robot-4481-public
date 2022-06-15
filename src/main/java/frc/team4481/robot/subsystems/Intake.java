package frc.team4481.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.components.*;
import static frc.team4481.robot.Constants.*;


public class Intake extends SubsystemBase<IntakeController> {

    //Subsystem Components
    SolenoidComponent mSolenoidComponent;
    SingleSparkMaxMotorComponent intakeMotor;

    public Intake(){
        mName = "Intake";
        this.mSubsystemController = new IntakeController();
        mSolenoidComponent = new SolenoidComponent(PneumaticsModuleType.REVPH, INTAKE_SOLENOID);
        intakeMotor = new SingleSparkMaxMotorComponent(INTAKE_LEAD_MOTOR,INTAKE_LEAD_MOTOR_INVERT);
    }

    @Override
    public void onStart(double timestamp) {
        // Subsystem startup routines
        zeroSensors();
        intakeMotor.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);
        intakeMotor.coastMode();
        intakeMotor.activate();

        if (DriverStation.isAutonomous() || DriverStation.isTeleop()){
            mSubsystemController.setControlState(IntakeController.controlState.ENABLED);
        }else {
            mSubsystemController.setControlState(IntakeController.controlState.DISABLED);
        }
    }

    @Override
    public void readPeriodicInputs() {

    }

    @Override
    public void onLoop(double timestamp) {
        switch (mSubsystemController.getControlState()) {
            case DISABLED:
                terminate();
            case ENABLED:
                // Check intake direction
                if (mSubsystemController.getIntakeForwardEnabled()) {
                    intakeMotor.setTargetValue(INTAKE_SPEED);
                } else if (mSubsystemController.getIntakeReversedSlowEnabled()) {
                    intakeMotor.setTargetValue(INTAKE_REVERSE_SPEED/2.0);
                } else if (mSubsystemController.getIntakeReversedEnabled()) {
                    intakeMotor.setTargetValue(INTAKE_REVERSE_SPEED);
                } else {
                    intakeMotor.setTargetValue(0);
                }

                // Latch solenoid
                if(mSubsystemController.getIntakeDeployed()){
                    mSolenoidComponent.activate();
                }else{
                    mSolenoidComponent.deactivate();
                }

                break;
        }

    }

    @Override
    public void writePeriodicOutputs() {
        mSolenoidComponent.update();
        intakeMotor.update();
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
        //Terminate intake motors and deactivate arm
        intakeMotor.setTargetValue(0);
        mSolenoidComponent.deactivate();

        mSubsystemController.setControlState(IntakeController.controlState.DISABLED);

    }

    @Override
    public void outputData() {
        SmartDashboard.putString("I_State", mSubsystemController.getControlState().toString());
        SmartDashboard.putBoolean("I_Out", mSolenoidComponent.isActivated());

        SmartDashboard.putBoolean("I_Latch", mSolenoidComponent.isActivated());
        SmartDashboard.putNumber("I_Intake speed",intakeMotor.getTargetValue());
        SmartDashboard.putBoolean("I_Intake Deployed", mSubsystemController.getIntakeDeployed());

    }
}
