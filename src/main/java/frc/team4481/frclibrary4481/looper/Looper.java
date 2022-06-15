package frc.team4481.frclibrary4481.looper;

import edu.wpi.first.wpilibj.Notifier;
import frc.team4481.robot.Constants;

/**
 * The looper class is able to run all registered loop classes assigned to it. To do this
 * a new timed-periodic thread, {@link Notifier}, is created and runs the start() run() and stop() functions
 * of the {@link BasicLooperRunnable}.
 *
 * @author Team 254 - The Cheesy Poofs
 *
 * Modified by Team 4481 - Team Rembrandts
 */
public class Looper extends BasicLooperRunnable {
    private final Notifier mNotifier;
    public final double kPeriod = Constants.kLooperDt;

    public Looper() {
        mNotifier = new Notifier(this);
    }

    @Override
    public synchronized void register(Loop loopSys) {
        super.register(loopSys);
    }

    public void start() {
        if (!mRunning) {
            super.start();
            mNotifier.startPeriodic(kPeriod);
        }
    }

    public void run() {
        super.run();
    }

    public void stop() {
        if (mRunning) {
            super.stop();
            mNotifier.stop();
        }
    }
}