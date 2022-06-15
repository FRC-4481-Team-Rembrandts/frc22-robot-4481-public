package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Drivetrain;
import frc.team4481.robot.subsystems.DrivetrainController;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;

public class ShootStopAction implements Action {
    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Shooter mShooter;
    ShooterController mShooterController;
    ShooterController.ShootState mShootState;
    Drivetrain mDrivetrain;
    DrivetrainController mDrivetrainController;

    /**
     * Creates a {@code ShooterStopAction}
     */
    public ShootStopAction() {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();
        mDrivetrain = (Drivetrain) mSubsystemManager.getSubsystemByClass(Drivetrain.class);
        mDrivetrainController = mDrivetrain.getSubsystemController();
    }

    @Override
    public void start() {
        mShooterController.setOuttakeActive(false);
        mShooterController.setShootActive(false);
        mShooterController.setControlState(ShooterController.ControlState.DISABLED);
        if(mShootState == ShooterController.ShootState.AUTOMATIC)
            mDrivetrainController.setControlState(DrivetrainController.controlState.DISABLED);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void done() {

    }
}
