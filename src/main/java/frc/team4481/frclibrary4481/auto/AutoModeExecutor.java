package frc.team4481.frclibrary4481.auto;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The AutoModeExecutor class runs, and (if necessary) stops a specified autonomous mode {@code AutoModeBase}.
 * To make sure the specified autonomous mode runs simultaneously with the subsystem looper a different thread
 * is created. This also allows the autonomous mode to be terminated if it is not completed by the time teleop
 * period starts.
 *
 * @author Team 254 - The Cheesy Poofs
 *
 * Modified by Team 4481 - Team Rembrandts
 */
public class AutoModeExecutor extends BasicAutoRunnable {
    private final AtomicReference<Thread> mThread = new AtomicReference<>(null);

    /**
     * sets {@code AutoModeBase} as desired autonomous instance to execute
     *
     * Prepares a thread for the autonomous execution.
     *
     * @param pAutoMode autonomous mode instance to run
     */
    public void setAutoMode(AutoModeBase pAutoMode) {
        super.setAutoMode(pAutoMode);
        mThread.set(new Thread(this));
    }

    /**
     * Starts a separate thread for autonomous execution
     */
    public void start() {
        if (mThread.get() != null) {
            mThread.get().start();
        }
    }

    /**
     * Stops the execution of the autonomous mode and terminates the thread
     */
    public void stop() {
        if (getAutoMode() != null) {
            super.stop();
        }

        mThread.set(null);
    }

    /**
     * Checks whether the thread is still active
     *
     * @return if the thread is active or not
     */
    public boolean isRunning() {
        if (mThread.get() != null) {
            return  mThread.get().isAlive();
        }
        return false;
    }
}

