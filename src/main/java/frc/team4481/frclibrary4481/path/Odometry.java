package frc.team4481.frclibrary4481.path;

import edu.wpi.first.math.geometry.*;

/**
 * The odometry class can be used to parse data from motor encoders and an IMU or gyroscope
 * to determine the current horizontal and vertical distance from the starting point in meters.
 * Additionally, the heading compared to the starting heading is also saved.
 * This position and theta can be found in the {@code currentPose}.
 */
public class Odometry {
    private Pose2d mCurrentPose;
    private double metersTraveledLeft = 0;
    private double metersTraveledRight = 0;
    private double previousHeading = 0;
    private boolean hasInitialPose = false;

    /**
     * Constructs a new Odometry object with starting position (0,0) and heading 0
     */
    public Odometry() {
        reset();
    }

    /**
     * Gets current robot {@code pose}
     *
     * @return Current robot {@code Pose}
     */
    public Pose2d getPose() {
        return mCurrentPose;
    }

    /**
     * Checks if the robot has an initial pose or not
     *
     * @return Whether the odometry has an initial pose
     */
    public boolean hasInitialPose() {return hasInitialPose;}

    /**
     * Resets the internal pose of the odometry to (0,0) with heading 0
     */
    public void reset() {
        mCurrentPose = new Pose2d(new Translation2d(0, 0), new Rotation2d(0));
    }

    /**
     * Resets the internal pose of the odometry to the specified starting {@code Pose2d}
     *
     * @param pStartingPose Initial {@code Pose2d} of the robot
     */
    public void reset(Pose2d pStartingPose) {
        mCurrentPose = pStartingPose;
        hasInitialPose = true;
    }

    /**
     * Rotates the robot pose by 180 degrees
     */
    public void reverse() {
        Transform2d halfCircle = new Transform2d(new Translation2d(), new Rotation2d(Math.PI));

        mCurrentPose = mCurrentPose.transformBy(halfCircle);
    }

    /**
     * Updates {@code Pose2d currentPose} of robot by calculating an arc based on distance driven and heading change
     *
     * @param pLeftEncoder  Total distance traveled in meters of the left side of the drivetrain since start of path
     * @param pRightEncoder Total distance traveled in meters of the right side of the drivetrain since start of path
     * @param pHeading      Current heading of the robot in radians
     *
     * @see Twist2d
     */
    public void updatePose(double pRightEncoder, double pLeftEncoder, double pHeading) {
        double distanceDelta = getDistanceDelta(pLeftEncoder, pRightEncoder);
        double headingDelta = getHeadingDelta(pHeading);

        Twist2d displacementTwist = new Twist2d(distanceDelta, 0, headingDelta);

        mCurrentPose = mCurrentPose.exp(displacementTwist);
    }

    /**
     * Calculates average distance traveled between last function call
     *
     * @param pLeftEncoder  Left encoder value in meters
     * @param pRightEncoder Right encoder value in meters
     * @return Meters traveled since last call
     */
    private double getDistanceDelta(double pLeftEncoder, double pRightEncoder) {
        // Calculate delta
        double deltaLeft = pLeftEncoder - metersTraveledLeft;
        double deltaRight = pRightEncoder - metersTraveledRight;

        // Set meters traveled to encoder values
        metersTraveledLeft = pLeftEncoder;
        metersTraveledRight = pRightEncoder;

        // Return average between left and right delta
        return (deltaLeft + deltaRight) / 2.0;
    }

    /**
     * Calculates the difference in heading between the current en previous calculation
     *
     * @param pCurrentHeading Current robot heading in radians
     * @return Difference in heading between the current and previous robot position
     */
    private double getHeadingDelta(double pCurrentHeading) {
        // Calculate delta
        double heading = pCurrentHeading - previousHeading;

        // Save heading buffer
        previousHeading = pCurrentHeading;

        // Return heading delta
        return heading;
    }
}
