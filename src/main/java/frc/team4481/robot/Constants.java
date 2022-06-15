package frc.team4481.robot;

public class Constants {
    /* ---------------------------------------- */
    /* LOOPER */
    /* ---------------------------------------- */
    public static final double kLooperDt = 0.01;

    /* ---------------------------------------- */
    /* HARDWARE */
    /* ---------------------------------------- */
    /*
     * Sensor Id's
     */
    public static final int PIGEON_IMU = 14;
    /*
     * Drivetrain motor CAN ID's
     */
    public static final int DRIVETRAIN_LEFT_FRONT_MOTOR = 10;
    public static final int DRIVETRAIN_LEFT_MID_MOTOR = 11;
    public static final int DRIVETRAIN_RIGHT_FRONT_MOTOR = 12;
    public static final int DRIVETRAIN_RIGHT_MID_MOTOR = 13;
    /*
     * Intake motor CAN and Solenoid ID's
     */
    public static final int INTAKE_LEAD_MOTOR = 20;
    public static final int INTAKE_STORAGE_MOTOR = 21;
    public static final int INTAKE_SOLENOID = 13;

    /*
     * Motor Climber motor CAN, PDH and Solenoid ID's
     */
    public final static int CLIMBER_MOTOR = 40;
    public final static int CLIMBER_BRAKE_SOLENOID_PORT = 15;
    public final static int CLIMBER_HIGH_SOLENOID_PORT = 0;
    public final static int CLIMBER_ANALOG_PORT = 2; //2
    /*
     * Shooter motor CAN, Analog and Solenoid ID's
     */
    public final static int KICKWHEEL_MOTOR = 30;
    public final static int SHOOTER_MOTOR1 = 31;
    public final static int SHOOTER_MOTOR2 = 32;
    public final static int ROLLER_MOTOR = 33;
    public final static int HOOD_MOTOR = 34;
    public final static int HOOD_ENCODER = 0;

    /* ---------------------------------------- */
    /* INTAKE */
    /* ---------------------------------------- */
    /*
     * Speed constants
     */
    public static final double INTAKE_SPEED = 0.6;
    public static final double INTAKE_REVERSE_SPEED = -0.6;
    public static final boolean INTAKE_LEAD_MOTOR_INVERT = false;

    /*
     * Sensor
     */
    public static final double COLOR_DELTA_THRESHOLD_RED = 0.07;
    public static final double COLOR_DELTA_THRESHOLD_BLUE = 0.07;


    /* ---------------------------------------- */
    /* SHOOTER */
    /* ---------------------------------------- */
    /*
     * Motor inversions
     */
    public final static boolean MOTOR1_INVERT = true;
    public final static boolean MOTOR2_INVERT = true;
    public final static boolean KICKWHEEL_INVERT = false;
    public final static boolean FEEDER_INVERT = true;
    public final static boolean ROLLER_INVERT = false;
    public final static boolean HOOD_INVERT = true;
    /*
     * Hood angle constants
     */
    public final static double HOOD_ENCODER_MIN = 0.35;//0.282; // Dev: 0.2018
    public final static double HOOD_ENCODER_MAX = 0.55;//0.6255; // Dev: 0.4866
    public final static double HOOD_ENCODER_HIGH = HOOD_ENCODER_MIN;
    public final static double HOOD_ENCODER_LOW = HOOD_ENCODER_MAX;
    public final static double HOOD_ANGLE_MIN = 13;
    public final static double HOOD_ANGLE_MAX = 43;
    public final static double HOOD_ANGLE_LIN_A = (HOOD_ENCODER_MAX - HOOD_ENCODER_MIN) / (HOOD_ANGLE_MAX - HOOD_ANGLE_MIN);
    public final static double HOOD_ANGLE_LIN_B = HOOD_ENCODER_MIN - HOOD_ANGLE_MIN * HOOD_ANGLE_LIN_A;

    /*
     * Storage constants
     */
    public final static double STORAGE_MOTOR_FEED_SPEED = 0.5;
    public final static double STORAGE_MOTOR_SHOOT_SPEED = 0.9;
    public final static double STORAGE_MOTOR_REVERSED_SPEED = -STORAGE_MOTOR_FEED_SPEED;
    /*
     * Roller Ratios
     */
    public final static double SHOOTER_GEAR_RATIO = 1.5; // motor / output axle
    public final static double TOP_ROLLER_GEAR_RATIO = 2; // motor / ouput axle
    public final static double TOP_ROLLER_DIAMETER_RATIO = 2; //
    public final static double TOP_ROLLER_RATIO = TOP_ROLLER_GEAR_RATIO * TOP_ROLLER_DIAMETER_RATIO;

