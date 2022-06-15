package frc.team4481.robot.auto.actions;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.path.AdaptivePurePursuitController;
import frc.team4481.frclibrary4481.path.Odometry;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Drivetrain;
import frc.team4481.robot.subsystems.DrivetrainController;

/**
 * Action to drive a predefined path using the Pure Pursuit algorithm.
 */
public class DrivePathAction implements Action {
    private final String mTrajectory;
    private final boolean mReversed;
    private double mMaxVelocity = -1;
    private double mMaxAcceleration = -1;

    private final AdaptivePurePursuitController mPpController;
    private final Odometry mOdometry;
    private final Drivetrain mDrivetrain;
    private final DrivetrainController mDrivetrainController;

    /**
     * Create a new {@code DrivePathAction} that will follow a {@code Trajectory} using the Pure Pursuit algorithm.
     *
     * @param pTrajectory Trajectory that the robot has to follow. Use the filename without extension from Path Planner
     * @param pReversed Whether the path should be followed in reverse
     */
    public DrivePathAction(String pTrajectory, boolean pReversed){
        mTrajectory = pTrajectory;
        mReversed = pReversed;

        SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
        mDrivetrain = (Drivetrain) mSubsystemManager.getSubsystemByClass(Drivetrain.class);
        mDrivetrainController = mDrivetrain.getSubsystemController();

        mOdometry = mDrivetrainController.getOdometry();
        mPpController = mDrivetrainController.getAPPController();
    }

    /**
     * Create a new {@code DrivePathAction} that will follow a {@code Trajectory} using the Pure Pursuit algorithm.
     *
     * @param pTrajectory Trajectory that the robot has to follow. Use the filename without extension from Path Planner
     * @param pReversed Whether the path should be followed in reverse
     */
    public DrivePathAction(String pTrajectory, boolean pReversed, double pMaxVelocity, double pMaxAcceleration){
        mTrajectory = pTrajectory;
        mReversed = pReversed;

        mMaxVelocity = pMaxVelocity;
        mMaxAcceleration = pMaxAcceleration;

        SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
        mDrivetrain = (Drivetrain) mSubsystemManager.getSubsystemByClass(Drivetrain.class);
        mDrivetrainController = mDrivetrain.getSubsystemController();

        mOdometry = mDrivetrainController.getOdometry();
        mPpController = mDrivetrainController.getAPPController();
    }

    @Override
    public void start() {
        Trajectory currentTrajectory;

        if(mMaxVelocity != -1 && mMaxAcceleration != -1) {
            currentTrajectory = mPpController.setNewTrajectory(mTrajectory, mReversed, mMaxVelocity, mMaxAcceleration);
        } else {
            currentTrajectory = mPpController.setNewTrajectory(mTrajectory, mReversed);
        }

        // Display trajectory on Driver Station
        mDrivetrain.mField.getObject("trajectory").setTrajectory(currentTrajectory);
        mDrivetrain.setBrakeMode();

        // If this path is the first path, move initial robot pose to first pose in current trajectory
        if (!mOdometry.hasInitialPose()) {
            System.out.println("------- Initial pose check -------");
            Pose2d initialPose = mPpController.getCurrentTrajectory().getInitialPose();
            mOdometry.reset(initialPose);
        }

        // Check whether last direction is different
        if(mReversed != mPpController.getPathReversed()) {
            mOdometry.reverse();
        }

        // Tell drivetrain to follow the path
        mDrivetrainController.setControlState(DrivetrainController.controlState.PATH_FOLLOWING);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return mPpController.isPathFinished();
    }

    @Override
    public void done() {
        mDrivetrainController.setControlState(DrivetrainController.controlState.DISABLED);
        mDrivetrain.mField.getObject("trajectory").setTrajectory(new Trajectory());
    }
}
