package frc.team4481.frclibrary4481.looper;

import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;

import java.util.List;

/**
 * Base loop for execution of the loop functions inside each subsystem when the robot is enabled.
 */
public class EnabledSuperSubsystemLoop implements Loop {
    private final List<SubsystemBase> mAllSubsystems;

    public EnabledSuperSubsystemLoop(SubsystemManager mSubsystemManger){
        this.mAllSubsystems = mSubsystemManger.getSubsystems();
    }

    @Override
    public void onStart(double timestamp) {
        mAllSubsystems.forEach(s -> s.onStart(timestamp));
    }

    @Override
    public void onLoop(double timestamp) {
        mAllSubsystems.forEach(SubsystemBase::readPeriodicInputs);
        mAllSubsystems.forEach(s -> s.onLoop(timestamp));
        mAllSubsystems.forEach(SubsystemBase::writePeriodicOutputs);
        mAllSubsystems.forEach(SubsystemBase::outputData);
    }

    @Override
    public void onStop(double timestamp) {
        mAllSubsystems.forEach(s -> s.onStop(timestamp));
    }
}
