package frc.team4481.robot.auto.modes;

import frc.team4481.frclibrary4481.actions.ParallelAction;
import frc.team4481.frclibrary4481.actions.SeriesAction;
import frc.team4481.frclibrary4481.actions.WaitAction;
import frc.team4481.frclibrary4481.auto.AutoModeBase;
import frc.team4481.frclibrary4481.auto.AutoModeEndedException;
import frc.team4481.robot.auto.actions.*;
import frc.team4481.robot.subsystems.ShooterController;

public class OneBallStealAutoBlue extends AutoModeBase {
    String path1 = "1B_STEAL_PATH1";
    String path2 = "1B_STEAL_PATH2";
    String path3 = "1B_STEAL_PATH3";
    String path4 = "1B_STEAL_PATH4";


    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new ColorFilterEnableAction(false));
        // Drive towards first ball
        runAction(
                new ParallelAction(
                        new DrivePathAction(path1, false, 2,1),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new IntakeSpinAction(false)
                        )
                )
        );
        // Shoot first ball
        runAction(new ShootAction(ShooterController.ShootState.AUTOMATIC, 2, 2));
        runAction(new IntakeStopAction());
        runAction(new DeployIntakeAction(false));
        // Drive closer to the opposing alliance ball
        runAction(new DrivePathAction(path2, true,2,1));
        // Pick up the opposing alliance ball
        runAction(
                new ParallelAction(
                        new DrivePathAction(path3, false, 2,1),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new IntakeSpinAction(false)
                        )
                )
        );
        runAction(new IntakeStopAction());
        runAction(new WaitAction(0.3));
        // Reverse intake the last ball
        runAction(new IntakeSpinAction(true));
        runAction(new DrivePathAction(path4, true,2,1));

    }
}
