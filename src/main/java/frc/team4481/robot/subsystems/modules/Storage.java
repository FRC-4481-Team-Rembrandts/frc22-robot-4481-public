package frc.team4481.robot.subsystems.modules;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.auto.AllianceColor;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.frclibrary4481.subsystems.components.MotorController;
import frc.team4481.frclibrary4481.subsystems.components.SingleSparkMaxMotorComponent;
import frc.team4481.frclibrary4481.util.CountingDelay;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;
import static frc.team4481.robot.Constants.*;


public class Storage extends SubsystemBase<StorageController> {
    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    private Shooter mShooter;
    private ShooterController mShooterController;
    private String stateStr = "";

    private SingleSparkMaxMotorComponent mKickerMotor;
    private SingleSparkMaxMotorComponent mFeederMotor;
    private CountingDelay mShotDetectionDelay;

    private boolean pulseBuffer = false;
    private boolean isPulseOuttake;
    private int numberOfShots;

    double[] color;
    double[] initColor;
    private NetworkTableEntry mColorSensor;
    private NetworkTableEntry mProxSensor;

    AnalogInput mTopSensor;
    AnalogInput mOutputSensor;

    public Storage() {
        mName = "Shooter";
        mSubsystemController = new StorageController();

        //TODO: ADD CORRECT PORT ID
        mKickerMotor = new SingleSparkMaxMotorComponent(KICKWHEEL_MOTOR, KICKWHEEL_INVERT);
        mFeederMotor = new SingleSparkMaxMotorComponent(INTAKE_STORAGE_MOTOR,FEEDER_INVERT);

        mShotDetectionDelay = new CountingDelay();

        mTopSensor = new AnalogInput(0);
        mOutputSensor = new AnalogInput(1);

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("piColor");
        mColorSensor = table.getEntry("likelycolor1");
        mProxSensor = table.getEntry("proximity1");

        initColor = new double[]{0, 0, 0};
    }

    @Override
    public void onStart(double timestamp) {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();

        mKickerMotor.activate();
        mKickerMotor.brakeMode();
        mKickerMotor.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);

        mFeederMotor.activate();
        mFeederMotor.coastMode();
        mFeederMotor.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);

        SmartDashboard.putBoolean("First Ball Positioned", false);
        SmartDashboard.putBoolean("Mismatch Ball 1", false);
        SmartDashboard.putBoolean("Ball in Outtake", false);
        SmartDashboard.putBoolean("Pulse Outtake", false);

        // Get initial color values for comparison
        initColor = mColorSensor.getDoubleArray(new double[3]);
    }

    @Override
    public void readPeriodicInputs() {
        // Read color values and detect possible mismatch
        color = mColorSensor.getDoubleArray(new double[3]);
        try {
            if (color[0] - initColor[0] > COLOR_DELTA_THRESHOLD_RED) { //RED
                mSubsystemController.setMismatchBall1(mShooterController.getAllianceColor() != AllianceColor.RED);
            } else if (color[2] - initColor[2] > COLOR_DELTA_THRESHOLD_BLUE) { //BLUE
                mSubsystemController.setMismatchBall1(mShooterController.getAllianceColor() != AllianceColor.BLUE);
            } else {
                mSubsystemController.setMismatchBall1(false);
            }
        } catch(Exception e) {
            mSubsystemController.setMismatchBall1(false);
        }

        // Check if the first ball is present in the storage
        mSubsystemController.setFirstBallPositioned(mTopSensor.getValue() < SHOOTER_ANALOG_SENSOR_DETECT_BALL);

        // Check if a ball is shot
        isPulseOuttake = mOutputSensor.getValue() < SHOOTER_ANALOG_SENSOR_DETECT_BALL;
    }

    @Override
    public void onLoop(double timestamp) {
        // Set actual motor setpoints based on the state of the shooter
        switch (mSubsystemController.getControlState()) {
            case IDLE:
                stateStr = "IDLE";
                mKickerMotor.setTargetValue(0);
                mFeederMotor.setTargetValue(0);

                numberOfShots = 0;
                mShooterController.setNumberOfShots(numberOfShots);
                break;
            case FEED:
                stateStr = "FEED";

                mKickerMotor.setTargetValue(STORAGE_MOTOR_FEED_SPEED);
                mFeederMotor.setTargetValue(STORAGE_MOTOR_FEED_SPEED);

                updateNumberOfShots();
                break;
            case REVERSE:
                stateStr = "REVERSE";
                mFeederMotor.setTargetValue(STORAGE_MOTOR_REVERSED_SPEED);

                // Either reserve intake one or two balls
                if (mShooterController.getReverseState() == ShooterController.reverseState.FULL) {
                    mKickerMotor.setTargetValue(STORAGE_MOTOR_REVERSED_SPEED);
                } else {
                    mKickerMotor.setTargetValue(0);
                }
                break;
            case FILTER:
                stateStr = "FILTER";

                // Eject ball of the wrong color
                if (mShooterController.getNumberOfShots() > 0) {
                    mKickerMotor.setTargetValue(0);
                } else {
                    mKickerMotor.setTargetValue(STORAGE_MOTOR_FEED_SPEED);
                    mFeederMotor.setTargetValue(STORAGE_MOTOR_FEED_SPEED);
                }
                updateNumberOfShots();

                break;
            case SHOOT:
                stateStr = "SHOOT";

                mKickerMotor.setTargetValue(STORAGE_MOTOR_SHOOT_SPEED);
                mFeederMotor.setTargetValue(STORAGE_MOTOR_SHOOT_SPEED);

                updateNumberOfShots();
                break;
        }
    }

    /**
     * Updates the number of shots made while holding the shoot button or while the shoot action is active
     */
    public void updateNumberOfShots() {
        if (isShotSensorPulse()) {
            System.out.println("no. of shots increased");
            numberOfShots++;
        }

        if (mShotDetectionDelay.delay(0)) {
            mShooterController.setNumberOfShots(numberOfShots);
            mShotDetectionDelay.reset();
        }
    }

    /**
     * Detects when a ball moves past the sensor inside the shooter in order to update the amount of made shots.
     *
     * @return true if a ball was shot
     */
    public boolean isShotSensorPulse() {
        if (isPulseOuttake) {
            if (pulseBuffer) {
                pulseBuffer = false;
                return true;
            }
        }
        else {
            pulseBuffer = true;
        }
        return false;
    }

    @Override
    public void writePeriodicOutputs() {
        mKickerMotor.update();
        mFeederMotor.update();
    }

    @Override
    public void onStop(double timestamp) {
        terminate();
    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void terminate() {
        mKickerMotor.deactivate();
        mFeederMotor.deactivate();
    }

    @Override
    public void outputData() {
        SmartDashboard.putString("Storage State", stateStr);
        SmartDashboard.putBoolean("Ball Pos1", mSubsystemController.getFirstBallPositioned());

        SmartDashboard.putBoolean("Mismatch", mSubsystemController.getMismatchBall1());
        SmartDashboard.putNumber("color_R", color[0]);
        SmartDashboard.putNumber("color_G", color[1]);
        SmartDashboard.putNumber("color_B", color[2]);
        SmartDashboard.putNumber("Icolor_R", initColor[0]);
        SmartDashboard.putNumber("Icolor_G", initColor[1]);
        SmartDashboard.putNumber("Icolor_B", initColor[2]);
        SmartDashboard.putNumber("prox", mProxSensor.getDouble(0.0));
        SmartDashboard.putNumber("SensorVal", mTopSensor.getValue());
        SmartDashboard.putBoolean("Outtake pulse",isPulseOuttake);
    }

}
