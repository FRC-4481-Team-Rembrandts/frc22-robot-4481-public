package frc.team4481.frclibrary4481.auto;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team4481.frclibrary4481.actions.Action;


/**
 * The AutoModeBase is the base class for each autonomous mode program.
 *
 * @author Team 254 - The Cheesy Poofs
 */
public abstract class AutoModeBase {
    protected double mUpdateRate = 1.0 / 50.0;
    protected boolean mActive = false;

    /**
     * Abstract class for definition of the autonomous program
     *
     * @throws AutoModeEndedException on early termination
     */
    protected abstract void routine() throws AutoModeEndedException;

    /**
     * Runs the autonomous routine {@see routine()} and deals with exception on early termination.
     */
    public void run() {
        mActive = true;

        try {
            routine();
        } catch (AutoModeEndedException e) {
            DriverStation.reportError("AUTO MODE ENDED EARLY", false);
            return;
        }

        done();
    }

    /**
     * Function to run on ending autonomous mode
     */
    public void done() {
        System.out.println("Auto mode done");
    }

    /**
     * Sets mActive to false which is used for the action runner {@see runAction()}
     */
    public void stop() {
        mActive = false;
    }

    /**
     * @return true when autonomous mode is active
     * @throws AutoModeEndedException when autonomous mode ended
     */
    public boolean isActiveWithThrow() throws AutoModeEndedException {
        if (!mActive) {
            throw new AutoModeEndedException();
        }

        return true;
    }

    /***
     * Runs actions inside the routine function {@see routine} for the selected autonomous mode instance.
     * Terminates the actions early if autonomous mode ended early.
     *
     * The action runner function rate is controlled by sleeping the thread for a desired amount of time.
     *
     * @param pAction action item to run
     * @throws AutoModeEndedException if autonomous becomes deactivated during action run.
     */
    public void runAction(Action pAction) throws AutoModeEndedException {
        isActiveWithThrow();
        pAction.start();

        while (isActiveWithThrow() && !pAction.isFinished()) {
            pAction.update();
            long waitTime = (long) (mUpdateRate * 1000.0);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pAction.done();
    }
}

// Copyright (c) 2019 Team 254