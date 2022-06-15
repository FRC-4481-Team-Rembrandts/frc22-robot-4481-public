package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;

/**
 * Enable color filtering during autonomous.
 */
public class ColorFilterEnableAction implements Action {
    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Shooter mShooter;
    ShooterController mShooterController;
    boolean enable;

    /**
     * Creates a new {@code ColorFilterEnableAction} to deploy or retract the intake mechanism of the robot.
     *
     * @param pEnable when color filtering needs to be enabled
     */
    public ColorFilterEnableAction(boolean pEnable) {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();
        enable = pEnable;
    }

    @Override
    public void start() {
        mShooterController.setColorFilterActive(enable);
        if (enable) {
            mShooterController.setControlState(ShooterController.ControlState.ENABLED);
        } else {
            mShooterController.setControlState(ShooterController.ControlState.DISABLED);
        }
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