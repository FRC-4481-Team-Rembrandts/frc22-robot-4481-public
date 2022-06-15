package frc.team4481.frclibrary4481.path;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.trajectory.Trajectory;

import static frc.team4481.robot.Constants.*;

/**
 * Utility class to load and transform a {@code Trajectory} such that it can be used by an
 * {@code AdaptivePurePursuitController}. This class might be useful for other tools as well
 * but these are not these are currently not supported.
 *
 * If you have a feature request, please message the author on Slack.
 */
public class TrajectoryHandler {
    /**
     * Transforms a Path Planner 2 path into a {@code PathPlannerTrajectory}
     *
     * @param pName      Filename of the path minus file extension
     * @param pReversed  Should the robot follow the path in reverse
     * @return A {@code PathPlannerTrajectory} of the current path
     */
    static Trajectory newTrajectory(String pName, boolean pReversed) {
        return PathPlanner.loadPath(pName, MAX_VELOCITY, MAX_ACCELERATION, pReversed);
    }

    /**
     * Transforms a Path Planner 2 path into a {@code PathPlannerTrajectory}
     *
     * @param pName      Filename of the path minus file extension
     * @param pReversed  Should the robot follow the path in reverse
     * @param pMaxVelocity Maximum robot velocity on the path in m/s
     * @param pMaxAcceleration Maximum robot acceleration on the path in m/s^2
     * @return A {@code PathPlannerTrajectory} of the current path
     */
    static Trajectory newTrajectory(String pName, boolean pReversed, double pMaxVelocity, double pMaxAcceleration) {
        return PathPlanner.loadPath(pName, pMaxVelocity, pMaxAcceleration, pReversed);
    }
}
