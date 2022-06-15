package frc.team4481.robot.subsystems.modules;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.frclibrary4481.subsystems.components.MotorController;
import frc.team4481.frclibrary4481.subsystems.components.SingleSparkMaxMotorComponent;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;

import static frc.team4481.robot.Constants.*;

public class Hood extends SubsystemBase<HoodController> {
    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    private Shooter mShooter;
    private ShooterController mShooterController;
    private double angle;
    private double demand;

    private DutyCycleEncoder mAbsoluteEncoder;

    private SingleSparkMaxMotorComponent mHood;
    private PIDController mHoodPID;

    private DigitalInput mDig;
    public Hood(){
        mName = "Hood";
        mSubsystemController = new HoodController();

        mHood = new SingleSparkMaxMotorComponent(HOOD_MOTOR, HOOD_INVERT);

        mHoodPID = new PIDController(HOOD_KP, HOOD_KI, HOOD_KD);
        mAbsoluteEncoder = new DutyCycleEncoder(HOOD_ENCODER);

        int amp = 25;
        mHood.getMotor().setSmartCurrentLimit(amp);
    }

    @Override
    public void onStart(double timestamp) {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();

        mHood.activate();
        mHood.brakeMode();
        mHood.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);
    }

    @Override
    public void readPeriodicInputs() {

    }

    @Override
    public void onLoop(double timestamp) {
        switch (mSubsystemController.getControlState()) {
            case DISABLED:
                demand = 0;
                mShooterController.setHoodComplete(false);
                break;
            case ENABLED:
                // Calculate motor demand based on desired angle
                angle = mSubsystemController.getAngle();
                demand = mHoodPID.calculate(mAbsoluteEncoder.get(), angle);

                // Return true if hood is within bounds
                mShooterController.setHoodComplete(
                        Math.abs(mAbsoluteEncoder.get() - angle) <
                                HOOD_ERROR_MARGIN
                );
                break;
        }
    }

    @Override
    public void writePeriodicOutputs() {
        /* TODO: Write angle */
        mHood.setTargetValue(demand);
        mHood.update();
    }

    @Override
    public void onStop(double timestamp) {

    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void terminate() {
        mHood.deactivate();
    }

    @Override
    public void outputData() {
        SmartDashboard.putNumber("H_AbsEncoder", mAbsoluteEncoder.get());
        SmartDashboard.putNumber("H_Setpoint", angle);
        SmartDashboard.putString("H_Enabled", getSubsystemController().getControlState().toString());

        if(mShooterController != null)
            SmartDashboard.putBoolean("H_Complete", mShooterController.getHoodComplete());

        SmartDashboard.putNumber("H_Current", mHood.getMotor().getOutputCurrent());
    }
}
