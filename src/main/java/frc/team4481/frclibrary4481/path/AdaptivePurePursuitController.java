package frc.team4481.frclibrary4481.path;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.trajectory.Trajectory;
import static frc.team4481.robot.Constants.*;

import java.util.ArrayList;

/**
 * The AdaptivePurePursuitController class implements the Pure Pursuit algorithm for tank drive.
 * This algorithm computes a trajectory curve towards the {@code Translate2d} at a set offset
 * from the current {@code Translate2d} of the robot. This offset is called the lookahead distance.
 * <p>
 * This curve is then used in combination with a target velocity to calculate separate target velocities
 * for the left and right sides of the {@code Drivetrain} in meters/second.
 * <p>
 * Before a {@code Trajectory} can be followed, it first has to be set using {@code setNewTrajectory()}.
 * After that, a new {@code MotorDemand} has to be calculated every cycle for optimal robot performance.
 * <p>
 * A path is finished when the closest point on the path to the robot is the last point in the path and the distance
 * is smaller than {@code END_DISTANCE}. If the path is finished but the heading of the robot is off,
 * it will be corrected to the heading of the vector between the penultimate and final point of the path.
 *
 * @see AdaptivePurePursuitController#setNewTrajectory(String, boolean) setNewTrajectory
 * @see AdaptivePurePursuitController#update(Pose2d) update
 */

public class AdaptivePurePursuitController {
    private Translation2d mTargetPos;

    private Trajectory mCurrentTrajectory;
    private ArrayList<Trajectory.State> mTrajectoryStates;

    private boolean isPathReversed = false;
    private boolean pathFinished = false;

    /**
     * Sets {@code Trajectory} to follow from name as defined in Path Planner
     *
     * @param pPathName      Name of the path according to Path Planner
     * @param pIsReversed    If the path should be driven backwards
     * @return {@code Trajectory} to follow
     */
    public Trajectory setNewTrajectory(String pPathName, boolean pIsReversed) {
        return setNewTrajectory(pPathName, pIsReversed, MAX_VELOCITY, MAX_ACCELERATION);
    }

    /**
     * Sets {@code Trajectory} to follow from name as defined in Path Planner
     *
     * @param pPathName      Name of the path according to Path Planner
     * @param pIsReversed    If the path should be driven backwards
     * @return {@code Trajectory} to follow
     */
    public Trajectory setNewTrajectory(String pPathName, boolean pIsReversed, double pMaxVelocity, double pMaxAcceleration) {
        mCurrentTrajectory = TrajectoryHandler.newTrajectory(pPathName, pIsReversed, pMaxVelocity, pMaxAcceleration);

        // Store states in ArrayList
        mTrajectoryStates = (ArrayList<Trajectory.State>) mCurrentTrajectory.getStates();

        // Trim first entry of path because it has a velocity setpoint of 0
        mTrajectoryStates.remove(0);

        // Reset pathFinished
        pathFinished = false;

        // Set path reversed state
        isPathReversed = pIsReversed;

        return mCurrentTrajectory;
    }

    /**
     * Calculates {@code MotorDemand} in meters/second for current {@code Pose} using the Pure Pursuit algorithm.
     * If the robot has reached the final point but the orientation is incorrect, the function will output a
     * {@code MotorDemand} to turn around its own axis until the path is finished.
     * When the path is finished, a {@code MotorDemand} of 0 will be given.
     *
     * @see MotorDemand
     * @see Pose2d
     *
     * @param pPose Current pose of the robot
     * @return MotorDemand for both sides of the drivetrain
     */
    public MotorDemand update(Pose2d pPose) {
        Translation2d translation = pPose.getTranslation();

        // Check if path is finished
        if (translation.getDistance(
                mTrajectoryStates.get(mTrajectoryStates.size() - 1).poseMeters.getTranslation()) < PATH_END_DISTANCE
        ){
            pathFinished = true;
            return new MotorDemand(0,0);
        }

        // Calculate index of the closest point in path
        int closestPosIndex = getClosestPosIndexInPath(translation);

        SmartDashboard.putNumber("ClosestPosIndex", closestPosIndex);

        double targetVelocity = getTargetVelocity(mTrajectoryStates,closestPosIndex);

        // Determine point to travel to with lookahead distance
        int indexToTarget = getClosestPosIndexToLookahead(
                mTrajectoryStates,
                closestPosIndex,
                pPose.getTranslation(),
                getLookaheadForVelocity(targetVelocity)
        );
        mTargetPos = mTrajectoryStates.get(indexToTarget).poseMeters.getTranslation();

        double relativeHorizontal = getRelativeHorizontal(pPose, mTargetPos);
        double curvature = getCurvature(pPose, mTargetPos, relativeHorizontal);

        return calculateMotorDemand(targetVelocity, curvature);
    }

    /**
     * Finds out if the robot has reached the end of the current path
     *
     * @see Trajectory
     *
     * @return If the path is finished or not
     */
    public boolean isPathFinished() {
        return pathFinished;
    }

    /**
     * Gets the current {@code Trajectory} that the robot will follow
     *
     * @return {@code Trajectory} that the robot will follow
     */
    public Trajectory getCurrentTrajectory() {
        return mCurrentTrajectory;
    }

    /**
     * Gets the {@code Translation2d} that the robot is currently targetting
     *
     * @return Current target of the Pure Pursuit algorithm
     */
    public Translation2d getTargetPos() {
        return mTargetPos;
    }

