package frc.team4481.frclibrary4481.controller;

import edu.wpi.first.wpilibj.Joystick;
import frc.team4481.frclibrary4481.throwable.HardwareException;

public class OrangeHID extends Joystick implements IPS4HID {

    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param pPort The port on the Driver Station that the joystick is plugged into.
     */
    public OrangeHID(int pPort) {
        super(pPort);
    }

    /**
     *
     * @param pButton The name of the button as seen on the controller
     * @return boolean (true = button pressed, false = button not pressed)
     */
    @Override
    public boolean getButtonValue(Button pButton) throws HardwareException {
        switch (pButton) {
            default:
                return getRawButton(pButton.mId);
        }
    }

    /**
     *
     * @param pAxis The name of the axis as seen on the controller
     * @return double value (-1.00 -- 1.00)
     */
    @Override
    public double getAxisValue(Axis pAxis) {
        return getRawAxis(pAxis.mId);
    }

    /**
     *
     * @param pButton The orientation of the requested DPadButton
     * @return if the requested button is pressed.
     */
    @Override
    public boolean getDpadValue(DpadButton pButton) {return getPOV() == pButton.mId;}
}
