package frc.team4481.frclibrary4481.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Executes one action at a time. Useful as a member of {@link ParallelAction}
 *
 * @author Team 254 - The Cheesy Poofs
 */
public class SeriesAction implements Action {
    private Action mCurrentAction;
    private final ArrayList<Action> mRemainingActions;

    public SeriesAction(List<Action> pActions) {
        mRemainingActions = new ArrayList<>(pActions);
        mCurrentAction = null;
    }

    public SeriesAction(Action... pActions) {
        this(Arrays.asList(pActions));
    }

    @Override
    public void start() {}

    @Override
    public void update() {
        if (mCurrentAction == null) {
            if (mRemainingActions.isEmpty()) {
                return;
            }

            mCurrentAction = mRemainingActions.remove(0);
            mCurrentAction.start();
        }

        mCurrentAction.update();

        if (mCurrentAction.isFinished()) {
            mCurrentAction.done();
            mCurrentAction = null;
        }
    }

    @Override
    public boolean isFinished() {
        return mRemainingActions.isEmpty() && mCurrentAction == null;
    }

    @Override
    public void done() {}
}

// Copyright (c) 2019 Team 254
