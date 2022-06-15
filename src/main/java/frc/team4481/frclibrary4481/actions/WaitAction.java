package frc.team4481.frclibrary4481.actions;

import edu.wpi.first.wpilibj.Timer;

/**
 * Simple wait action useful as a member of {@link SeriesAction} or {@link ParallelAction} or for early
 * autonomous mode testing
 *
 * @author Team 254 - The Cheesy Poofs
 */
public class WaitAction implements Action{
    private final double mTimeToWait;
    private double mStartTime;

    public WaitAction(double timeToWait /*ms>*/) {
        mTimeToWait = timeToWait;
    }

    @Override
    public void start() {
        mStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
        System.out.println("Timer:" + (Timer.getFPGATimestamp()-mStartTime));
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - mStartTime >= mTimeToWait;
    }

    @Override
    public void done() {

    }
}

// Copyright (c) 2019 Team 254
