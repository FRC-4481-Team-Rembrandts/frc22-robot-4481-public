package frc.team4481.robot.util;

/**
 * A way to easily store independent motor demands for the left and right side of a drivetrain.
 *
 */
public class RollerDemands {
    public double shooterRPM;
    public double topRollerRPM;

    /**
     *
     * @param pShooterRPM  Target velocity in RPM for the shooter wheel
     * @param pTopRollerRPM Target velocity in RPM for the top roller
     */
    public RollerDemands(double pShooterRPM, double pTopRollerRPM) {
        this.shooterRPM = pShooterRPM;
        this.topRollerRPM = pTopRollerRPM;
    }
}