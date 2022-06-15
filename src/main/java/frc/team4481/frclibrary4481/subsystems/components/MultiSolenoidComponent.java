package frc.team4481.frclibrary4481.subsystems.components;

import java.util.Arrays;
import java.util.List;

public class MultiSolenoidComponent extends Component{
    List<SolenoidComponent> mSolenoids;
    private boolean mLast = false;

    public MultiSolenoidComponent(SolenoidComponent... pSolenoids) {
        mSolenoids = Arrays.asList(pSolenoids);
    }


    public void update() {
        mSolenoids.forEach(SolenoidComponent::update);
    }

    public void latch(boolean pActivate) {
        mSolenoids.forEach(solenoidComponent -> solenoidComponent.latch(pActivate));
        if (pActivate && !mLast) {
            mActivated = !mActivated;
        }
        mLast = pActivate;
    }

    @Override
    public void activate() {
        super.activate();
        mSolenoids.forEach(SolenoidComponent::activate);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        mSolenoids.forEach(Component::deactivate);
    }
}
