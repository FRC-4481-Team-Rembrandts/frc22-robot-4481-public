package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.SeriesAction;
import frc.team4481.frclibrary4481.actions.WaitAction;
import frc.team4481.robot.subsystems.ShooterController;

/**
 * Action to shoot a selected amount of balls
 */
public class ShootAction extends SeriesAction{
    /**
     * Creates a new ShootAction
     *
     * @param pShooterState Specifies the shooter routine to execute.
     * @param pFireAmountOfBalls Specifies the amount of balls to shoot.
     * @param pFailTime Sets a time bound on the duration of the action.
     */
    public ShootAction(ShooterController.ShootState pShooterState, int pFireAmountOfBalls, int pFailTime) {
        // Wait a short amount of time before disabling such that the last ball does not get stuck in the shooter wheel.
        super(
                new ShootStartAction(pShooterState, pFireAmountOfBalls, pFailTime),
                new WaitAction(0.1),
                new ShootStopAction());
    }
}

