package frc.team4481.robot.subsystems.modules;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4481.frclibrary4481.subsystems.SubsystemBase;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.frclibrary4481.subsystems.components.MotorController;
import frc.team4481.frclibrary4481.subsystems.components.MultiMotorComponent;
import frc.team4481.frclibrary4481.subsystems.components.SingleSparkMaxMotorComponent;
import frc.team4481.robot.subsystems.Shooter;
import frc.team4481.robot.subsystems.ShooterController;

import static frc.team4481.robot.Constants.*;

public class ShooterRollers extends SubsystemBase<ShooterRollersController> {
    private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
    private Shooter mShooter;
    private ShooterController mShooterController;
    private double shootSpeed;
    private double topRollerSpeed;

    private SingleSparkMaxMotorComponent mRoller;
    private final CANSparkMax mTopRollerLeadMotor;
    private PIDController mTopRollerPID;
    private final SimpleMotorFeedforward mTopRollerFeedForward;

    private MultiMotorComponent mShooterWheel;
    private final CANSparkMax mShooterLeadMotor;
    private PIDController mShooterPID;
    private final SimpleMotorFeedforward mShooterFeedForward;

    public ShooterRollers(){
        mName = "ShooterRollers";
        mSubsystemController = new ShooterRollersController();

        mRoller = new SingleSparkMaxMotorComponent(ROLLER_MOTOR, ROLLER_INVERT);
        mTopRollerLeadMotor = mRoller.getMotor();
        mTopRollerPID = new PIDController(ROLLER_kP,ROLLER_kI,ROLLER_kD);
        mTopRollerFeedForward = new SimpleMotorFeedforward(ROLLER_kS, ROLLER_kV, ROLLER_kA);

        mShooterWheel = new MultiMotorComponent(
                new SingleSparkMaxMotorComponent(SHOOTER_MOTOR1, MOTOR1_INVERT),
                new SingleSparkMaxMotorComponent(SHOOTER_MOTOR2, MOTOR2_INVERT)
        );
        mShooterWheel.setInverted(true);

        mShooterWheel.setControlMode(MotorController.ControlMode.PERCENTOUTPUT);

        mShooterLeadMotor = (CANSparkMax) mShooterWheel.getLeadMotor();
        mShooterPID = new PIDController(SHOOTER_kP,SHOOTER_kI,SHOOTER_kD);//.02)1;
        mShooterFeedForward = new SimpleMotorFeedforward(SHOOTER_kS, SHOOTER_kV, SHOOTER_kA);

    }

    @Override
    public void onStart(double timestamp) {
        mShooter = (Shooter) mSubsystemManager.getSubsystemByClass(Shooter.class);
        mShooterController = mShooter.getSubsystemController();
        mRoller.deactivate();
        mRoller.coastMode();

        mShooterWheel.deactivate();
        mShooterWheel.coastMode();
    }

    @Override
    public void readPeriodicInputs() {

    }

    @Override
    public void onLoop(double timestamp) {
        switch (mSubsystemController.getControlState()) {
            case IDLE:
                shootSpeed = 0;
                topRollerSpeed = 0;

                mSubsystemController.setAtSetpoint(false);
                break;
            case ENABLED:
                // Check whether the speed for the first and second ball needs to deviate and adjust the setpoint
                if(mShooterController.getNumberOfShots() > 0){
                    shootSpeed = mSubsystemController.getBall2ShooterSpeed();
                    topRollerSpeed = mSubsystemController.getBall2TopRollerSpeed();
                } else {
                    shootSpeed = mSubsystemController.getBall1ShooterSpeed();
                    topRollerSpeed = mSubsystemController.getBall1TopRollerSpeed();
                }

                mSubsystemController.setAtSetpoint(
                        Math.abs(mShooterLeadMotor.getEncoder().getVelocity() - shootSpeed) <
                                SHOOTER_ERROR_MARGIN
                );
                break;
        }
    }

    @Override
    public void writePeriodicOutputs() {

        double topRollerFeedForward = mTopRollerFeedForward.calculate(topRollerSpeed / 60d);
        double feedForward = mShooterFeedForward.calculate(shootSpeed / 60d);

        if(shootSpeed != 0) {
            mTopRollerLeadMotor.setVoltage(
                    topRollerFeedForward +
                            mTopRollerPID.calculate(
                                    mTopRollerLeadMotor.getEncoder().getVelocity() / 60,
                                    topRollerSpeed / 60)
            );
            mShooterLeadMotor.setVoltage(
                    feedForward +
                            mShooterPID.calculate(
                                    mShooterLeadMotor.getEncoder().getVelocity()/60,
                                    shootSpeed/60)
            );
        }else{
            mTopRollerLeadMotor.setVoltage(0);
            mShooterLeadMotor.setVoltage(0);
        }
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
        mShooterWheel.deactivate();
        mRoller.deactivate();
    }

    @Override
    public void outputData() {
        SmartDashboard.putNumber("SR_OutputVelocity", mShooterLeadMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("SR_TopRollerVelocity", mTopRollerLeadMotor.getEncoder().getVelocity());
        SmartDashboard.putBoolean("SR_atSet", mSubsystemController.getAtSetpoint());

    }
}
