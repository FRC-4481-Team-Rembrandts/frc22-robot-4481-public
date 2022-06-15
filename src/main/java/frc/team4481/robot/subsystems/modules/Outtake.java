package frc.team4481.robot.subsystems.modules;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;
import frc.team4481.robot.util.RollerDemands;
import frc.team4481.robot.util.ShooterPreset;
import frc.team4481.robot.util.VisionLookUpTable;

import static frc.team4481.robot.Constants.*;

import java.util.Arrays;
import java.util.List;

public class Outtake extends SubsystemBase<OuttakeController> {
    private List<SubsystemBase> mOuttakeModules;
    private ShooterRollers mShooterRollers;
    private Hood mHood;

    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    private Shooter mShooter;
    private ShooterController mShooterController;

    private VisionLookUpTable mVisionLookupTable;
    private RollerDemands mRollerDemands1;
    private RollerDemands mRollerDemands2;

    private String stateStr = "";;
    private String sstateStr = "";;

    public Outtake(){
        mName = "Outtake";
        mSubsystemController = new OuttakeController();

        mShooterRollers = new ShooterRollers();
        mHood = new Hood();
        mOuttakeModules = Arrays.asList(mShooterRollers, mHood);

        mVisionLookupTable = new VisionLookUpTable();
        mRollerDemands1 = new RollerDemands(0,0);
        mRollerDemands2 = new RollerDemands(0,0);

    }

    @Override
    public void onStart(double timestamp) {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();

        mOuttakeModules.forEach(s -> s.onStart(timestamp));
    }

    @Override
    public void readPeriodicInputs() {
        if (mShooterController != null)
            mShooterController.setAtSetpoint(mShooterRollers.getSubsystemController().getAtSetpoint());

        mOuttakeModules.forEach(SubsystemBase::readPeriodicInputs);
    }

    @Override
    public void onLoop(double timestamp) {
        switch (mSubsystemController.getControlState()) {
            case IDLE:
                stateStr = "IDLE";

                mHood.getSubsystemController().setControlState(HoodController.controlState.DISABLED);
                mShooterRollers.getSubsystemController().setControlState(ShooterRollersController.controlState.IDLE);

                mShooterRollers.getSubsystemController().setBall1Speed(0, 0);
                mShooterRollers.getSubsystemController().setBall2Speed(0, 0);
                break;
            case SHOOT:
                stateStr = "SHOOT";

                mHood.getSubsystemController().setControlState(HoodController.controlState.ENABLED);
                mShooterRollers.getSubsystemController().setControlState(ShooterRollersController.controlState.ENABLED);

                // Control hood and shooter rollers for each shooting state
                switch (mShooterController.getShootState()) {
                    // Shoot 2 balls high
                    case HIGH2X:
                        sstateStr = "HIGH2";

                        mHood.getSubsystemController().setAngle(HOOD_ENCODER_HIGH);

                        mRollerDemands1 = getRollerSpeeds(SHOOTER_VELOCITY_2xHIGH_BALL1, ROLLER_TO_SHOOTER_RATIO_2xHIGH_BALL1);
                        mRollerDemands2 = getRollerSpeeds(SHOOTER_VELOCITY_2xHIGH_BALL2, ROLLER_TO_SHOOTER_RATIO_2xHIGH_BALL2);

                        // Possibly set different speeds for the first and second ball
                        mShooterRollers.getSubsystemController().setBall1Speed(mRollerDemands1.shooterRPM, mRollerDemands1.topRollerRPM);
                        mShooterRollers.getSubsystemController().setBall2Speed(mRollerDemands2.shooterRPM, mRollerDemands2.topRollerRPM);
                        break;
                    // Slow shot to shoot balls inside hangar zone
                    case STEAL:
                        sstateStr = "STEAL";

                        mHood.getSubsystemController().setAngle(HOOD_ENCODER_LOW);

                        mRollerDemands1 = getRollerSpeeds(SHOOTER_VELOCITY_STEAL, ROLLER_TO_SHOOTER_RATIO_STEAL);
                        mRollerDemands2 = getRollerSpeeds(SHOOTER_VELOCITY_STEAL, ROLLER_TO_SHOOTER_RATIO_STEAL);

                        // Possibly set different speeds for the first and second ball
                        mShooterRollers.getSubsystemController().setBall1Speed(mRollerDemands1.shooterRPM, mRollerDemands1.topRollerRPM);
                        mShooterRollers.getSubsystemController().setBall2Speed(mRollerDemands2.shooterRPM, mRollerDemands2.topRollerRPM);
                        break;
                    // Shoot 2 balls low
                    case LOW2X:
                        sstateStr = "LOW2";

                        mHood.getSubsystemController().setAngle(HOOD_ENCODER_LOW);

                        mRollerDemands1 = getRollerSpeeds(SHOOTER_VELOCITY_2xLOW_BALL1, ROLLER_TO_SHOOTER_RATIO_2xLOW_BALL1);
                        mRollerDemands2 = getRollerSpeeds(SHOOTER_VELOCITY_2xLOW_BALL2, ROLLER_TO_SHOOTER_RATIO_2xLOW_BALL2);

                        // Possibly set different speeds for the first and second ball
                        mShooterRollers.getSubsystemController().setBall1Speed(mRollerDemands1.shooterRPM, mRollerDemands1.topRollerRPM);
                        mShooterRollers.getSubsystemController().setBall2Speed(mRollerDemands2.shooterRPM, mRollerDemands2.topRollerRPM);
                        break;
                    // Shoot based on vision data
                    case AUTOMATIC:
                        sstateStr = "AUTOMATIC";

                        /* Get distance to target from the limelight and get a matching shooter preset from the
                        vision lookup table */
                        double llDistance = mShooterController.getShotDistance();
                        ShooterPreset shotPreset = mVisionLookupTable.getShooterPreset(llDistance);

                        mHood.getSubsystemController().setAngle(transformHoodAngle(shotPreset.getHoodEjectAngle()));
                        mRollerDemands1 = getRollerSpeeds(shotPreset.getFlywheelSpeed(), shotPreset.getTopRollerPercentage());

                        // Possibly set different speeds for the first and second ball
                        mShooterRollers.getSubsystemController().setBall1Speed(mRollerDemands1.shooterRPM, mRollerDemands1.topRollerRPM);
                        mShooterRollers.getSubsystemController().setBall2Speed(mRollerDemands1.shooterRPM, mRollerDemands1.topRollerRPM);
                        break;
                    // Eject the balls of the wrong color
                    case FILTER:
                        sstateStr = "FILTER";

                        mHood.getSubsystemController().setAngle(HOOD_ENCODER_LOW);

                        mRollerDemands1 = getRollerSpeeds(SHOOTER_VELOCITY_FILTER, ROLLER_TO_SHOOTER_RATIO_FILTER);
                        mRollerDemands2 = getRollerSpeeds(SHOOTER_VELOCITY_FILTER, ROLLER_TO_SHOOTER_RATIO_FILTER);

                        // Possibly set different speeds for the first and second ball
                        mShooterRollers.getSubsystemController().setBall1Speed(mRollerDemands1.shooterRPM, mRollerDemands1.topRollerRPM);
                        mShooterRollers.getSubsystemController().setBall2Speed(mRollerDemands2.shooterRPM, mRollerDemands2.topRollerRPM);
                        break;
                }
                break;
        }

        mOuttakeModules.forEach(s -> s.onLoop(timestamp));
    }

