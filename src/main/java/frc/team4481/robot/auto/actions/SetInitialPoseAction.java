package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.path.Odometry;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Drivetrain;
import frc.team4481.robot.subsystems.DrivetrainController;

public class SetInitialPoseAction implements Action {
    private final Odometry mOdometry;
    private final Drivetrain mDrivetrain;
    private final DrivetrainController mDrivetrainController;

    public SetInitialPoseAction() {
        SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
        mDrivetrain = (Drivetrain) mSubsystemManager.getSubsystemByClass(Drivetrain.class);
        mDrivetrainController = mDrivetrain.getSubsystemController();

        mOdometry = mDrivetrainController.getOdometry();
    }

    @Override
    public void start() {

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
