package frc.team4481.robot.auto.modes;

import frc.team4481.frclibrary4481.actions.ParallelAction;
import frc.team4481.frclibrary4481.actions.SeriesAction;
import frc.team4481.frclibrary4481.actions.WaitAction;
import frc.team4481.frclibrary4481.auto.AutoModeBase;
import frc.team4481.frclibrary4481.auto.AutoModeEndedException;
import frc.team4481.robot.auto.actions.*;
import frc.team4481.robot.subsystems.ShooterController;

public class FiveBallAutoBlue extends AutoModeBase {
    String path1 = "5B_BLUE_PATH1";
    String path2 = "5B_BLUE_PATH2";
    String path3 = "5B_BLUE_PATH3";
    String path4 = "5B_BLUE_PATH4";
    String path5 = "5B_BLUE_PATH5";

    @Override
    protected void routine() throws AutoModeEndedException {
        // Pick up first ball while driving towards it
        runAction(
                new ParallelAction(
                        new DrivePathAction(path1, false),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new ColorFilterEnableAction(false),
                                new IntakeSpinAction(false)
                        )
                )
        );
        // Wait some time before starting the shot
        runAction(new WaitAction(0.4));
        // Stop the intake spinning
        runAction(new ParallelAction(
                new DeployIntakeAction(false),
                new IntakeStopAction()
        ));
        // Shoot the first two balls
        runAction(new ShootAction(ShooterController.ShootState.AUTOMATIC,2,2));
        // Drive a little forward to make an easier turn
        runAction(new DrivePathAction(path2,true));
        // Drive towards the third ball
        runAction(
                new ParallelAction(
                        new DrivePathAction(path3, false),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new IntakeSpinAction(false)
                        )
                )
        );
        // Wait some time and shoot again
        runAction(new WaitAction(0.3));
        runAction(new ShootAction(ShooterController.ShootState.AUTOMATIC,1,2));
        // Drive towards the terminal for ball four and five
        runAction(
                new ParallelAction(
                        new DrivePathAction(path4,false),
                        new SeriesAction(
                                new DeployIntakeAction(true),
                                new IntakeSpinAction(false)
                        )
                )
        );
        // Give the human player some time to trow the ball
        runAction(new WaitAction(1.5));
        // Drive to the shooting location
        runAction(new DrivePathAction(path5, true));
        runAction(new WaitAction(0.3));
        // Shoot the last balls
        runAction(new ShootAction(ShooterController.ShootState.AUTOMATIC,2,2));
    }
}