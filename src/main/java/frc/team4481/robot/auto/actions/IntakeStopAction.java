package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.*;

/**
 * Action to stop the intake from spinning
 */
public class IntakeStopAction implements Action {
    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Intake mIntake;
    IntakeController mIntakeController;
    Shooter mShooter;
    ShooterController mShooterController;

    /**
     * Creates a new {@code IntakeStopAction} that stops the intake from spinning.
     */
    public IntakeStopAction() {
        mIntake = (Intake) mSubsystemManager.getSubsystemByClass(Intake.class);
        mIntakeController = mIntake.getSubsystemController();
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();
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
        mIntakeController.setIntakeForwardEnabled(false);
        mIntakeController.setIntakeReversedEnabled(false);
        mIntakeController.setControlState(IntakeController.controlState.DISABLED);

        mShooterController.setStorageActive(false);
        mShooterController.setControlState(ShooterController.ControlState.DISABLED);
    }
}