    /*
     * Velocity/Power constants
     */
    public final static double SHOOTER_VELOCITY_BASE_LOW = 800;
    public final static double SHOOTER_VELOCITY_BASE_HIGH = 2400;
    public final static double ROLLER_TO_SHOOTER_RATIO_BASE_LOW = 1;
    public final static double ROLLER_TO_SHOOTER_RATIO_BASE_HIGH = 0.5;

    public final static double SHOOTER_VELOCITY_2xLOW_BALL1 = SHOOTER_VELOCITY_BASE_LOW;
    public final static double SHOOTER_VELOCITY_2xLOW_BALL2 = SHOOTER_VELOCITY_BASE_LOW;
    public final static double ROLLER_TO_SHOOTER_RATIO_2xLOW_BALL1 = ROLLER_TO_SHOOTER_RATIO_BASE_LOW;
    public final static double ROLLER_TO_SHOOTER_RATIO_2xLOW_BALL2 = ROLLER_TO_SHOOTER_RATIO_BASE_LOW;

    public final static double SHOOTER_VELOCITY_2xHIGH_BALL1 = SHOOTER_VELOCITY_BASE_HIGH;
    public final static double SHOOTER_VELOCITY_2xHIGH_BALL2 = SHOOTER_VELOCITY_BASE_HIGH;
    public final static double ROLLER_TO_SHOOTER_RATIO_2xHIGH_BALL1 = ROLLER_TO_SHOOTER_RATIO_BASE_HIGH;
    public final static double ROLLER_TO_SHOOTER_RATIO_2xHIGH_BALL2 = ROLLER_TO_SHOOTER_RATIO_BASE_HIGH;

    public final static double SHOOTER_VELOCITY_STEAL = 1100;
    public final static double ROLLER_TO_SHOOTER_RATIO_STEAL = 0.75;

    public final static double SHOOTER_VELOCITY_FILTER = 500;
    public final static double ROLLER_TO_SHOOTER_RATIO_FILTER = 1.1;

    /*
     * PIDF values
     */
    public final static double SHOOTER_kP = 0.01; // CompRobot: 0.006 // DevRobot: 0.012866/2;
    public final static double SHOOTER_kI = 0;
    public final static double SHOOTER_kD = 0;

    public final static double SHOOTER_kS = 0.20375;//0.19654; // CompRobot: 0.19654 // DevRobot: 0.19654;
    public final static double SHOOTER_kV = 0.12701;//0.12985;//0.12701; // CompRobot: 0.12701 // DevRobot: 0.12871;
    public final static double SHOOTER_kA = 0.0055207; // CompRobot: 0.00877718 // DevRobot: 0.00877718;

    public final static double ROLLER_kP = 0.01;
    public final static double ROLLER_kI = 0;
    public final static double ROLLER_kD = 0;

    public final static double ROLLER_kS = 0.26428;//0.10272;
    public final static double ROLLER_kV = 0.06392;//0.12785;//0.061463;
    public final static double ROLLER_kA = 0.0014046;//0.001798;

    public final static double HOOD_KP = 5;
    public final static double HOOD_KI = 0;
    public final static double HOOD_KD = 0;

    public final static double SHOOTER_ERROR_MARGIN = 65;
    public final static double HOOD_ERROR_MARGIN = 0.01;

    /*
     * Sensor threshold
     */
    public final static double SHOOTER_ANALOG_SENSOR_DETECT_BALL = 500;
    /*
     * Shooter Delays
     */
    public final static double SHOT_DETECTION_DELAY = 0.1;

