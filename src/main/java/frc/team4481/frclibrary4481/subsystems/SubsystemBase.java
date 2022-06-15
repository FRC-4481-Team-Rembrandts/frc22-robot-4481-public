package frc.team4481.frclibrary4481.subsystems;

import frc.team4481.frclibrary4481.looper.Loop;

import java.util.UUID;

public abstract class SubsystemBase<TController> extends SubsystemControllerBase implements Loop {
    public String mName = "Unnamed Subsystem";
    UUID mId = null;

    protected TController mSubsystemController;

    public abstract void readPeriodicInputs();

    public abstract void writePeriodicOutputs();

    public abstract void zeroSensors();

    public abstract void terminate();

    public abstract void outputData();

    public TController getSubsystemController(){
        return mSubsystemController;
    }
}
