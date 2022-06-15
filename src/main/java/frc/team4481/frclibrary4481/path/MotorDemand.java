package frc.team4481.frclibrary4481.path;

/**
 * A way to easily store independent motor demands for the left and right side of a drivetrain.
 */
public class MotorDemand {
    public double left;
    public double right;

    /**
     * Constructs a new demand for the {@code Drivetrain} with a separate
     * target velocity for the left and right side in m/s.
     *
     * @param pLeft  Target velocity in m/s for the left side of the drivetrain
     * @param pRight Target velocity in m/s of the right side of the drivetrain
     */
    public MotorDemand(double pLeft, double pRight) {
        this.left = pLeft;
        this.right = pRight;
    }
}
