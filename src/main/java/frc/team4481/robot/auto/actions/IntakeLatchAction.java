package frc.team4481.robot.auto.actions;

import frc.team4481.frclibrary4481.actions.SeriesAction;
import frc.team4481.frclibrary4481.actions.WaitAction;
import static frc.team4481.robot.Constants.*;

/**
 * Action to switch the open or close state of the intake
 */
public class IntakeLatchAction extends SeriesAction {
    /**
     * Creates a new {@code IntakeLatchAction} that will switch the open/close state of the intake.
     */
    public IntakeLatchAction(){
        super(new SetIntakeLatchAction(), new WaitAction(0.1), new ResetIntakeLatchAction());
    }
}
