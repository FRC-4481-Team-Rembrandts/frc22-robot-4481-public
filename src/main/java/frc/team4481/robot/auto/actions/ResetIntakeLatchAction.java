package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Intake;
import frc.team4481.robot.subsystems.IntakeController;

/**
 * Low level {@code Action} that resets the SR to latch for the intake arm.
 *
 * Note: This action should always occur after a {@link SetIntakeLatchAction}
 *
 * @see IntakeLatchAction
 */
public class ResetIntakeLatchAction implements Action {
    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Intake mIntake;
    IntakeController mIntakeController;

    /**
     * Create a new ResetIntakeLatchAction.
     */
    public ResetIntakeLatchAction() {
        mIntake = (Intake) mSubsystemManager.getSubsystemByClass(Intake.class);
        mIntakeController = mIntake.getSubsystemController();
        mIntakeController.setControlState(IntakeController.controlState.ENABLED);
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        // setLatchIntakeArm to false in intake controller such that the latch value is reset
        mIntakeController.setIntakeDeployed(false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void done() {

    }
}
