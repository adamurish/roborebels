/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
 * Joystick (xbox) button map
 * 0
 * 1 A
 * 2 B
 * 3 X
 * 4 Y
 * 5 L Bumper
 * 6 R Bumper
 * 7 Back
 * 8 Start
 * 9 L Stick Click
 * 10 R Stick Click
 * 
 * Axis
 * 
 *  �1: Left Stick X Axis
-Left:Negative ; Right: Positive
�2: Left Stick Y Axis
-Up: Negative ; Down: Positive
�3: Triggers
-Left: Positive ; Right: Negative
�4: Right Stick X Axis
-Left: Negative ; Right: Positive
�5: Right Stick Y Axis
-Up: Negative ; Down: Positive
�6: Directional Pad (Not recommended, buggy)

 * 
 */

/*
 * 


 * NOTES:
 *
 *
 * - I have decided to clean up our code base a bit. - Derek W.
 * 
 * 
 * TODO:
 * 
 * - Migrate code out of RoboRebels.java into their respective classes
 * 
 * - All classees that depend on a Joystick should be passed joystick object(s)
 *   and handled within their classes
 * 
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    // Declare a variable to use to access the driver station object
    DriverStation m_ds;                   // driver station object
    static DriverStationLCD m_dsLCD;                // driver station LCD object
    Joystick m_rightStick;           // joystick 1 (arcade stick or right tank stick)
    Joystick m_leftStick;            // joystick 2 (tank left stick)
    Joystick m_xboxStick;
    Gyro gyro;
    Encoder rightEncoder, leftEncoder;
    RRDrive drive;
    RRDriveThread driveThread;
    RRShooter shooter;
    RRSlider slider;
    RRGatherer gatherer;
//    RRGathererThread    gathererThread;
    RRBridgeArm arm;
//    RRBridgeArmThread   armThread;
    ADXL345_I2C accel;
    RobotDrive m_robotDrive;
    RRTracker tracker;
    RRShooterTrackerThread shooterTrackerThread;
    RRBallSensor sensor;
    RRDIPSwitch dipSwitch;
    RRButtonMap buttonMap;
    RRAutonomous autonomous;
    //RRTracker tracker = new RRTracker();   // New objects shouldn't be created outside of a method.
    double lastZValue;                         // last Z value for the dial on the joystick
    double autonomousStartTime;    // holds the start time for autonomous mode
    double robotDriveSensitivity = 0.25;       // sensitivity of the RobotDrive object
    boolean tankDrive = false;
    // PWM Channel constants
    final static int LEFT_DRIVE_CHANNEL = 1;
    final static int RIGHT_DRIVE_CHANNEL = 2;
    final static int SHOOTER_CHANNEL = 3;
    final static int TILT_CHANNEL = 7;
    final static int LAZY_SUSAN_CHANNEL = 8;
    final static int LOADER_CHANNEL = 5;
    final static int SPINNER_CHANNEL = 4;
    /*
    final static int    FRONT_SLIDER_LEFT_CHANNEL = 5;
    final static int    BACK_SLIDER_LEFT_CHANNEL = 4;
    final static int    FRONT_SLIDER_RIGHT_CHANNEL = 9;
    final static int    BACK_SLIDER_RIGHT_CHANNEL = 10;
     */
    final static int BRIDGE_ARM_CHANNEL = 6;
    // Digital I/O constants
    final static int BOTTOM_BALL_SENSOR_CHANNEL = 1;
    final static int MIDDLE_BALL_SENSOR_CHANNEL = 2;
    final static int TOP_BALL_SENSOR_CHANNEL = 3;
    final static int TILT_LIMIT_SWITCH_CHANNEL = 4;
    final static boolean DEBUG_ON = false; //true;  // false;  //true;       // true;
    final static boolean MIN_DEBUG_ON = false;  // false;       // true;
    final static boolean TRACKER_DEBUG_ON = false;      //true;
    static final int NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean disabledStateBroadcasted = false;
    static boolean teleopStateBroadcasted = false;
    static boolean autonomousStateBroadcasted = false;
    int pwmTest = 0;
    boolean btnPressed = false;
    double launcher_speed = 0.0;
    boolean launcher_button_pressed = false;
