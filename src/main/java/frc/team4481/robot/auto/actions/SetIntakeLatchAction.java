package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Intake;
import frc.team4481.robot.subsystems.IntakeController;

/**
 * Low level {@code Action} that sets the SR latch for the intake arm.
 */
public class SetIntakeLatchAction implements Action {
    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Intake mIntake;
    IntakeController mIntakeController;

    /**
     * Create a new {@code SetIntakeLatchAction} that sets the SR latch for the intake arm.
     */
    public SetIntakeLatchAction() {
        mIntake = (Intake) mSubsystemManager.getSubsystemByClass(Intake.class);
        mIntakeController = mIntake.getSubsystemController();
        mIntakeController.setControlState(IntakeController.controlState.ENABLED);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        // setLatchIntakeArm to true in intake controller
        mIntakeController.setIntakeDeployed(true);

    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void done() {
        // Note: Do not set control state back to DISABLED to overcome incorrect behavior.
    }
}
