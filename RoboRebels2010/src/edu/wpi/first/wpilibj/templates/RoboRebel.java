/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
// This is a chance



//<<<<<<< .mine
/*
 * This is Derek.
 * All your base are belong to iPhone.
 */
//=======
/*
 *This is Jac.
 */
//>>>>>>> .r14

/**
 * Objects that would be important to develop:
 *
 *   - Kicker object
 *     * Will most likely use the following objects:
 *
 *   - Pulley object
 *     * Will most likely use the following objects:
 */

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebel extends IterativeRobot {

    // Declare variable for the robot drive system
    RobotDrive m_robotDrive;	 // robot will use PWM 1-4 for drive motors

    // Declare a variable to use to access the driver station object
    DriverStation m_ds;                     // driver station object
    int m_priorPacketNumber;                // keep track of the most recent packet number from the DS
    int m_dsPacketsReceivedInCurrentSecond;	// keep track of the ds packets received in the current second

    // Declare variables for the two joysticks being used
    Joystick m_rightStick;	 // joystick 1 (arcade stick or right tank stick)
    Joystick m_leftStick;	 // joystick 2 (tank left stick)

    static final int NUM_JOYSTICK_BUTTONS = 16;
    boolean[] m_rightStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS+1)];
    boolean[] m_leftStickButtonState = new boolean[(NUM_JOYSTICK_BUTTONS+1)];

    /**
     * Constructor
     */
    public void RoboRebel()
    {
        /**
         * Set up the following:
         *
         *   - Create a new RobotDrive object specifying 4 motors
         *   - Grab an instance of the DriveStation object
         *   - Create Joystick objects which map to the approprite modules
         *   - Set up Joystick button map array
         */
        
        System.out.println("RoboRebel Constructor Started\n");

        // Create a robot using standard right/left robot drive on PWMS 1, 2, 3, and #4
        m_robotDrive = new RobotDrive(1, 3, 2, 4);

                        // Define joysticks being used at USB port #1 and USB port #2 on the Drivers Station
        m_rightStick = new Joystick(1);
        m_leftStick = new Joystick(2);

                        // Iterate over all the buttons on each joystick, setting state to false for each


        System.out.println("RoboRebel Constructor Completed\n");
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {

    }

    public void disabledInit()
    {
        
    }

    public void autonomousInit()
    {
        
    }

    public void teleopInit()
    {

    }

    /**
     * This function is called periodically during autonomous
     *
     * Notes:
     *
     *   - We can use the RobotDrive.drive() method to drive the robot programatically
     *   - Use the camera to for guidance (???)
     *   - Use gyro (???)
     *   - Use kicker object
     *
     * What it needs to do:
     *
     *   - Feed the Watchdog
     *   -
     */
    public void autonomousPeriodic()
    {

    }

    /**
     * This function is called periodically during operator control
     *
     * What it needs to do:
     *
     *   - Feed the Watchdog
     *   - Scan Joystick for button values, then pack the
     */
    public void teleopPeriodic()
    {

        // feed the user watchdog at every period when in autonomous
        Watchdog.getInstance().feed();

        /*
         * No longer needed since periodic loops are now synchronized with incoming packets.
        if (m_ds->GetPacketNumber() != m_priorPacketNumber) {
        */
        /*
         * Code placed in here will be called only when a new packet of information
         * has been received by the Driver Station.  Any code which needs new information
         * from the DS should go in here
         */

        // use tank drive
        m_robotDrive.tankDrive(m_leftStick, m_rightStick);	// drive with tank style


        /*
        }  // if (m_ds->GetPacketNumber()...
        */

        
    }

    /**
     * This function is called periodically during the disabled state
     *
     * What it needs to do:
     *
     *   - Feed the Watchdog
     */
    public void disabledPeriodic()
    {
     
    }

    /**
     * The VM will try to call this function as often as possible during the autonomous state
     *
     */
    public void autonomousContinuous()
    {
        
    }

    /**
     * The VM will try to call this function as often as possible during the teleop state
     *
     */
    public void teleopContinuous()
    {

    }

    /**
     * The VM will try to call this function as often as possible during the disbabled state
     */
    public void disabledContinuous()
    {
        
    }
    
}