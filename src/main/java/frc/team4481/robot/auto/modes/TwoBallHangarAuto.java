package frc.team4481.robot.auto.modes;

import frc.team4481.frclibrary4481.actions.ParallelAction;
import frc.team4481.frclibrary4481.actions.SeriesAction;
import frc.team4481.frclibrary4481.auto.AutoModeBase;
import frc.team4481.frclibrary4481.auto.AutoModeEndedException;
import frc.team4481.robot.auto.actions.*;
import frc.team4481.robot.subsystems.ShooterController;

public class TwoBallHangarAuto extends AutoModeBase {
    String path1 = "2B_PATH1";

    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new ColorFilterEnableAction(false));
        // Drive towards the first ball
        runAction(
                new ParallelAction(
                        new DrivePathAction(path1, false),
                        new SeriesAction(
                                new DeployIntakeAction(true), new IntakeSpinAction(false)
                        )
                )
        );
        // Shoot balls
        runAction(new ShootStartAction(ShooterController.ShootState.AUTOMATIC, 2, 2));
        runAction(new IntakeStopAction());
        runAction(new DeployIntakeAction(false));
    }
}
