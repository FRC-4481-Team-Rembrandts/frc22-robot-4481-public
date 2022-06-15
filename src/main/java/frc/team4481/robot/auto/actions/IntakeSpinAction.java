package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.*;

/**
 * Action to set the intake behaviour to spin
 */
public class IntakeSpinAction implements Action {
    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Intake mIntake;
    IntakeController mIntakeController;
    Shooter mShooter;
    ShooterController mShooterController;

    boolean reversed;

    /**
     * Creates a new {@code IntakeSpinAction} to set the intake behaviour to spin either forward or backward.
     *
     * @param reversed whether the intake should spin in reverse
     */
    public IntakeSpinAction(boolean reversed) {
        mIntake = (Intake) mSubsystemManager.getSubsystemByClass(Intake.class);
        mIntakeController = mIntake.getSubsystemController();
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();

        this.reversed = reversed;
    }

    @Override
    public void start() {
        mIntakeController.setControlState(IntakeController.controlState.ENABLED);
        mShooterController.setControlState(ShooterController.ControlState.ENABLED);

        if (!reversed) {
            mShooterController.setStorageActive(true);
        } else {
            mShooterController.setReverseState(ShooterController.reverseState.FULL);
            mShooterController.setReverse(true);
        }

        mIntakeController.setIntakeForwardEnabled(!reversed);
        mIntakeController.setIntakeReversedEnabled(reversed);
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
