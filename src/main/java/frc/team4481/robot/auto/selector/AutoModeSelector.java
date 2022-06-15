package frc.team4481.robot.auto.selector;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.auto.AllianceColor;
import frc.team4481.frclibrary4481.auto.AutoModeBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.auto.modes.*;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;

/**
 * Class for selecting of autonomous mode via the driver station.
 *
 * Inspired by Team 254
 */
public class AutoModeSelector {
    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    private Shooter mShooter;
    private ShooterController mShooterController;

    private AutoModeBase mAutoMode;

    private AutoModeList mSelectedAutoMode = null;
    private AllianceColor mSelectedAlliance = null;

    private final SendableChooser<AutoModeList> mModeChooser;
    private final SendableChooser<AllianceColor> mAllianceChooser;

    private final TwoBallHangarAuto mTwoBallHangarAuto = new TwoBallHangarAuto();

    private final OneBallStealAutoBlue mOneBallStealAutoBlue = new OneBallStealAutoBlue();
    private final OneBallStealAutoRed mOneBallStealAutoRed = new OneBallStealAutoRed();

    private final TwoBallStealAutoBlue mTwoBallStealAutoBlue = new TwoBallStealAutoBlue();
    private final TwoBallStealAutoRed mTwoBallStealAutoRed = new TwoBallStealAutoRed();

    private final FiveBallAutoBlue mFiveBallAutoBlue = new FiveBallAutoBlue();
    private final FiveBallAutoRed mFiveBallAutoRed = new FiveBallAutoRed();

    private final DoNothing mDoNothing = new DoNothing();

    /**
     * Creates an autonomous mode selector which is visible on the driver station.
     */
    public AutoModeSelector() {
        mAllianceChooser = new SendableChooser<>();
        mAllianceChooser.setDefaultOption("RED", AllianceColor.RED);
        mAllianceChooser.addOption("BLUE", AllianceColor.BLUE);


        mModeChooser = new SendableChooser<>();
        mModeChooser.setDefaultOption("DO NOTHING", AutoModeList.DO_NOTHING);
        mModeChooser.addOption("2 BALL HANGAR", AutoModeList.TWO_BALL_HANGAR);
        mModeChooser.addOption("1 BALL STEAL", AutoModeList.ONE_BALL_STEAL);
        mModeChooser.addOption("2 BALL STEAL", AutoModeList.TWO_BALL_STEAL);
        mModeChooser.addOption("5 BALL AUTO", AutoModeList.FIVE_BALL_AUTONOMOUS);

        SmartDashboard.putData(mAllianceChooser);
        SmartDashboard.putData(mModeChooser);
        SmartDashboard.putString("FMS Detected Alliance Color", DriverStation.getAlliance().name());
    }

    /**
     * Update the selected autonomous mode based in the driver station input
     */
    public void updateAutoModeSelector() {
        AutoModeList desiredMode = mModeChooser.getSelected();
        AllianceColor desiredAlliance = mAllianceChooser.getSelected();

        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();

        mShooterController.setAllianceColor(desiredAlliance);


        if (mSelectedAutoMode != desiredMode || mSelectedAlliance != desiredAlliance) {
            mAutoMode = getAutoModeForParams(desiredMode, desiredAlliance);

            mSelectedAutoMode = desiredMode;
            mSelectedAlliance = desiredAlliance;
        }

        if (mAutoMode != null) {
            SmartDashboard.putString("Currently Selected Autonomous Mode", mAutoMode.toString());
        } else {
            SmartDashboard.putString("Currently Selected Autonomous Mode", "NoneSelected");
        }

    }

    /**
     * Get the desired instance of the autonomous mode based on the alliance color and selected autonomous mode
     * from the driver station
     *
     * @param pAutoMode Autonomous mode enum
     * @param pAlliance Alliance color enum
     *
     * @return Instance of autonomous mode to run
     */
    private AutoModeBase getAutoModeForParams(AutoModeList pAutoMode, AllianceColor pAlliance) {
        if (pAutoMode == AutoModeList.DO_NOTHING)
        {
            return mDoNothing;
        }
        switch (pAlliance){
            case RED:
                switch(pAutoMode){
                    case ONE_BALL_STEAL:
                        return mOneBallStealAutoRed;
                    case TWO_BALL_STEAL:
                        return mTwoBallStealAutoRed;
                    case TWO_BALL_HANGAR:
                        return mTwoBallHangarAuto;
                    case FIVE_BALL_AUTONOMOUS:
                        return mFiveBallAutoRed;
                }
                break;
            case BLUE:
                switch(pAutoMode){
                    case ONE_BALL_STEAL:
                        return mOneBallStealAutoBlue;
                    case TWO_BALL_STEAL:
                        return mTwoBallStealAutoBlue;
                    case TWO_BALL_HANGAR:
                        return mTwoBallHangarAuto;
                    case FIVE_BALL_AUTONOMOUS:
                        return mFiveBallAutoBlue;
                }
                break;
        }

        return null;
    }

    public AutoModeBase getAutoMode() {return mAutoMode;}

    public void reset() {
        mAutoMode = null;
        mSelectedAutoMode = null;
        mSelectedAlliance = null;
    }
}

