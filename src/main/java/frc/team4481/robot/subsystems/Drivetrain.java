package frc.team4481.robot.subsystems;

import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.path.AdaptivePurePursuitController;
import frc.team4481.frclibrary4481.path.MotorDemand;
import frc.team4481.frclibrary4481.path.Odometry;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.frclibrary4481.subsystems.components.*;

import static frc.team4481.robot.Constants.*;

public class Drivetrain extends SubsystemBase<DrivetrainController> {
    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    private Shooter mShooter;
    private ShooterController mShooterController;

    //Subsystem Components
    MultiMotorComponent mLeft;
    MultiMotorComponent mRight;

    MotorDemand mMotorDemand;

    PigeonIMU mIMU;
    TalonFX mLeadLeftMotor;
    TalonFX mLeadRightMotor;

    AdaptivePurePursuitController mAPPController;
    Odometry mOdometry;

    SimpleMotorFeedforward mFeedforward;
    PIDController mPidCL;
    PIDController mPidCR;

    NetworkTable mVisionTable;

    public final Field2d mField = new Field2d();

    private double horizonCross;
    private double verticleCross;
    private boolean bVision = false;

    //Subsystem Local Variable
    private double leftPower;
    private double rightPower;
    private double speed;
    private double forward;
    private double backward;
    private double rotation;
    private double quickrotation;
    private double rotationSpeed = 0.5;
    private double quickRotationSpeed = 0.45;
    private double heading;