    /* ---------------------------------------- */
    /* DRIVETRAIN */
    /* ---------------------------------------- */
    /*
     * Geometric constants
     */
    public static final double DRIVETRAIN_WIDTH = 0.554; // meters
    /*
     * Gearing constants
     */
    public static final double CIRCUMFERENCE_WHEELS = 0.1016 * Math.PI; //meters
    public static final double MOTOR_GEAR_RATIO = 12.0/50.0;
    public static final double GEAR_WHEEL_RATIO = 12.0/22.0;
    public static final double GEAR_RATIO = MOTOR_GEAR_RATIO*GEAR_WHEEL_RATIO;
    public static final double GEAR_REDUCTION = 1/GEAR_RATIO;
    /*
     * TalonFX computation constants
     */
    public static final double FX_TICKS_PER_REVOLUTION = 2048.0;
    public static final double FX_TICKS_SPEED = 0.1; //s
    public static final double FX_TICKS_TO_RPS = ((1/FX_TICKS_PER_REVOLUTION)/FX_TICKS_SPEED);
    public static final double FX_TICKS_TO_RPM = FX_TICKS_TO_RPS*60;
    /*
     * Unit transformations
     */
    public static final double MOTOR_RPM_TO_MPM = GEAR_RATIO * CIRCUMFERENCE_WHEELS;
    public static final double MOTOR_RPM_TO_MPS = MOTOR_RPM_TO_MPM / 60;
    public static final double SENSOR_TALONFX_TO_VELOCITY = FX_TICKS_TO_RPM * MOTOR_RPM_TO_MPS;
    /*
     * Characterization parameters
     */
    public static final double DRIVETRAIN_kP = 3.2016;
    public static final double DRIVETRAIN_kI = 0;
    public static final double DRIVETRAIN_kD = 0;
    public static final double DRIVETRAIN_kS = 0.64827;
    public static final double DRIVETRAIN_kV = 2.5141;
    public static final double DRIVETRAIN_kA = 0.26875;
    /*
     * Current Limits
     */
    public static final double AMP_LIMIT = 45;
    public static final double AMP_OFFSET = 5;
    public static final double AMP_THRESHOLD = AMP_LIMIT + AMP_OFFSET;
    public static final double AMP_THRESHOLD_TIME = 0.5; // seconds
    /*
     * Vision Homing parameters
     */
    public static final double HOMING_TURNING_P = 0.03;
    public static final double HOMING_FORWARD_SPEED = 0.3;
    public static final int PIPELINE_CAMERA = 0;
    public static final int PIPELINE_VISION = 0;
    public static final double HEIGHT_CENTER_TAPE_HUB = 262.5; //CM = TopTape-(HeightTape/2)
    public static final double HEIGHT_CENTER_CAMERA_LENS = 95.5;//CM
    public static final double ANGLE_CAMERA_DEG = 39.47;

    public final static double VISION_ERROR_MARGIN = 1.5;


    /*
     * HID preferences
     */
    public static final double DRIVE_BOOST_VELOCITY = 1.0;
    public static final double DRIVE_DEFAULT_VELOCITY = 0.7;


    /* ---------------------------------------- */
    /* PATH PLANNING */
    /* ---------------------------------------- */
    /*
     * Maximum robot acceleration in m/s^2
     * Maximum robot velocity in m/s
     * Minimum robot velocity in m/s
     */
    public static final double MAX_ACCELERATION = 2.5;
    public static final double MAX_VELOCITY = 4; //3.8
    public static final double MIN_VELOCITY = 0.25;
    /*
     * Maximum distance from final point that counts as ended path in m
     */
    public static final double PATH_END_DISTANCE = 0.3;
    /*
     * Minimum lookahead distance in m
     * Maximum lookahead distance in m
     */
    public static final double MIN_LOOKAHEAD = .25;
    public static final double MAX_LOOKAHEAD = 1;

    /* ---------------------------------------- */
    /* CLIMBER */
    /* ---------------------------------------- */
    public final static double CLIMBER_BRAKE_DELAY = 0.2;
    public static final double CLIMBER_AUTO_DOWN_SPEED = 1.0;
    public static final double CLIMBER_AUTO_UP_SPEED = -1.0;
    public static final double CLIMBER_UPPER_LIMIT = 6.5;
    public static final double CLIMBER_LOWER_LIMIT = -0.7;
    public static final double CLIMBER_AUTO_UPPER_LIMIT = 1;
    public static final double CLIMBER_AUTO_LOWER_LIMIT = CLIMBER_LOWER_LIMIT;
    public final static double CLIMBER_HIGH_HOOK_DELAY = 1.5;
    public static final double CLIMBER_AUTO_HOOK_POINT = 4.0;
    public static final double CLIMBER_CURRENT_LIMIT = 30.0; //50 peak, reduced to 30
    public static final double CLIMBER_SENSOR_LIMIT = 2000.0;
    public static final double CLIMBER_MOTOR_CALIBRATION_POWER = CLIMBER_AUTO_DOWN_SPEED;
}
