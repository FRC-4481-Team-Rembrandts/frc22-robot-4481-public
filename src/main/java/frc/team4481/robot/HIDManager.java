package frc.team4481.robot;

import frc.team4481.frclibrary4481.HIDlayout.HIDLayout;
import frc.team4481.frclibrary4481.controller.BlackHID;
import frc.team4481.frclibrary4481.controller.OrangeHID;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.frclibrary4481.throwable.HardwareException;
import frc.team4481.robot.HIDlayout.WorldsLayout;

public class HIDManager {
    public static HIDManager mInstance = null;

    private HIDManager() {
    }

    OrangeHID mOrangeController = new OrangeHID(0);
    BlackHID mBlackController = new BlackHID(1);

    HIDLayout layout = new WorldsLayout(mOrangeController, mBlackController);

    public static HIDManager getInstance() {
        if (mInstance == null) {
            mInstance = new HIDManager();
        }
        return mInstance;
    }

    public void getControllers(SubsystemManager subsystemManager) {
        layout.getControllers();
    }

    public void update() {
        try {
            layout.updateOrange();
            layout.updateBlack();
        } catch (HardwareException e) {
            e.printStackTrace();
        }
    }
}
