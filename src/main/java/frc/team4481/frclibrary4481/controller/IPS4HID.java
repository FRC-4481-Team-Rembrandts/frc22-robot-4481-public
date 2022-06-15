package frc.team4481.frclibrary4481.controller;

import frc.team4481.frclibrary4481.throwable.HardwareException;

public interface IPS4HID {
    /**
     * The naming of the button id configuration
     */
    enum Button{
        CROSS(1),
        CIRCLE(2),
        SQUARE(3),
        TRIANGLE(4),

        BUMPER_L1(5),
        BUMPER_R1(6),

        SHARE(7),
        OPTIONS(8),

        LEFTSTICK_BUTTON(9),
        RIGHTSTICK_BUTTON(10);


        public final int mId;

        Button(int pId) {
            mId = pId;
        }
    }

    enum DpadButton{
        DPAD_N(0),
        DPAD_NE(45),
        DPAD_E(90),
        DPAD_SE(135),
        DPAD_S(180),
        DPAD_SW(225),
        DPAD_W(270),
        DPAD_NW(315);
        public final int mId;
        DpadButton(int pID) {mId = pID;}
    }
    /**
     * The naming of the axis id configuration
     */
    enum Axis{
        LEFTSTICK_X(0),
        LEFTSTICK_Y(1),
        TRIGGER_L2(2),
        TRIGGER_R2(3),
        RIGHTSTICK_X(4),
        RIGHTSTICK_Y(5);

        public final int mId;

        Axis(int pId) {
            mId = pId;
        }
    }
    //standard SubsystemController functions

    /**
     *
     * @param pButton The requested button
     * @return boolean (true = button pressed, false = button not pressed)
     * @throws HardwareException an exception that can be thrown if the button is broken
     */
    boolean getButtonValue(Button pButton) throws HardwareException;

    /**
     *
     * @param pAxis The requested axis
     * @return double value (-1.00 -- 1.00)
     */
    double getAxisValue(Axis pAxis);
    /**
     *
     * @param pButton The orientation of the requested DPadButton
     * @return if the requested button is pressed.
     */
    boolean getDpadValue(DpadButton pButton);
}
