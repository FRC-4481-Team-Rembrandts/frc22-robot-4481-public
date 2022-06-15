package frc.team4481.frclibrary4481.subsystems.components;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class GyroComponent extends Component{
    private AHRS mNavX;
    private double currentHeading;

    public GyroComponent(){
        mNavX = new AHRS(SPI.Port.kMXP);
        currentHeading = 0.0;
    }

    @Override
    public void update() {
        if(isActivated()){
            currentHeading = mNavX.getCompassHeading();
        }
    }

    public double getCurrentHeading() {
        return currentHeading;
    }

    public void reset(){
        mNavX.reset();
    }
}
