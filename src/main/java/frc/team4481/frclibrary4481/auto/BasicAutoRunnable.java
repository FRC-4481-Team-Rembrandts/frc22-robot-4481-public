package frc.team4481.frclibrary4481.auto;

import frc.team4481.frclibrary4481.util.CrashTracker;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The BasicAutoRunnable class is a runnable used for the autonomous mode executor {@see AutoModeExecutor}.
 * It runs the selected autonomous program when the runnable (or thread) is activated.
 *
 * @author Team 254 - The Cheesy Poofs
 *
 * Modified by Team 4481 - Team Rembrandts
 */
public class BasicAutoRunnable implements Runnable{
    private final AtomicReference<AutoModeBase> mAutoMode = new AtomicReference<>(null);

    /**
     * Checks whether there is autonomous mode to execute.
     */
    @Override
    public final void run() {
        try {
            runCrashTrackedAuto();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash("BasicAuto Exception", t);
            throw t;
        }
    }

    /**
     * Runs autonomous mode
     */
    private void runCrashTrackedAuto(){
        if (mAutoMode.get() != null) {
            mAutoMode.get().run();
        }
    }

    /**
     * Set the desired autonomous mode for execution
     *
     * @param pAutoMode is the autonomous mode to execute
     */
    public void setAutoMode(AutoModeBase pAutoMode) {
        mAutoMode.set(pAutoMode);
    }

    /**
     * @return the selected autonomous mode instance
     */
    public AutoModeBase getAutoMode() {
        return mAutoMode.get();
    }

    /**
     * Stops the autonomous mode
     */
    public void stop() {
        mAutoMode.get().stop();
    }
}

