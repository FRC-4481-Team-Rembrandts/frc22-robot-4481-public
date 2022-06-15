package frc.team4481.frclibrary4481.looper;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.util.CrashTracker;
import frc.team4481.robot.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * The BasicLooperRunnable class is a {@link Runnable} created to execute registered loops in a single rate timed-driven
 * AFAP cyclic executive fashion.
 *
 * @author Team 254 - The Cheesy Poofs
 */
public class BasicLooperRunnable implements Runnable {
    public final Object mTaskRunningLock = new Object();
    protected List<Loop> mLoops;

    protected double mDT = 0;
    private double mTimestamp = 0;

    protected boolean mRunning;

    public BasicLooperRunnable(){
        mRunning = false;
        mLoops = new ArrayList<>();
    }

    /**
     * @param loop loop instance to add to cyclic executive
     */
    public void register(Loop loop) {
        synchronized (mTaskRunningLock) {
            mLoops.add(loop);
        }
    }

    @Override
    public void run() {
        try {
            runCrashTrackedLooper();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash("BasicLooper Exception", t);
            throw t;
        }
    }

    /**
     * Cyclic executive for running all registered loops in a AFAP fashion. Observe that synchronization is required
     * in order to keep the loops in the same state.
     *
     * mDT tracks the average loop time in order to track possible drift.
     */
    private void runCrashTrackedLooper() {
        synchronized (mTaskRunningLock) {
            if (mRunning) {
                double now = Timer.getFPGATimestamp();

                for (Loop loop : mLoops) {
                    loop.onLoop(now);
                }

                mDT = now - mTimestamp;
                mTimestamp = now;
            }
        }
    }

    /**
     * Starts the onStart() function for all registered loops. Observe that synchronization is required
     * in order to keep the loops in the same state.
     */
    public synchronized void start() {
        System.out.println("Starting loops");

        synchronized (mTaskRunningLock) {
            mTimestamp = Timer.getFPGATimestamp();
            for (Loop loop : mLoops) {
                loop.onStart(mTimestamp);
            }
            mRunning = true;
        }
    }

    /**
     * Starts the onStop() function for all registered loops. Observe that synchronization is required
     * in order to keep the loops in the same state.
     */
    public synchronized void stop() {
        System.out.println("Stopping loops");

        synchronized (mTaskRunningLock) {
            mRunning = false;
            mTimestamp = Timer.getFPGATimestamp();
            for (Loop loop : mLoops) {
                System.out.println("Stopping " + loop);
                loop.onStop(mTimestamp);
            }
        }
    }

    /**
     * Output relevant looper data to the smart dashboard.
     */
    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("Looper DT", mDT);
        SmartDashboard.putBoolean("Looper Overflow", (mDT > Constants.kLooperDt));
    }
}

// Copyright (c) 2019 Team 254
