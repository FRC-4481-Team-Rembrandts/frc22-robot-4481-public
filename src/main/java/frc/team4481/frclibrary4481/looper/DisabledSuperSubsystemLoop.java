package frc.team4481.frclibrary4481.looper;

import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;

import java.util.List;

/**
 * Base loop for execution of the loop functions inside each subsystem when the robot is disabled.
 */
public class DisabledSuperSubsystemLoop implements Loop {
    private final List<SubsystemBase> mAllSubsystems;

    public DisabledSuperSubsystemLoop(SubsystemManager mSubsystemManger){
        this.mAllSubsystems = mSubsystemManger.getSubsystems();
    }

    @Override
    public void onStart(double timestamp) {
    }

    @Override
    public void onLoop(double timestamp) {
        mAllSubsystems.forEach(SubsystemBase::readPeriodicInputs);
        mAllSubsystems.forEach(SubsystemBase::outputData);
    }

    @Override
    public void onStop(double timestamp) {
    }
}