//    final static int    CLOSE_LEFT = -1;
//    final static int    CLOSE_RIGHT = 1;
    final static int LEFT = -2;
    final static int RIGHT = 2;
    final static int FAR_LEFT = -3;
    final static int FAR_RIGHT = 3;
    final static int LOCK = 0;
    
    final static int UP = 1;
    final static int DOWN = -1;
    final static int FASTER = 1;
    final static int SLOWER = -1;
    final static int HOLD = -4;
    final static int MIN_TILT_ANGLE = 40;
    final static int AT_RIGHT_LIMIT = 1;
    final static int AT_LEFT_LIMIT = -1;
    final static int OK = 0;
    static double angle_position = 0.0;     // Initial position of LS.
    static double calc_angle = 0.0;
    static double old_c = 0.0;
    static double distance = 0.0;
    static boolean save_pic_next_time = false;
    static double time_last_update = 0;     // Timestamp of last position update
    final static int PIXEL_ACCURACY = 12;    // 10; barely worked    // 16; worked   //20; worked     // Used by RRTRacker to determne when Locked.
    final static int ANGLE_ACCURACY = 2;     // 4; worked     //6;       // Used by RRTRacker to determne when Locked.
    final static int LOWEST_TARGET = 0;     // Lowest basket target
    //    final static int    MIDDLE = 1;     // Middle basket target
    final static int HIGHEST_TARGET = 2;    // Highest basket target
    final static int LEFT_TARGET = 3;       // Left Middle target
    final static int RIGHT_TARGET = 4;      // Right Middle target
    final static int AUTO_TARGET = 5;       // trackTarget chooses target automatically
    static boolean going_for_highest = false;   // true;       //  was false in old calibration
//    static boolean      continuous_targeting = false;    // Targeting all the time or only when button pressed
    static int target_azimuth = HOLD;  // -1 if target is to left, 0 if on target, 1 if target is the right
    static int target_elevation = HOLD;  // elevation direction of target:  UP, DOWN, LOCK
    static int target_muzzle_velocity = HOLD; //muzzle velocity in meters per second
    static double muzzle_velocity = 7.5;  // Actual muzzle velocity 8.5 meters per second
    final static int NUMBER_OF_PREVIOUS = 5;    // 10;    // Moving average values
    static double previous_angles[] = new double[NUMBER_OF_PREVIOUS];
    static int curent_angle_index = 0;
    static double current_angle_sum = 0;
    static boolean azimuth_lock = false;  //  azimuth (left/right) target lock acquired
    static boolean elevation_lock = false; // elevation (up/down) target lock acquired
    static boolean muzzle_velocity_lock = false;  // muzzle velocity is correct
    static boolean isFinishedShooting = false;  // True when a ball has just been shot
    static boolean isShooting = false;         // True when ball is in process of being shot
    static boolean delay_between_balls = false;  // True when waiting between shooting balls
    static boolean delay_after_two_balls = false;
    static boolean shot_first_ball = false;
    static boolean shot_second_ball = false;
