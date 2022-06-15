package frc.team4481.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.modules.Outtake;
import frc.team4481.robot.subsystems.modules.OuttakeController;
import frc.team4481.robot.subsystems.modules.Storage;
import frc.team4481.robot.subsystems.modules.StorageController;


import java.util.Arrays;
import java.util.List;

public class Shooter extends SubsystemBase<ShooterController> {
    private List<SubsystemBase> mShooterModules;
    private Storage mStorage;

    private Outtake mOuttake;


    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    private Intake mIntake;
    private IntakeController mIntakeController;
    private Drivetrain mDrivetrain;
    private DrivetrainController mDrivetrainController;

    private boolean mismatchLatch;

    public Shooter(){
        mName = "Shooter";
        mSubsystemController = new ShooterController();

        mStorage = new Storage();
        mOuttake = new Outtake();
        mShooterModules = Arrays.asList(mStorage, mOuttake);
    }

    @Override
    public void onStart(double timestamp) {
        if (DriverStation.isAutonomous()){
            mSubsystemController.setControlState(ShooterController.ControlState.ENABLED);
        }else if(DriverStation.isTeleop()){
            mSubsystemController.setControlState(ShooterController.ControlState.ENABLED);
        }else{
            mSubsystemController.setControlState(ShooterController.ControlState.DISABLED);
        }

        mShooterModules.forEach(s -> s.onStart(timestamp));

        // Get intake reference
        mIntake = (Intake) mSubsystemManager.getSubsystemByClass(Intake.class);
        mIntakeController = mIntake.getSubsystemController();
        // Get intake reference
        mDrivetrain = (Drivetrain) mSubsystemManager.getSubsystemByClass(Drivetrain.class);
        mDrivetrainController = mDrivetrain.getSubsystemController();
    }

    @Override
    public void onLoop(double timestamp) {
        switch (mSubsystemController.getControlState()) {
            case DISABLED:
                mStorage.getSubsystemController().setControlState(StorageController.controlState.IDLE);
                mOuttake.getSubsystemController().setControlState(OuttakeController.controlState.IDLE);
                break;
            case ENABLED:

                /* ---------------------------------------- */
                /* Storage Behaviour */
                /* ---------------------------------------- */

                /* Check whether reverse intake takes place */
                if (mSubsystemController.getReverse()) {
                    mStorage.getSubsystemController().setControlState(StorageController.controlState.REVERSE);
                    mSubsystemController.setSecondBallMismatch(false);
                    mDrivetrainController.setMakeSnapshot(false);

                /* Shoot balls without waiting for a ball to reach the sensor */
                } else if (
                        mSubsystemController.getShootActive() &&
                        mSubsystemController.getAtSetpoint() &&
                        mSubsystemController.getHoodComplete() &&
                        mSubsystemController.isVisionAtSetpoint()
                ) {
                    mDrivetrainController.setMakeSnapshot(true);

                    mStorage.getSubsystemController().setControlState(StorageController.controlState.SHOOT);

                /* If there is a ball in the first slot, check for color filtering */
                } else if (mStorage.getSubsystemController().getFirstBallPositioned()) {
                    mDrivetrainController.setMakeSnapshot(false);

                    /* When color filtering is active filter ball if necessary */
                    if (mSubsystemController.getColorFilterActive() && mStorage.getSubsystemController().getMismatchBall1()) {
                        mStorage.getSubsystemController().setControlState(StorageController.controlState.FILTER);
                        System.out.println("hello");

                    /* Go to idle */
                    } else if (!mSubsystemController.getShootActive()){
                        mSubsystemController.setSecondBallMismatch(false);
                        // Put storage back to idle
                        mStorage.getSubsystemController().setControlState(StorageController.controlState.IDLE);
                    }

                /* If there is no ball in the first slot or if the storage is enabled we need to feed the ball to
                   the desired height */
                } else if (mSubsystemController.getStorageActive() || mSubsystemController.getShootActive()){
                    mDrivetrainController.setMakeSnapshot(false);

                    mStorage.getSubsystemController().setControlState(StorageController.controlState.FEED);
                    mSubsystemController.setSecondBallMismatch(false);

                /* If nothing happens, the storage should idle */
                } else {
                    mDrivetrainController.setMakeSnapshot(false);

                    mStorage.getSubsystemController().setControlState(StorageController.controlState.IDLE);
                    mSubsystemController.setSecondBallMismatch(false);
                }

                /* ---------------------------------------- */
                /* Outtake Behaviour */
                /* ---------------------------------------- */

                /* Check if the desired shoot state can be performed */
                if (mSubsystemController.getColorFilterActive() && mStorage.getSubsystemController().getMismatchBall1()) {
                    mSubsystemController.setShootState(ShooterController.ShootState.FILTER);
                    mismatchLatch = true;
                } else if (!mismatchLatch) {
                    mSubsystemController.setShootState(mSubsystemController.getDesiredShootState());
                }

                /* Change outtake state */
                if (
                        (mSubsystemController.getColorFilterActive() && mStorage.getSubsystemController().getMismatchBall1()) ||
                        mSubsystemController.getOuttakeActive()
                ) {
                    mOuttake.getSubsystemController().setControlState(OuttakeController.controlState.SHOOT);
                } else {
                    mOuttake.getSubsystemController().setControlState(OuttakeController.controlState.IDLE);
                    mismatchLatch = false;
                }
        }

        mShooterModules.forEach(s -> s.onLoop(timestamp));
    }

    @Override
    public void onStop(double timestamp) {
        mShooterModules.forEach(s -> s.onStop(timestamp));
    }

    @Override
    public void readPeriodicInputs() {
        mShooterModules.forEach(SubsystemBase::readPeriodicInputs);
    }

    @Override
    public void writePeriodicOutputs() {
        mShooterModules.forEach(SubsystemBase::writePeriodicOutputs);
    }

    @Override
    public void zeroSensors() {
        mShooterModules.forEach(SubsystemBase::zeroSensors);
    }

    @Override
    public void terminate() {
        mShooterModules.forEach(SubsystemBase::terminate);
    }

    @Override
    public void outputData() {
        SmartDashboard.putNumber("Shots", mSubsystemController.getNumberOfShots());
        SmartDashboard.putBoolean("ShotActive", mSubsystemController.getShootActive());
        SmartDashboard.putBoolean("Setpoint", mSubsystemController.getAtSetpoint());
        SmartDashboard.putBoolean("VisionSet", mSubsystemController.isVisionAtSetpoint());
        SmartDashboard.putBoolean("Hood", mSubsystemController.getHoodComplete());
        SmartDashboard.putBoolean("Color Filter Active", mSubsystemController.getColorFilterActive());
        SmartDashboard.putString("Acolor", mSubsystemController.getAllianceColor().toString());
        mShooterModules.forEach(SubsystemBase::outputData);
    }
}
