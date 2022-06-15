package frc.team4481.robot.HIDlayout;

import frc.team4481.frclibrary4481.HIDlayout.HIDLayout;
import frc.team4481.frclibrary4481.controller.BlackHID;
import frc.team4481.frclibrary4481.controller.OrangeHID;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.frclibrary4481.throwable.HardwareException;
import frc.team4481.frclibrary4481.util.CountingDelay;
import frc.team4481.robot.subsystems.*;

import static frc.team4481.frclibrary4481.controller.IPS4HID.Axis.*;
import static frc.team4481.frclibrary4481.controller.IPS4HID.Button.*;
import static frc.team4481.robot.Constants.CLIMBER_BRAKE_DELAY;

public class WorldsLayout extends HIDLayout {
    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();

    private Shooter mShooter;
    private ShooterController mShooterController;

    private Intake mIntake;
    private IntakeController mIntakeController;

    private Drivetrain mDrivetrain;
    private DrivetrainController mDrivetrainController;

    private Climber mClimber;
    private ClimberController mClimberController;

    private CountingDelay mClimberBrakeDelay;

    private boolean colorFilterLatch = true;
    private boolean matchStart = true;

    /**
     * Creates a new HID Layout for driver and operator control
     *
     * @param orange ID of the orange controller (DRIVER)
     * @param black ID of the black controller (OPERATOR)
     */
    public WorldsLayout(OrangeHID orange, BlackHID black) {
        super(orange, black);
        mClimberBrakeDelay = new CountingDelay();
    }

    /**
     * Obtain the subsystem and subsystem controller instances by requesting the subsystem manager
     */
    @Override
    public void getControllers() {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();
        mIntake = (Intake) mSubsystemManager.getSubsystemByClass(Intake.class);
        mIntakeController = mIntake.getSubsystemController();
        mDrivetrain = (Drivetrain) mSubsystemManager.getSubsystemByClass(Drivetrain.class);
        mDrivetrainController = mDrivetrain.getSubsystemController();
        mClimber = (Climber) mSubsystemManager.getSubsystemByClass(Climber.class);
        mClimberController = mClimber.getSubsystemController();
    }

    /**
     * Updates the orange controller inputs given by the driver
     * @throws HardwareException
     */
    @Override
    public void updateOrange() throws HardwareException {
        /*---------------------------------------------------------------------------------
         * DRIVING
         ---------------------------------------------------------------------------------*/
        mDrivetrainController.setBoost(orangeController.getButtonValue(BUMPER_R1));
        mDrivetrainController.setHeading(orangeController.getAxisValue(LEFTSTICK_X));

        // Drive slowly against fender while shooting
        if (orangeController.getAxisValue(TRIGGER_L2) > 0.1 && orangeController.getAxisValue(TRIGGER_R2) > 0.1) {
            // Because of boost this percentage is halved
            mDrivetrainController.setBackward(0.6);
        } else {
            mDrivetrainController.setBackward(orangeController.getAxisValue(TRIGGER_L2));
            mDrivetrainController.setForward(orangeController.getAxisValue(TRIGGER_R2));
        }

        // Drive by vision
        if (orangeController.getButtonValue(BUMPER_L1)) {
            mDrivetrainController.setControlState(DrivetrainController.controlState.VISION);
        } else {
            mDrivetrainController.setControlState(DrivetrainController.controlState.GTA);
        }

        /*---------------------------------------------------------------------------------
         * INTAKE
         ---------------------------------------------------------------------------------*/
        // Set Reverse Intake type
        if (orangeController.getButtonValue(TRIANGLE)) {
            mShooterController.setReverseState(ShooterController.reverseState.FULL);
        } else if (orangeController.getButtonValue(CIRCLE)) {
            mShooterController.setReverseState(ShooterController.reverseState.SINGLE);
        }
        mShooterController.setReverse(orangeController.getButtonValue(TRIANGLE) || orangeController.getButtonValue(CIRCLE));
        mShooterController.setStorageActive(orangeController.getButtonValue(CROSS) || blackController.getButtonValue(CROSS));

        //Intake for both controllers
        mIntakeController.setIntakeDeployed(
                orangeController.getButtonValue(CROSS) ||
                orangeController.getButtonValue(TRIANGLE) ||
                orangeController.getButtonValue(CIRCLE)
        );

        mIntakeController.setIntakeForwardEnabled(orangeController.getButtonValue(CROSS));
        mIntakeController.setIntakeReversedEnabled(orangeController.getButtonValue(TRIANGLE) || orangeController.getButtonValue(CIRCLE));

        if (orangeController.getButtonValue(TRIANGLE)) {
            mShooterController.setReverseState(ShooterController.reverseState.FULL);
        } else if (orangeController.getButtonValue(CIRCLE)) {
            mShooterController.setReverseState(ShooterController.reverseState.SINGLE);
        }
    }

    /**
     * Updates the black controller inputs given by the operator
     * @throws HardwareException
     */
    @Override
    public void updateBlack() throws HardwareException {
        /*---------------------------------------------------------------------------------
         * COLOR FILTERING
         ---------------------------------------------------------------------------------*/
        if (matchStart) {
            mShooterController.setColorFilterActive(true);
            matchStart = false;
        }

        if(blackController.getButtonValue(OPTIONS)) {
            if (!colorFilterLatch){
                colorFilterLatch = true;
                mShooterController.setColorFilterActive(!mShooterController.getColorFilterActive());
            }
        } else {
            colorFilterLatch = false;
        }

        /*---------------------------------------------------------------------------------
         * SHOOTING
         ---------------------------------------------------------------------------------*/
        mShooterController.setOuttakeActive((blackController.getAxisValue(TRIGGER_R2) > 0.1) || orangeController.getButtonValue(BUMPER_L1));

        // Shooting buttons
        if (orangeController.getButtonValue(BUMPER_L1)) {
            mShooterController.setDesiredShootState(ShooterController.ShootState.AUTOMATIC);
        } else if (blackController.getAxisValue(TRIGGER_L2) > 0.1) {
            mShooterController.setDesiredShootState(ShooterController.ShootState.LOW2X);
        } else if (blackController.getButtonValue(BUMPER_R1)) {
            mShooterController.setDesiredShootState(ShooterController.ShootState.STEAL);
        } else if (blackController.getButtonValue(BUMPER_L1)) {
            mShooterController.setDesiredShootState(ShooterController.ShootState.HIGH2X);
        }

        // Set shot active
        mShooterController.setShootActive(
                orangeController.getButtonValue(BUMPER_L1) ||
                        (blackController.getAxisValue(TRIGGER_L2) > 0.1) ||
                        blackController.getButtonValue(BUMPER_R1)  ||
                        blackController.getButtonValue(BUMPER_L1)
        );

        // Put drivetrain in brake mode while shooting
        mDrivetrainController.setBrakeMode(mShooterController.getShootActive());

        /*---------------------------------------------------------------------------------
         * CLIMBING
         ---------------------------------------------------------------------------------*/
        //Move the climber with a little delay to allow for the lock to detach
        if (Math.abs(blackController.getAxisValue(LEFTSTICK_Y)) > 0.1) {
            mClimberController.setTurnSpeed(blackController.getAxisValue(LEFTSTICK_Y));
        } else if (blackController.getButtonValue(TRIANGLE)){
            mClimberController.setTurnSpeed(-0.3);
        } else {
            mClimberController.setTurnSpeed(0);
        }

        if (blackController.getButtonValue(SQUARE)){
            mClimberController.setResetAutoClimber(true);
        }
        mClimberController.setManualFlipOut(blackController.getButtonValue(CIRCLE));
    }
}
