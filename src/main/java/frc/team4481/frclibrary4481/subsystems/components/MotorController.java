package frc.team4481.frclibrary4481.subsystems.components;

public enum MotorController {
    SPARKMAX,
    TALONSRX,
    FALCON,
    VICTORSPX;
    public enum ControlMode{
        PERCENTOUTPUT,
        CURRENT,
        PID,
        FOLLOWER,
        PIDF,
    }
    public enum EncoderValue{
        POSITION,
        VELOCITY,
        PERCENT
    }
}
