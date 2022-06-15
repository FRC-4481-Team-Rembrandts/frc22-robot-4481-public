package frc.team4481.frclibrary4481.subsystems.components;

public abstract class Component {
    boolean mActivated;
    public void activate(){
        mActivated = true;
    }
    public void deactivate(){
        mActivated = false;
    }
    abstract public void update();
    public boolean isActivated(){
        return mActivated;
    }
}
