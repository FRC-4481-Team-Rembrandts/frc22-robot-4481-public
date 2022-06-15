/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team4481.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.team4481.frclibrary4481.auto.AutoModeBase;
import frc.team4481.frclibrary4481.auto.AutoModeExecutor;
import frc.team4481.frclibrary4481.looper.DisabledSuperSubsystemLoop;
import frc.team4481.frclibrary4481.looper.EnabledSuperSubsystemLoop;
import frc.team4481.frclibrary4481.looper.Looper;
import frc.team4481.frclibrary4481.subsystems.SubsystemManager;
import frc.team4481.robot.auto.selector.AutoModeSelector;
import frc.team4481.robot.subsystems.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * methods corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.<br>
 * <br>
 * This class, via its super class, provides the following methods which are called by the main loop:<br>
 * <br>
 *   - startCompetition(), at the appropriate times:<br>
 * <br>
 *   - robotInit() -- provide for initialization at robot power-on<br>
 * <br>
 * init methods -- each of the following methods is called once when the appropriate mode is entered:<br>
 *     - disabledInit()   -- called each and every time disabled is entered from another mode<br>
 *     - autonomousInit() -- called each and every time autonomous is entered from another mode<br>
 *     - teleopInit()     -- called each and every time teleop is entered from another mode<br>
 *     - testInit()       -- called each and every time test is entered from another mode<br>
 * <br>
 * periodic methods -- each of these methods is called on an interval:<br>
 *   - robotPeriodic()<br>
 *   - disabledPeriodic()<br>
 *   - autonomousPeriodic()<br>
 *   - teleopPeriodic()<br>
 *   - testPeriodic()<br>
 */
public class Robot extends TimedRobot
{
    /**
     * This method is run when the robot is first started up and should be
     * used for any initialization code.
     */
  private final Looper mEnabledLooper = new Looper();
  private final Looper mDisabledLooper = new Looper();

  private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
  private final HIDManager mHIDManager = HIDManager.getInstance();

  private AutoModeExecutor mAutoModeExecutor;
  private final AutoModeSelector mAutoModeSelector = new AutoModeSelector();

  @Override
  public void robotInit() {
    mSubsystemManager.setSubsystems(
            new Drivetrain(),
            new Intake(),
            new Shooter(),
            new Climber()
    );
    mHIDManager.getControllers(mSubsystemManager);

    mEnabledLooper.register(new EnabledSuperSubsystemLoop(mSubsystemManager));
    mDisabledLooper.register(new DisabledSuperSubsystemLoop(mSubsystemManager));

    mAutoModeSelector.reset();
    mAutoModeSelector.updateAutoModeSelector();

  }

    /**
     * This method is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     * <p>
     * This runs after the mode specific periodic methods, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic()
    {
        mEnabledLooper.outputToSmartDashboard();
    }    

  /**
   * Initialization code for autonomous mode should go here.
   * <p>
   * Users should use this method for initialization code which will be called each time the
   * robot enters autonomous mode.
   */
    @Override
    public void autonomousInit()
    {
      mDisabledLooper.stop();

      if (mAutoModeExecutor != null && mAutoModeExecutor.getAutoMode() != null)
        mAutoModeExecutor.start();

      mEnabledLooper.start();
    }

    /**
     * This method is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic()
    {
        
    }

    /**
     * Initialization code for teleop mode should go here.
     * <p>
     * Users should use this method for initialization code which will be called each time the
     * robot enters teleop mode.
     */
    @Override
    public void teleopInit()
    {
      mDisabledLooper.stop();
      if (mAutoModeExecutor != null) {
        mAutoModeExecutor.stop();
      }

      mEnabledLooper.start();
    }

    /**
     * This method is called periodically during operator control.
     */

    @Override
    public void teleopPeriodic()
    {
      mHIDManager.update();
    }

  /**
   * Initialization code for test mode should go here.
   * <p>
   * Users should use this method for initialization code which will be called each time the
   * robot enters test mode.
   */
    @Override
    public void testInit()
    {
        
    }

    /**
     * This method is called periodically during test mode.
     */
    @Override
    public void testPeriodic()
    {
        
    }

    @Override
    public void disabledInit()
    {
      mEnabledLooper.stop();
      if (mAutoModeExecutor != null) {
        mAutoModeExecutor.stop();
      }

      mAutoModeExecutor = new AutoModeExecutor();

      mAutoModeSelector.reset();
      mAutoModeSelector.updateAutoModeSelector();
      mDisabledLooper.start();
    }

    @Override
    public void disabledPeriodic()
    {
      mAutoModeSelector.updateAutoModeSelector();

      AutoModeBase autoMode = mAutoModeSelector.getAutoMode();

      if (autoMode != null)
        mAutoModeExecutor.setAutoMode(autoMode);
    }
}
