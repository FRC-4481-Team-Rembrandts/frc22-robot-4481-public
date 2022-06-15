package frc.team4481.frclibrary4481.HIDlayout;

import frc.team4481.frclibrary4481.controller.BlackHID;
import frc.team4481.frclibrary4481.controller.OrangeHID;
import frc.team4481.frclibrary4481.throwable.HardwareException;

public abstract class HIDLayout {
    protected OrangeHID orangeController;
    protected BlackHID blackController;

    public HIDLayout(OrangeHID orange, BlackHID black){
        orangeController = orange;
        blackController = black;
    }

    public abstract void getControllers();

    public abstract void updateOrange() throws HardwareException;
    public abstract void updateBlack() throws HardwareException;
}
