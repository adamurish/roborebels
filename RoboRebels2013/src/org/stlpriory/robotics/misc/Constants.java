/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.misc;

import edu.wpi.first.wpilibj.CANJaguar;

/**
 *
 */
public class Constants {

    // Threshold below which joystick inputs will be ignored
    public static final double JOYSTICK_THRESHOLD = 0.2;

    /**
     * The servo angle, in degrees, used to load frisbee discs.
     *
     * Assume that the servo angle is linear with respect to the PWM value (big assumption, need to test).
     *
     * Servo angles that are out of the supported range of the servo simply "saturate" in that direction
     * In other words, if the servo has a range of (X degrees to Y degrees) than angles of less than X
     * result in an angle of X being set and angles of more than Y degrees result in an angle of Y being set.
     */

    //-----------------------------------------------------------------------
    //     Constants for the Shooter subsystem
    //-----------------------------------------------------------------------
    public static final double MIN_LOADER_SERVO_ANGLE = -45.0;
    public static final double MAX_LOADER_SERVO_ANGLE = 65.0;

    public static final double SHOOTER_WHEEL_MOTOR_SPEED = 0.5;
    public static final double LOADER_MOTOR_SPEED = 0.5;

    //-----------------------------------------------------------------------
    //     Constants for the CANDriveTrain subsystem
    //-----------------------------------------------------------------------

    /*
     * A PID Controller implements the PID control algorithm. The PID control is a useful
     * form of control loop in robotics. It simplifies control of a mechanism by creating
     * a system that you can tell to go to some setpoint and it takes care automatically
     * moving to that setpoint and maintaining that setpoint as long as the PID loop is tuned.
     * There are 4 important constants that influence the behavior of the PID Controller, by
     * setting any of these to 0, you can ignore that coefficient and use a simpler controller
     * if it meets your needs. The 4 constants are:
     *
     * The Proportional Term
     * The proportional control constant of the PID loop is often referred to as P or kP. It
     * is the simplest parameter to use and in many cases when using PID you really only need
     * the proportional constant. The proportional term is very simple, you know where you are
     * and where you want to be (the setpoint), so you just take the difference and multiply
     * by kP which acts as a scaling factor.
     *
     * The Integral Term
     * The integral control constant of the PID loop is often referred to as I or kI. It is useful
     * for overcoming unexpected resistance that would stop a simple loop using only the proportional
     * constant. The integral term represents the area under the curve of the error over time. You
     * know where you are and where you want to be (the setpoint), so you just add this up over time
     * and multiply by kI which acts scaling factor.
     *
     * The Derivative Term
     * The derivative control constant of the PID loop is often referred to is D or kD. It is useful
     * for preventing oscillation that otherwise occurs in a control loop. Derivative is the rate of
     * change of the error over time. You know where you are and where you want to be (the setpoint),
     * and you know this from the past time, so you take the difference divide by the time difference
     * and as always multiply by a scaling factor, in this case kD.
     */

    // Codes per revolution generated by the encoder
    // (US Digital E4P-250-250-N-S-D-D-B encoder)
    public static final int ENCODER_CODES_PER_REV = 250;
    // The number of turns on the potentiometer
    public static final int ENCODER_POTENTIOMETER_TURNS = 0;
    // The maximum voltage that the Jaguar will ever output
    public static final double JAGUAR_MAX_OUTPUT_VOLTAGE = 12;
    // The maximum voltage change rate
    public static final double JAGUAR_VOLTAGE_RAMP_RATE = 20;

    // The proportional gain of the Jaguar's PID controller.
    public static final double KP = 0.5;
    // The integral gain of the Jaguar's PID controller.
    public static final double KI = 0; //0.005;
    // The differential gain of the Jaguar's PID controller.
    public static final double KD = 0; //0.25;

    // The Jaguar configuration properties
    public static final CANJaguar.NeutralMode JAGUAR_NEUTRAL_MODE = CANJaguar.NeutralMode.kJumper;
    public static final CANJaguar.ControlMode JAGUAR_CONTROL_MODE = CANJaguar.ControlMode.kSpeed;
    public static final CANJaguar.SpeedReference JAGUAR_SPEED_REFERNCE = CANJaguar.SpeedReference.kQuadEncoder;

    // RobotDrive configuration properties
    // The DRIVE_MAX_OUTPUT is multiplied with the output percentage
    // computed by the drive functions.  It signifies the speed that
    // you want joystick full forward to mean
    public static final double DRIVE_MAX_OUTPUT = 100;

    // Reference URLs:
    // http://www.chiefdelphi.com/forums/showthread.php?t=105641
    // http://team2168.org/index.php/resources/electrical/210-can-jaguars
    // http://wpilib.screenstepslive.com/s/3120/m/7882/l/79335-writing-the-code-for-a-pidsubystem-in-java
    // http://www.youtube.com/watch?v=1WLk1_Ye_U8
    // http://www.chiefdelphi.com/forums/showthread.php?t=110730
    // http://www.chiefdelphi.com/forums/showthread.php?t=90508
    // http://www.chiefdelphi.com/forums/showthread.php?t=104757
    // http://www.youtube.com/watch?v=j2Xz8bRRcF0
    // PID subsystems
    // http://www.chiefdelphi.com/forums/showthread.php?t=105810
}
