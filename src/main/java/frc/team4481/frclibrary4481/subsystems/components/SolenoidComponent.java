package frc.team4481.frclibrary4481.subsystems.components;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class SolenoidComponent extends Component{
    Solenoid mSolenoid;
    private boolean mLast = false;

    public SolenoidComponent(PneumaticsModuleType pcm, int pPortID){
        mSolenoid = new Solenoid(pcm, pPortID);
    }

    @Override
    public void update() {
        mSolenoid.set(mActivated);
    }

    public void latch(boolean pActivate){
        if (pActivate && !mLast) {
            mActivated = !mActivated;
        }
        mLast = pActivate;
    }
}