    public Drivetrain(){
        mName = "Drivetrain";
        mSubsystemController = new DrivetrainController();

        mLeft = new MultiMotorComponent(
                new SingleFalconMotorComponent(DRIVETRAIN_LEFT_FRONT_MOTOR, true),
                new SingleFalconMotorComponent(DRIVETRAIN_LEFT_MID_MOTOR, false)
        );

        mRight  = new MultiMotorComponent(
                new SingleFalconMotorComponent(DRIVETRAIN_RIGHT_FRONT_MOTOR, false),
                new SingleFalconMotorComponent(DRIVETRAIN_RIGHT_MID_MOTOR, false)
        );

        mLeft.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);
        mRight.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);

        mLeft.coastMode();
        mRight.coastMode();

        mLeadLeftMotor = (TalonFX) mLeft.getLeadMotor();
        mLeadRightMotor = (TalonFX) mRight.getLeadMotor();

        mIMU = new PigeonIMU(PIGEON_IMU);

        double amp_L = 45;
        double amp_R = 45;
        mLeadLeftMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true,amp_L, amp_L,0.5));
        mLeadRightMotor.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true,amp_R, amp_R,0.5));
        mMotorDemand = new MotorDemand(0,0);

        // Pure Pursuit
        mAPPController = new AdaptivePurePursuitController();
        mOdometry = new Odometry();

        mSubsystemController.setOdometry(mOdometry);
        mSubsystemController.setAPPController(mAPPController);

        // PIDF tuning
        mFeedforward = new SimpleMotorFeedforward(DRIVETRAIN_kS,DRIVETRAIN_kV,DRIVETRAIN_kA);
        mPidCL = new PIDController(DRIVETRAIN_kP,DRIVETRAIN_kI,DRIVETRAIN_kD);
        mPidCR = new PIDController(DRIVETRAIN_kP,DRIVETRAIN_kI,DRIVETRAIN_kD);


        mVisionTable = NetworkTableInstance.getDefault().getTable("limelight");

        // Create Field2d on shuffleboard
        SmartDashboard.putData("Field", mField);
    }

    @Override
    public void onStart(double timestamp) {
        // Subsystem startup routines
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();

        mLeft.activate();
        mRight.activate();

        if (DriverStation.isAutonomous()){
            //AUTONOMOUS IS HANDLED BY THE APP CONTROLLER, DO NOTHING HERE
            mSubsystemController.setControlState(DrivetrainController.controlState.DISABLED);
            mSubsystemController.setBrakeMode(true);
        }else if(DriverStation.isTeleop()){
            mSubsystemController.setControlState(DrivetrainController.controlState.GTA);
        }else{
            mSubsystemController.setControlState(DrivetrainController.controlState.DISABLED);
        }

        zeroSensors();
    }

    @Override
    public void readPeriodicInputs() {

    }

    @Override
    public void onLoop(double timestamp) {
        switch (mSubsystemController.getControlState()) {
            case DISABLED:
                mLeft.setTargetValue(0);
                mRight.setTargetValue(0);
                break;
            case GTA:
                if (mSubsystemController.isBoost()) {
                    speed = DRIVE_BOOST_VELOCITY;
                } else {
                    speed = DRIVE_DEFAULT_VELOCITY;
                }

                //convert inputs
                forward = Math.pow(mSubsystemController.getForward(), 3) * speed; // Trigger^3 * speed = forward
                backward = Math.pow(mSubsystemController.getBackward(), 3) * speed; //backward Trigger^3 * speed = forward
                rotation = Math.pow(mSubsystemController.getHeading(), 3) * rotationSpeed; // X^3 * speed = rotation
                quickrotation = Math.pow(mSubsystemController.getHeading(), 3) * quickRotationSpeed; // X^3 * speed = rotation

                //calculate drive speeds for left & right
                if (forward - backward == 0) {
                    if (mSubsystemController.isBoost()) {
                        mMotorDemand.left = rotation;
                        mMotorDemand.right = -rotation;
                    } else {
                        mMotorDemand.left = quickrotation;
                        mMotorDemand.right = -quickrotation;
                    }
                } else if (forward - backward > 0) {
                    mMotorDemand.left = forward - backward + rotation;
                    mMotorDemand.right = forward - backward - rotation;
                } else if (forward - backward < 0) {
                    mMotorDemand.left = forward - backward + rotation;
                    mMotorDemand.right = forward - backward - rotation;
                }

                //set drive speed left & right
		        mLeft.setTargetValue(mMotorDemand.left);
                mRight.setTargetValue(mMotorDemand.right);

                break;
            case PATH_FOLLOWING:
                double heading = Math.toRadians(mIMU.getYaw());

                // TODO offload conversion?
                //calculate distance driver
                double posL = mLeadLeftMotor.getSelectedSensorPosition(0)/  2048.0 * GEAR_RATIO * CIRCUMFERENCE_WHEELS;
                double posR = mLeadRightMotor.getSelectedSensorPosition(0)/ 2048.0 * GEAR_RATIO * CIRCUMFERENCE_WHEELS;

                //get new position of robot on field
                mOdometry.updatePose(posR, posL, heading);
                Pose2d mPose = mOdometry.getPose();

                // Update pose in Shuffleboard plot element
                mField.setRobotPose(mPose);

                mMotorDemand = mAPPController.update(mPose);

                // Plot lookahead to field in Shuffleboard
                mField.getObject("lookahead").setPose(
                        new Pose2d(mAPPController.getTargetPos(), new Rotation2d(0))
                );

                //set motor voltage
                mLeft.setTargetValue(
                        calcTargetVoltage(mLeadLeftMotor, mPidCL,mMotorDemand.left) / RobotController.getBatteryVoltage()
                );
                mRight.setTargetValue(
                        calcTargetVoltage(mLeadRightMotor, mPidCR, mMotorDemand.right) / RobotController.getBatteryVoltage()
                );
                break;
            case VISION:
                //aim drive train towards a target
                horizonCross = mVisionTable.getEntry("tx").getDouble(0.0); //get crosshair horizontal offset. the center of the screen is 0,0.
                mLeft.setTargetValue(HOMING_TURNING_P * horizonCross);
                mRight.setTargetValue(HOMING_TURNING_P * -horizonCross);


                SmartDashboard.putNumber("horizonCross", horizonCross);

                mShooterController.setShotDistance(calcVisionDistance()+19);
                break;
        }

        if (mSubsystemController.getControlState() == DrivetrainController.controlState.VISION) {
            mShooterController.setVisionAtSetpoint(
                    Math.abs(horizonCross) <
                            VISION_ERROR_MARGIN
            );
        } else {
            mShooterController.setVisionAtSetpoint(true);
        }

        //make a foto of the current vision view
        if (mSubsystemController.getMakeSnapshot()) {
            mVisionTable.getEntry("snapshot").setNumber(1);
        } else {
            mVisionTable.getEntry("snapshot").setNumber(0);
        }

        if(mSubsystemController.isBrakeMode()){
            mLeft.brakeMode();
            mRight.brakeMode();
        }else{
            mLeft.coastMode();
            mRight.coastMode();
        }
    }
    @Override
    public void writePeriodicOutputs() {
        mLeft.update();
        mRight.update();
    }

    @Override
    public void onStop(double timestamp) {
        terminate();
    }

    @Override
    public void zeroSensors() {
        mIMU.setYaw(0);
        mLeadLeftMotor.setSelectedSensorPosition(0);
        mLeadRightMotor.setSelectedSensorPosition(0);
    }

    @Override
    public void terminate() {
        //For final disabling only
        setCoastMode();
        mLeft.deactivate();
        mRight.deactivate();
        //gyro.deactivate();
        mSubsystemController.setControlState(DrivetrainController.controlState.DISABLED);

    }

    @Override
    public void outputData() {
        SmartDashboard.putBoolean("D_boost", mSubsystemController.isBoost());
        SmartDashboard.putNumber("D_heading", mSubsystemController.getHeading());
        SmartDashboard.putNumber("D1_forward", mSubsystemController.getForward());
        SmartDashboard.putNumber("D_backward", mSubsystemController.getBackward());
        SmartDashboard.putNumber("D1_leftPower", mMotorDemand.left);
        SmartDashboard.putNumber("D1_rightPower", mMotorDemand.right);
        SmartDashboard.putString("D2_state", mSubsystemController.getControlState().toString());
        SmartDashboard.putNumber("D2_gyro", mIMU.getYaw());
        SmartDashboard.putNumber("D_amp_L", ((TalonFX)mLeft.getLeadMotor()).getStatorCurrent());
        SmartDashboard.putNumber("D_amp_R", ((TalonFX)mRight.getLeadMotor()).getStatorCurrent());

        SmartDashboard.putNumber("Position L", mLeadLeftMotor.getSelectedSensorPosition(0) /  2048.0 * GEAR_RATIO * CIRCUMFERENCE_WHEELS);
        SmartDashboard.putNumber("Position R", mLeadRightMotor.getSelectedSensorPosition(0) /  2048.0 * GEAR_RATIO * CIRCUMFERENCE_WHEELS);
        SmartDashboard.putNumber("Distance" ,calcVisionDistance());
        SmartDashboard.putNumber("BatVoltage", RobotController.getBatteryVoltage());
    }

    /**
     * Calculates the target voltage for a given motor based on given setpoint
     *
     * @param pTargetMotor Motor for which to calculate the target voltage
     * @param pVelocitySetpoint Velocity setpoint in m/s
     * @return Voltage setpoint
     */
    private double calcTargetVoltage(TalonFX pTargetMotor, PIDController pPidC, double pVelocitySetpoint) {
        double ffComponent = mFeedforward.calculate(pVelocitySetpoint);
        double pidComponent = pPidC.calculate(
                pTargetMotor.getSelectedSensorVelocity(0) * SENSOR_TALONFX_TO_VELOCITY,
                pVelocitySetpoint
        );

        return ffComponent + pidComponent;
    }

    /**
     * Calculate distance from robot to the target.
     */
    private double calcVisionDistance(){
        double angleYCrosshair = mVisionTable.getEntry("ty").getDouble(0);
        return (HEIGHT_CENTER_TAPE_HUB - HEIGHT_CENTER_CAMERA_LENS) / Math.tan(Math.toRadians(ANGLE_CAMERA_DEG + angleYCrosshair));
    }


    public void setCoastMode(){
        mLeft.coastMode();
        mRight.coastMode();
    }

    public void setBrakeMode(){
        mLeft.brakeMode();
        mRight.brakeMode();
    }
}
