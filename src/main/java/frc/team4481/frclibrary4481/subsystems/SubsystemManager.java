package frc.team4481.frclibrary4481.subsystems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsystemManager {
    public static SubsystemManager mInstance = null;

    private List<SubsystemBase> mAllSubsystems;

    private SubsystemManager(){}

    public static SubsystemManager getInstance(){
        if(mInstance == null){
            mInstance = new SubsystemManager();
        }

        return mInstance;
    }

    public void setSubsystems(SubsystemBase... pAllSubsystems) {
        mAllSubsystems = Arrays.asList(pAllSubsystems);
    }

    public List<SubsystemBase> getSubsystems() {
        return mAllSubsystems;
    }

    public SubsystemBase getSubsystemByName(String name){
        return mAllSubsystems.stream().filter(subsystemBase -> name.equals(subsystemBase.mName)).findFirst().orElse(null);
    }
    public SubsystemBase getSubsystemByClass(Class subsystem){
        return mAllSubsystems.stream().filter(subsystemBase -> subsystem.equals(subsystemBase.getClass())).findFirst().orElse(null);
    }
    public SubsystemControllerBase getSubsystemControllerByClass(Class controller){
        return mAllSubsystems.stream().filter(subsystemBase -> controller.equals(subsystemBase.mSubsystemController)).findFirst().orElse(null);
    }

    public List<String> getSubsystemNames(){
        List<String> names = new ArrayList<>();
        mAllSubsystems.forEach(subsystemBase -> names.add(subsystemBase.mName));
        return names;
    }

    public void outputToSmartDashboard() {
        mAllSubsystems.forEach(SubsystemBase::outputData);
    }

    public void terminateAll() {
        mAllSubsystems.forEach(SubsystemBase::terminate);
    }

}
