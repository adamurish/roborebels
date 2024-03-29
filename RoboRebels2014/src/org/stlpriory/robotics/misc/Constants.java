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
    // The increment added to the commanded motor speeds
    // in order to gradually ramp up the speed
    public static final double MOTOR_RAMP_INCREMENT = 0.05;
    public static final double MOTOR_RAMP_DOWN_INCREMENT = .1;

    //public static final double DRIVE_MAX_OUTPUT = 300;  // speed control mode (rpm value)
    //public static final double DRIVE_MAX_OUTPUT = 400;  // speed control mode (rpm value)
    public static final double DRIVE_MAX_OUTPUT = 100;  // voltage control mode (

    // The proportional gain of the Jaguar's PID controller.
    public static final double KP = 0.41;
    // The integral gain of the Jaguar's PID controller.
    public static final double KI = 0.01;
    // The differential gain of the Jaguar's PID controller.
    public static final double KD = 0.0;

    // The Jaguar configuration properties
    public static final CANJaguar.NeutralMode JAGUAR_NEUTRAL_MODE = CANJaguar.NeutralMode.kBrake;
    public static final CANJaguar.ControlMode JAGUAR_CONTROL_MODE = CANJaguar.ControlMode.kPercentVbus;
    public static final CANJaguar.SpeedReference JAGUAR_SPEED_REFERENCE = CANJaguar.SpeedReference.kQuadEncoder;

    //-----------------------------------------------------------------------
    //     Constants for the Shooter subsystem
    //-----------------------------------------------------------------------

    // Define the timeouts for the loadDisc and resetLoader actions
    public static final double LOAD_DISC_TIMEOUT_IN_SECS    = 1;
    public static final double RESET_LOADER_TIMEOUT_IN_SECS = 1;
    public static final double RUMBLER_TIMEOUT_IN_SECS      = 0.3;

    // Shooter wheel motor speed (must be within the range of -1 to 1)
    public static final double SHOOTER_WHEEL_MOTOR_SPEED    = 1.0;
    // Loader arm motor speed (must be within the range of -1 to 1)
    public static final double LOADER_MOTOR_SPEED           = 1.0;
    // For the shooter wheel encoder with PIDSourceParameter.kRate the
    // PIDController setpoint value should be in units of RPM
    public static final double SHOOTER_WHEEL_MOTER_RATE     = 1.0;//-20000;
    
    public static final int ROBOT_MANUAL_MODE = 0;
    public static final int ROBOT_AUTOMATIC_MODE = 1;
    public static final int ROBOT_DEFAULT_MODE = 0;

}