    /**
     * Calculates index of closest {@code Translation2d} in {@code Path} compared to robot {@code Translation2d}
     *
     * @param pCurrentPos Current robot {@code Translation2d}
     * @return Index of closest {@code Translation2d} in {@code currentPath}
     */
    private int getClosestPosIndexInPath(Translation2d pCurrentPos) {
        int smallestDistanceIndex = 0;
        // Init as infinity so that there is always a smaller distance somewhere in the array
        double smallestDistance = Double.POSITIVE_INFINITY;

        // Loop over every Position in path to check closest
        for (int i = 0; i < mTrajectoryStates.size(); i++) {
            double currentDistance = pCurrentPos.getDistance(mTrajectoryStates.get(i).poseMeters.getTranslation());

            // Set smallest index to current index if distance is smaller
            if (currentDistance < smallestDistance) {
                smallestDistance = currentDistance;
                smallestDistanceIndex = i;
            }
        }

        return smallestDistanceIndex;
    }

    public boolean getPathReversed() {
        return isPathReversed;
    }

    /**
     * Calculates index of {@code Translation2d} in {@code Path} closest to lookahead distance.
     *
     * @param stateArray            The array of states that make up the current {@code Trajectory}
     * @param closestPathPosIndex   Index of closest {@code Translation2d} to robot from {@code Path}
     * @param robotPos              Current robot {@code Translation2d}
     * @param lookahead             Radius of the lookahead circle in meters
     * @return Index of {@code Translation2d} closest to lookahead distance
     */
    private int getClosestPosIndexToLookahead(
            ArrayList<Trajectory.State> stateArray,
            int closestPathPosIndex,
            Translation2d robotPos,
            double lookahead
    ) {
        int currentIndex = closestPathPosIndex;
        double currentDistance = robotPos.getDistance(stateArray.get(currentIndex).poseMeters.getTranslation());

        while (currentDistance < lookahead && currentIndex < stateArray.size() - 1) {
            currentDistance = robotPos.getDistance(stateArray.get(currentIndex).poseMeters.getTranslation());
            currentIndex++;
        }

        return currentIndex;
    }

    /**
     * Calculates a lookahead distance in meters based on a current lookahead distance in m/s
     *
     * @param pVelocity Current robot velocity in m/s
     * @return A lookahead distance in m based on current velocity
     */
    private double getLookaheadForVelocity(double pVelocity) {
        /* Algorithm based on code by FRC 254 */
        double deltaLookahead = MAX_LOOKAHEAD - MIN_LOOKAHEAD;
        double deltaVelocity = MAX_VELOCITY - MIN_VELOCITY;
        double lookahead = deltaLookahead * (pVelocity - MIN_VELOCITY) / deltaVelocity + MIN_LOOKAHEAD;

        // Clamp calculated lookahead between min and max lookahead
        return Double.isNaN(lookahead) ? MIN_LOOKAHEAD : Math.max(MIN_LOOKAHEAD, Math.max(MAX_LOOKAHEAD, lookahead));
    }

    /**
     * Gets relative horizontal distance to target point in robot space
     *
     * @param pRobotPose        Current {@code Pose2d} of the robot
     * @param pLookaheadPoint   {@code Translation2d} on the current Trajectory closest to the lookahead distance
     * @return Relative horizontal distance of the robot in meters
     */
    private double getRelativeHorizontal(Pose2d pRobotPose, Translation2d pLookaheadPoint){
        double a = -Math.tan(pRobotPose.getRotation().getRadians());
        double b = 1;
        double c = Math.tan(pRobotPose.getRotation().getRadians()) * pRobotPose.getX() - pRobotPose.getY();

        return Math.abs(a * pLookaheadPoint.getX() + b * pLookaheadPoint.getY() + c) / Math.hypot(a, b);
    }

    /**
     * Calculates the curvature of the arc through the robot position and the target point
     *
     * @param pRobotPose            Current {@code Pose2d} of the robot
     * @param pLookaheadPoint       {@code Translation2d} on the current Trajectory closest to the lookahead distance
     * @param pRelativeHorizontal   Relative horizontal distance from robot to target in robot space in meters
     * @return Curvature of arc between robot and target point
     */
    private double getCurvature(Pose2d pRobotPose, Translation2d pLookaheadPoint, double pRelativeHorizontal){
        double lookaheadDistance = pRobotPose.getTranslation().getDistance(pLookaheadPoint);

        double curvature = (2 * pRelativeHorizontal) / Math.pow(lookaheadDistance, 2);

        double side = Math.signum(
                Math.sin(pRobotPose.getRotation().getRadians()) * (pLookaheadPoint.getX() - pRobotPose.getX()) -
                        Math.cos(pRobotPose.getRotation().getRadians()) * (pLookaheadPoint.getY() - pRobotPose.getY())
        );

        return side * curvature;
    }

    /**
     * Calculates the {@code MotorDemand} based on target velocity and curvature
     * of arc through robot position and lookahead point
     *
     * @param pTargetVelocity   Target velocity of the robot in m/s
     * @param pCurvature        Curvature of arc through robot position and lookahead point
     * @return A {@code MotorDemand} containing target velocities for left and right parts of the drivetrain in m/s
     */
    private MotorDemand calculateMotorDemand(double pTargetVelocity,  double pCurvature) {
        return new MotorDemand(
                pTargetVelocity * (2 + pCurvature * DRIVETRAIN_WIDTH) / 2,
                pTargetVelocity * (2 - pCurvature * DRIVETRAIN_WIDTH) / 2
        );
    }

    /**
     * Returns the target velocity at the closest point from the robot in m/s
     *
     * @param pStateArray        Array of states that make up the current trajectory
     * @param pClosestPosIndex   Array index of the {@code Trajectory.State} closest to the robot
     * @return Target velocity in m/s at the closest point from the robot
     */
    private double getTargetVelocity(ArrayList<Trajectory.State> pStateArray, int pClosestPosIndex) {
        return pStateArray.get(pClosestPosIndex).velocityMetersPerSecond;
    }
}
