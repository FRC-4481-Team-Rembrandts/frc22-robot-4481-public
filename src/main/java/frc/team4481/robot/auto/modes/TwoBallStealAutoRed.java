package frc.team4481.robot.auto.modes;

import frc.team4481.frclibrary4481.actions.ParallelAction;
import frc.team4481.frclibrary4481.actions.SeriesAction;
import frc.team4481.frclibrary4481.actions.WaitAction;
import frc.team4481.frclibrary4481.auto.AutoModeBase;
import frc.team4481.frclibrary4481.auto.AutoModeEndedException;
import frc.team4481.robot.auto.actions.*;
import frc.team4481.robot.subsystems.ShooterController;

public class TwoBallStealAutoRed extends AutoModeBase {
    String path1 = "1B_STEAL_PATH1";
    String path15 = "2B_STEAL_PATH1.5";
    String path2 = "2B_STEAL_PATH2";
    String path3 = "2B_STEAL_PATH3";
    String path4 = "2B_STEAL_PATH4";


    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new ColorFilterEnableAction(false));
        // Drive towards the first ball
        runAction(
                new ParallelAction(
                        new DrivePathAction(path1, false, 2,1),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new IntakeSpinAction(false)
                        )
                )
        );
        // Shoot both balls
        runAction(new ShootAction(ShooterController.ShootState.AUTOMATIC, 2, 2));
        runAction(new IntakeStopAction());
        runAction(new DeployIntakeAction(false));
        // Drive towards opposing alliance ball in the center of the field
        runAction(new DrivePathAction(path15, true,2,1));
        runAction(
                new ParallelAction(
                        new DrivePathAction(path2, false, 2,1),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new IntakeSpinAction(false)
                        )
                )
        );
        // Drive back to the other opposing alliance ball
        runAction(new DrivePathAction(path3, true,2,1));
        // Make a short turn and continue to the opposing alliance ball
        runAction(
                new ParallelAction(
                        new DrivePathAction(path4, false, 2,1),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new IntakeSpinAction(false)
                        )
                )
        );
        runAction(new IntakeStopAction());
        runAction(new DeployIntakeAction(false));
        // Shoot both balls in the hanger zone
        runAction(new ShootAction(ShooterController.ShootState.STEAL, 2, 2));
    }
}
