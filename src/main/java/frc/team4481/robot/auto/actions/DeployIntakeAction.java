package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.Action;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Intake;
import frc.team4481.robot.subsystems.IntakeController;

/**
 * Action to deploy or retract the intake mechanism of the robot.
 */
public class DeployIntakeAction implements Action {

    private final boolean deployed;

    SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    Intake mIntake;
    IntakeController mIntakeController;

    /**
     * Creates a new {@code DeployIntakeAction} to deploy or retract the intake mechanism of the robot.
     *
     * @param pDeployed whether the intake should deploy or retract
     */
    public DeployIntakeAction(boolean pDeployed) {
        deployed = pDeployed;

        mIntake = (Intake) mSubsystemManager.getSubsystemByClass(Intake.class);
        mIntakeController = mIntake.getSubsystemController();
        mIntakeController.setControlState(IntakeController.controlState.ENABLED);
    }

    @Override
    public void start() {
        mIntakeController.setIntakeDeployed(deployed);
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