//    final static double TICKS_FOR_3_SECONDS = 3.0;  // Used for delay between shots
    static boolean driving_to_bridge = false;
    static boolean no_balls_shot = true;
    static boolean second_ball_started_shoot = false;
    static boolean autonomous_complete = false;
    static boolean autonomous_mode_tracking = false;
    static boolean autonomous_tracking_failed = false;
    static boolean shooter_motor_running = false;
    static boolean save_camera_image_file = false;  // true;
    static double time_started_waiting;           // Time variables
    static double time_started_shooting;
    static double time_started_shooter_motor;
    static double time_started_tracking;
    static double time_started_driving;
    static double time_after_shooting;
    static double time_delivered_ball;
    static boolean remove_small_objects_from_image = false;    // Set true to try adding back in image processing step
    // During match setup, save image file to set image thresholds
    final static double MAX_TRACKING_TIME = 10.0;    // Time before tracking is given up if no lock obtained
    final static double DELAY_AT_START_OF_AUTON = 2.0;  // Delay set by DIP switch in shooting
    final static double MAX_SHOOTING_TIME = 9.0;  // Total time for shooter to give ball to basket
    final static double DELAY_BETWEEN_SHOTS = 2.0;  // Used for delay between shots in autonomous
    final static double DRIVE_TIME_TO_BRIDGE = 2.0; // Drive to bridge for 2 seconds
    final static double EXPAND_TIME = 3.0;
    final static double SHOOTER_SPINUP_TIME = 1.0;  // Time taken for shooter to get up to speed before we send ball
    final static double SHOOTER_SPINDOWN_TIME = 2.0;  // Time to wait for motor to spin down
    static boolean autonomous_mode = true;
    static boolean troubleshooting = false;  // true;
    static boolean dont_track_azimuth = false;
    static double tilt_angle = 90;        // tilt angle (elevation)

    /*
     *          (\_/)
     *          (O.0)
     *           =o=
     *         (    ) <--- bunny ;)
     *          (  )
     *
     */
    /**
     * Constructor
     */
    public void RoboRebels() {
        RRLogger.logDebug(this.getClass(),"","RoboRebels()");

    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        RRLogger.logDebug(this.getClass(),"robotInit()","");

        m_dsLCD = DriverStationLCD.getInstance();

        m_leftStick = new Joystick(1);
        m_rightStick = new Joystick(2);
        m_xboxStick = new Joystick(3);
        RRLogger.logDebug(this.getClass(),"robotInit()","Joysticks set");

        gyro = new Gyro(1);
        gyro.reset();
        rightEncoder = new Encoder(12, 11, false);
        leftEncoder = new Encoder(14, 13, true);
        rightEncoder.reset();
        rightEncoder.start();
        leftEncoder.reset();
        leftEncoder.start();

        buttonMap = new RRButtonMap(m_leftStick, m_rightStick, m_xboxStick);
        buttonMap.setControllers();
        RRLogger.logDebug(this.getClass(),"robotInit()","Button map");

        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number
        RRLogger.logDebug(this.getClass(),"robotInit()","accel");

        // ******************
//        drive = new RRDrive(2, 1);
        driveThread = new RRDriveThread();
        driveThread.start();

        drive = driveThread.getDrive();

        RRLogger.logDebug(this.getClass(),"robotInit()","Drive");

        // ******************
        gatherer = new RRGatherer(SPINNER_CHANNEL, LOADER_CHANNEL, BOTTOM_BALL_SENSOR_CHANNEL, MIDDLE_BALL_SENSOR_CHANNEL, TOP_BALL_SENSOR_CHANNEL);
//        gathererThread = new RRGathererThread(SPINNER_CHANNEL, LOADER_CHANNEL, BOTTOM_BALL_SENSOR_CHANNEL, MIDDLE_BALL_SENSOR_CHANNEL, TOP_BALL_SENSOR_CHANNEL);
//        gathererThread.start();
        RRLogger.logDebug(this.getClass(),"robotInit()","Gatherer");


//        dipSwitch = new RRDIPSwitch(7, 10);  // These are the values from last year.

        // ******************
//        tracker = new RRTracker(accel, dipSwitch);
//        RRLogger.logDebug(this.getClass(),"","Tracker");

        // ******************
        arm = new RRBridgeArm(BRIDGE_ARM_CHANNEL);
//        armThread = new RRBridgeArmThread(BRIDGE_ARM_CHANNEL);
//        armThread.start();
        RRLogger.logDebug(this.getClass(),"robotInit()","Arm");

        sensor = new RRBallSensor();
        sensor.ballSensorInit(5, 4); // These are the values from last year.

        // ********************
//        shooter = new RRShooter(SHOOTER_CHANNEL, LAZY_SUSAN_CHANNEL, TILT_CHANNEL, 
//                                TILT_LIMIT_SWITCH_CHANNEL, tracker, sensor, dipSwitch, gatherer);
//        
//        tracker.setShooter(shooter);

        shooterTrackerThread = new RRShooterTrackerThread(SHOOTER_CHANNEL, LAZY_SUSAN_CHANNEL, TILT_CHANNEL,
                                                          TILT_LIMIT_SWITCH_CHANNEL, sensor, dipSwitch, gatherer,
                                                          accel, driveThread, drive);
        shooterTrackerThread.start();

        shooter = shooterTrackerThread.getShooter();

        // ********************
//        autonomous = new RRAutonomous(dipSwitch, tracker, shooter, sensor, gathererThread.getGatherer());
        autonomous = new RRAutonomous(shooterTrackerThread.getTracker(), shooterTrackerThread.getShooter(),
                                      sensor, gatherer);

        isFinishedShooting = true;

        // *****************
        //slider = new RRSlider(FRONT_SLIDER_LEFT_CHANNEL, BACK_SLIDER_LEFT_CHANNEL);

        time_last_update = Timer.getFPGATimestamp();

        RRLogger.logInfo(this.getClass(),"robotInit()","-- Configuration Data --");
        RRLogger.logInfo(this.getClass(),"robotInit()","DEBUG_ON: " + DEBUG_ON + " MIN_DEBUG_ON: " + MIN_DEBUG_ON);
        RRLogger.logInfo(this.getClass(),"robotInit()"," Pixel Accuracy: +/-" + (PIXEL_ACCURACY / 2)
                + "pixels Angle Accuracy: +/-" + (ANGLE_ACCURACY / 2) + " degrees");
        RRLogger.logInfo(this.getClass(),"robotInit()"," Max Tracking Time: " + MAX_TRACKING_TIME + " Shooter Spinup Time: "
                + SHOOTER_SPINUP_TIME + " Shooter Spindown Time: " + SHOOTER_SPINDOWN_TIME
                + "Max Shooting Time: " + MAX_SHOOTING_TIME);
        RRLogger.logInfo(this.getClass(),"robotInit()","Autonomous Config.  Expand Time: " + EXPAND_TIME + " Delay at Start: " + DELAY_AT_START_OF_AUTON
                + " Delay Between Shots: " + DELAY_BETWEEN_SHOTS + " Drive Time to Bridge: " + DRIVE_TIME_TO_BRIDGE);
        RRLogger.logInfo(this.getClass(),"robotInit()"," ");

        RRLogger.logInfo(this.getClass(),"robotInit()","Robot Ready");
    }

    public void disabledInit() {
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;

        time_last_update = Timer.getFPGATimestamp();
        azimuth_lock = false;  //  azimuth (left/right) target lock acquired
        elevation_lock = false; // elevation (up/down) target lock acquired
        muzzle_velocity_lock = false;
        angle_position = 0.0;

        dont_track_azimuth = false;

        // ****************
//        shooter.reset();
        shooterTrackerThread.resetShooter();

        driveThread.disableDrive();
//        armThread.disableArm();
//        gathererThread.disableGatherer();
        shooterTrackerThread.disable();
    }

    public void autonomousInit() {
        RRLogger.logDebug(this.getClass(),"autonomousInit()","");

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        // Get the time that the autonomous mode starts
        autonomousStartTime = Timer.getFPGATimestamp();

        isFinishedShooting = false;
        isShooting = false;
        delay_between_balls = false;
        delay_after_two_balls = false;
        shot_first_ball = false;
        shot_second_ball = false;
        driving_to_bridge = false;
        shooter_motor_running = false;

        autonomous_mode = true;
        autonomous_complete = false;

        dont_track_azimuth = true;                 // true;        // Only make this true if robot fails to lock during autonomous.

        autonomous_tracking_failed = false;
        no_balls_shot = true;
        second_ball_started_shoot = false;
        autonomous_mode_tracking = false;       // Don't start tracking until expanded

        for (int i = 0; i < NUMBER_OF_PREVIOUS; i++) // initialize Moving Average values
        {
            previous_angles[i] = 90.0;
        }
        current_angle_sum = 90.0 * NUMBER_OF_PREVIOUS;  // initialize MA sum

        // ****************
//        shooter.reset();
        shooterTrackerThread.resetShooter();

        autonomous.auton_init();

        driveThread.disableDrive();
//        armThread.disableArm();
//        gathererThread.disableGatherer();
        shooterTrackerThread.disable();
    }

    public void teleopInit() {

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;
        tankDrive = false;
        autonomous_mode = false;
        autonomous_mode_tracking = false;


        driveThread.enableDrive();
//        armThread.enableArm();
//        gathererThread.enableGatherer();
        shooterTrackerThread.enable();

        // Need to fix this!!

//        gatherer.stop();

        isFinishedShooting = false;
//        isShooting = false;

        dont_track_azimuth = false;

        // ****************
//        shooter.reset();
        shooterTrackerThread.resetShooterAfterAuton();

        // shooter_motor_running = false;

        /* Drive station code */
        //m_ds = DriverStation.getInstance();
        //m_dsLCD = DriverStationLCD.getInstance();
        RRLogger.logDebug(this.getClass(),"teleopInit()","Initialization Complete!");


    }

    /**
     * This function is called periodically during autonomous
     *
     * Notes:
     *
     *
     *
     */
    public void autonomousPeriodic() {
//        tracker.trackTarget(RoboRebels.AUTO_TARGET);

        autonomous.auton_periodic();

        //RRLogger.logDebug(this.getClass(),"autonomousPeriodic()",getAngle());
    }

    /**
     * This function is called periodically during operator control
     *
     * ---------------------
     * This is the most important method in this class
     * ---------------------
     */
    public void teleopPeriodic() {


        if (teleopStateBroadcasted == true) {
            RRLogger.logDebug(this.getClass(),"teleopPeriodic()", "Teleop State" );
            teleopStateBroadcasted = false;
        }

        //***************
//        if ( tankDrive == true ) {
//            drive.drive(true);
//            //RRLogger.logDebug(this.getClass(),"teleopPeriodic()","Tank Drive");
//        }
//        else{
//            drive.drive(false);
//            //RRLogger.logDebug(this.getClass(),"teleopPeriodic()","Arcade Drive");
//        }
        //***************

        // ****************
//      tracker.trackTarget(RoboRebels.AUTO_TARGET);   
//      shooter.shoot();

        //******************

        if (!shooter.tracking) {
            gatherer.gather();
            // ******************
            arm.arm();
            // ******************
            //slider.slide();
        }
        // ******************

//      RRLogger.logDebug(this.getClass(),"teleopPeriodic()","Gyro: " + gyro.getAngle());
//      RRLogger.logDebug(this.getClass(),"teleopPeriodic()","RE: " + rightEncoder.get() + " | LE: " + leftEncoder.get());
    }

    /**
     * This function is called periodically during the disabled state
     *
     * What it needs to do:
     *
     *
     */
    public void disabledPeriodic() {
        //nothing right now
    }

    /**
     * The VM will try to call this function as often as possible during the autonomous state
     *
     */
    public void autonomousContinuous() {
        //nothing right now
    }

    /**
     * The VM will try to call this function as often as possible during the teleop state
     *
     */
    public void teleopContinuous() {
    }

    /**
     * The VM will try to call this function as often as possible during the disbabled state
     */
    public void disabledContinuous() {
    }

    /*
     * This method checks buttons and sets states accordingly
     * 
     * NOTE:  Input checking should be put into their respective classes.  For 
     * reference, see RRShooter.
     */
    public void checkButtons() {
        //RRLogger.logDebug(this.getClass(),"checkButtons()", "checkButtons()" );
        /*
        if (m_rightStick.getZ() <= 0)
        {    // Logitech Attack3 has z-polarity reversed; up is negative
        // arcade mode
        tankDrive = false;
        }
        else
        {
        // tank drive
        tankDrive = true;
        }
         * */
        /*
        if (m_leftStick.getRawButton(1)) {
        launcher.set(launcher_speed);
        if (!launcher_button_pressed)  // if the shooter button is pressed then this adds .2 to its speed

        {
        launcher_speed += -0.2;
        launcher_button_pressed = true;
        if (launcher_speed < -1.0)  // once it gets past speed of -1
        {
        launcher_speed  = 0.0; // it turns itself off
        }

        RRLogger.logDebug(this.getClass(),"checkButtons()","Increasing launcher_speed to "+ launcher_speed);
        }
        }
        else
        launcher_button_pressed = false;
        // else {
        //      launcher.set(0);
        //      RRLogger.logDebug(this.getClass(),"checkButtons()","Launch cim off");
        //  }
        
        if(m_leftStick.getRawButton(3)) { //when button 3 is presssssssed the up/down aiming increases
        elevation.set(.3);
        RRLogger.logDebug(this.getClass(),"checkButtons()","elevation increase");
        }
        else if (m_leftStick.getRawButton(2)) { //when button 2 is pressed the up/down aiming decreases
        elevation.set(-.3);
        RRLogger.logDebug(this.getClass(),"checkButtons()","elevation decrease");
        }
        else {
        elevation.set(0);
        RRLogger.logDebug(this.getClass(),"checkButtons()","elevation standstill");//otherwise it stops moving
        }
        

        if (m_leftStick.getRawButton(4)) { //when button 4, move susan left
        lazySusan.set(-.3);
        RRLogger.logDebug(this.getClass(),"checkButtons()","Lazysusan Left");
        }
        else if (m_leftStick.getRawButton(5)) {// when but 5, move suzie right
        lazySusan.set(.3);
        RRLogger.logDebug(this.getClass(),"checkButtons()","Lazysusan Right");
        }
        else { //otherwise dont do anything
        lazySusan.set(0);
        RRLogger.logDebug(this.getClass(),"checkButtons()","Lazysusan STOPPPPP!!!!");
        }

        if (m_leftStick.getRawButton(6)) { //if 6, suck ball in
        loader.set(-.75);
        RRLogger.logDebug(this.getClass(),"checkButtons()","loader up =D");
        }
        else if (m_leftStick.getRawButton(7)) { //if 7, drop (or de-suck) ball
        loader.set(.75);
        RRLogger.logDebug(this.getClass(),"checkButtons()","loader down :?");
        }
        else {
        loader.set(0); //otherwise, dont move(aka stoop) at all
        RRLogger.logDebug(this.getClass(),"checkButtons()","loader STOOP");
        }
         *
         */

        /*
        if (m_leftStick.getRawButton(6) && btnPressed == false) {
        btnPressed = true;
        pwmTest++;

        if (currentPWM != null)
        currentPWM.setRaw(0);

        if (pwmTest == 8)
        pwmTest = 1;

        RRLogger.logDebug(this.getClass(),"checkButtons()","PWM #" + pwmTest);

        currentPWM = new PWM(pwmTest);
        currentPWM.setRaw(128);
        RRLogger.logDebug(this.getClass(),"checkButtons()","Pwm Test done with channel");
        }

        if (!m_leftStick.getRawButton(6) && btnPressed == true) {
        btnPressed = false;
        }
         *
         */
        /*
        RRLogger.logDebug(this.getClass(),"checkButtons()", "LX: " + m_xboxStick.getRawAxis(1));
        System.out.flush();
        RRLogger.logDebug(this.getClass(),"checkButtons()", "LY: " + m_xboxStick.getRawAxis(2));
        System.out.flush();
        RRLogger.logDebug(this.getClass(),"checkButtons()", "RX: " + m_xboxStick.getRawAxis(4));
        System.out.flush();
        RRLogger.logDebug(this.getClass(),"checkButtons()", "RY: " + m_xboxStick.getRawAxis(5));
        System.out.flush();
         */
    }

    public static void printLCD(int lineNum, String value) {
        DriverStationLCD.Line line = null;
        switch (lineNum) {
            case 2:
                line = DriverStationLCD.Line.kUser2;
                break;
            case 3:
                line = DriverStationLCD.Line.kUser3;
                break;
            case 4:
                line = DriverStationLCD.Line.kUser4;
                break;
            case 5:
                line = DriverStationLCD.Line.kUser5;
                break;
            case 6:
                line = DriverStationLCD.Line.kMain6;
                break;
        }
        m_dsLCD.println(line, 1, value);
        m_dsLCD.updateLCD();
    }
    /*
    public double getAngle() {
    ADXL345_I2C.AllAxes axes = accel.getAccelerations();
    RRLogger.logDebug(this.getClass(),"getAngle()","X Accel: " + axes.XAxis);
    RRLogger.logDebug(this.getClass(),"getAngle()","Y Accel: " + axes.YAxis);
    RRLogger.logDebug(this.getClass(),"getAngle()","Z Accel: " + axes.ZAxis);
    double yAxis = Math.min(1, axes.YAxis);
    yAxis = Math.max(-1, yAxis);
    return 180.0 * MathUtils.asin(yAxis) / 3.14159;
    }
     *
     */
}
