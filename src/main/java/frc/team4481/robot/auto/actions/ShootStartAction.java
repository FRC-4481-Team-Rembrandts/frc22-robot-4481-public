package frc.team4481.robot.auto.actions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Drivetrain;
import frc.team4481.robot.subsystems.DrivetrainController;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;

/**
 * Low level {@code Action} that shoots the balls.
 */
public class ShootStartAction implements Action {
    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Shooter mShooter;
    ShooterController mShooterController;
    ShooterController.ShootState mShootState;
    Drivetrain mDrivetrain;
    DrivetrainController mDrivetrainController;
    int fireAmountOfBalls;
    int failTime;
    private double startTime;

    /**
     * Creates a {@code ShooterStartAction}
     *
     * @param pShooterState Specifies the shooter routine to execute.
     * @param pFireAmountOfBalls Specifies the amount of balls to shoot.
     * @param pFailTime Sets a time bound on the duration of the action.
     */
    public ShootStartAction(ShooterController.ShootState pShooterState, int pFireAmountOfBalls, int pFailTime) {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();
        mDrivetrain = (Drivetrain) mSubsystemManager.getSubsystemByClass(Drivetrain.class);
        mDrivetrainController = mDrivetrain.getSubsystemController();

        this.fireAmountOfBalls = pFireAmountOfBalls;
        this.failTime = pFailTime;
        mShootState = pShooterState;
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        mShooterController.setControlState(ShooterController.ControlState.ENABLED);
        mShooterController.setDesiredShootState(mShootState);
        mShooterController.setOuttakeActive(true);
        mShooterController.setShootActive(true);
        if(mShootState == ShooterController.ShootState.AUTOMATIC)
            mDrivetrainController.setControlState(DrivetrainController.controlState.VISION);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean isFinished() {
        // Check whether fail time is exceeded. Else look for the amount of balls that are aready shot.
        if (Timer.getFPGATimestamp() - startTime >= failTime){
            DriverStation.reportWarning("Fail safe time exceeded, abort shoot action", true);
            return true;
        } else {
            // Finish action if amount of balls fired is equal to the requested amount.
            return mShooterController.getNumberOfShots() == fireAmountOfBalls;
        }
    }

    @Override
    public void done() {

    }
}