    @Override
    public void writePeriodicOutputs() {
        mOuttakeModules.forEach(SubsystemBase::writePeriodicOutputs);
    }

    @Override
    public void onStop(double timestamp) {
        mOuttakeModules.forEach(s -> s.onStop(timestamp));
    }

    @Override
    public void zeroSensors() {
        mOuttakeModules.forEach(SubsystemBase::zeroSensors);
    }

    @Override
    public void terminate() {
        mOuttakeModules.forEach(SubsystemBase::terminate);
    }

    @Override
    public void outputData() {
        SmartDashboard.putString("Outtake State", stateStr);
        SmartDashboard.putString("Shoot State", sstateStr);
        SmartDashboard.putNumber("SR_Demand_TR", mRollerDemands1.topRollerRPM);
        SmartDashboard.putNumber("SR_Demand_S", mRollerDemands1.shooterRPM);


        mOuttakeModules.forEach(SubsystemBase::outputData);
    }

    /**
     * Get roller speeds based on a percentage difference between the bottom and top roller
     *
     * @param pBaseSpeed Base speed for the bottom roller
     * @param pTopRollerPercentage Percentage difference between the bottom and top roller
     *
     * @return converted bottom and top roller speeds
     */
    private RollerDemands getRollerSpeeds(double pBaseSpeed, double pTopRollerPercentage) {
        double shooterSpeed = SHOOTER_GEAR_RATIO * pBaseSpeed;
        double rollerSpeed = TOP_ROLLER_RATIO * pTopRollerPercentage * pBaseSpeed;

        return new RollerDemands(shooterSpeed, rollerSpeed);
    }

    /**
     * Find the amount of absolute encoder pulses for a hood angle
     *
     * @param pAngle in degrees
     * @return hood setpoint in encoder pulses
     */
    private double transformHoodAngle(double pAngle) {
        // y = Ax + B;
        return (HOOD_ANGLE_LIN_A * pAngle) + HOOD_ANGLE_LIN_B;
    }
}
