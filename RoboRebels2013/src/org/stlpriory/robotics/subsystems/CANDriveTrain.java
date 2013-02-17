/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.commands.drivetrain.DriveWithGamepad;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Utils;

/**
 *
 */
public class CANDriveTrain extends Subsystem {

    private RobotDrive drive;
    private static CANJaguar leftFrontJag;
    private static CANJaguar rightFrontJag;
    private static CANJaguar leftRearJag;
    private static CANJaguar rightRearJag;
    private static double direction = 1;

    public CANDriveTrain() {
        super("CANDriveTrain");
        Debug.println("[CANDriveTrain] Instantiating...");

        // CAN Jaguar configuration properties
        CANJaguar.ControlMode controlMode = CANJaguar.ControlMode.kSpeed;
        CANJaguar.NeutralMode neutralMode = CANJaguar.NeutralMode.kBrake;
        CANJaguar.SpeedReference speedReference = CANJaguar.SpeedReference.kQuadEncoder;

        // The proportional gain of the Jaguar's PID controller.
        double pValue = 30;
        // The integral gain of the Jaguar's PID controller.
        double iValue = 0.005;
        // The differential gain of the Jaguar's PID controller.
        double dValue = 0.25;

        try {
            Debug.println("[CANDriveTrain] Initializing left front CANJaguar to CAN bus address "
                    + RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftFrontJag = createCANJaguar(RobotMap.LEFT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing left rear CANJaguar to CAN bus address "
                    + RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            leftRearJag = createCANJaguar(RobotMap.LEFT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing right front CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightFrontJag = createCANJaguar(RobotMap.RIGHT_FRONT_DRIVE_MOTOR_CAN_BUS_ADDRESS);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        try {
            Debug.println("[CANDriveTrain] Initializing right rear CANJaguar to CAN bus address "
                    + RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
            rightRearJag = createCANJaguar(RobotMap.RIGHT_REAR_DRIVE_MOTOR_CAN_BUS_ADDRESS);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

        Debug.println("[CANDriveTrain] Initializing RobotDrive");
        drive = new RobotDrive(leftFrontJag, leftRearJag, rightFrontJag, rightRearJag);
        drive.setSafetyEnabled(false);
        drive.setExpiration(0.1);
        drive.setSensitivity(0.5);
        drive.setMaxOutput(1.0);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        Debug.println("[CANDriveTrain] Instantiation complete.");
    }

    private CANJaguar createCANJaguar(int busAddress) throws CANTimeoutException {
        // CAN Jaguar configuration properties
        CANJaguar.ControlMode controlMode = CANJaguar.ControlMode.kSpeed;
        CANJaguar.NeutralMode neutralMode = CANJaguar.NeutralMode.kBrake;
        CANJaguar.SpeedReference speedReference = CANJaguar.SpeedReference.kQuadEncoder;

        // Codes per revolution generated by the encoder
        // (US Digital E4P-250-250-N-S-D-D-B encoder)
        int codesPerRev = 250;
        // The maximum voltage that the Jaguar will ever output
        double maxOutputVoltage = 12;
        // The maximum voltage change rate
        double voltageRampRate = 1;

        // The proportional gain of the Jaguar's PID controller.
        double pValue = 1;//30;
        // The integral gain of the Jaguar's PID controller.
        double iValue = 0.005;  //0.004
        // The differential gain of the Jaguar's PID controller.
        double dValue = 0.25;  //0.0

        // http://www.chiefdelphi.com/forums/showthread.php?t=105641

        CANJaguar jaguar = new CANJaguar(busAddress);
        jaguar.changeControlMode(controlMode);
        jaguar.setSpeedReference(speedReference);
        //jaguar.configNeutralMode(neutralMode);
        jaguar.configEncoderCodesPerRev(codesPerRev);
        //jaguar.configMaxOutputVoltage(maxOutputVoltage);
        //jaguar.setVoltageRampRate(voltageRampRate);
        jaguar.setPID(pValue, iValue, dValue);
        jaguar.enableControl();

        Debug.println("[CANDriveTrain] CANJaguar configuration properties: ");
        Debug.println("                Bus address      = "+jaguar.getDescription());
        Debug.println("                ControlMode      = "+toString(jaguar.getControlMode()));
        Debug.println("                SpeedReference   = "+toString(jaguar.getSpeedReference()));
        Debug.println("                NeutralModel     = "+toString(neutralMode));
        Debug.println("                PID values       = "+jaguar.getP() + ", " + jaguar.getI() + ", " + jaguar.getD());
        Debug.println("                codesPerRev      = "+codesPerRev);
        Debug.println("                maxOutputVoltage = "+maxOutputVoltage);
        Debug.println("                firmware version = "+jaguar.getFirmwareVersion());
        Debug.println("                hardware version = "+jaguar.getHardwareVersion());

        return jaguar;
    }

    /**
     * Initialize and set default command
     */
    public void initDefaultCommand() {
        Debug.println("[CANDriveTrain.initDefaultCommand()] Setting default command to " + DriveWithGamepad.class.getName());
        setDefaultCommand(new DriveWithGamepad());
    }

    public void setForwards() {
        direction = 1;
    }

    public void setBackwards() {
        direction = -1;
    }

    public void stop() {
        Debug.println("[CANDriveTrain.stop]");
        drive.tankDrive(0.0, 0.0);
    }

    public boolean canDrive() {
        return true;
    }

    public void tankDrive(double leftValue, double rightValue) {
        leftValue *= direction;
        rightValue *= direction;
        if (canDrive()) {
            drive.tankDrive(leftValue, rightValue);
        }
    }

    public void arcadeDrive(double moveValue, double rotateValue) {
        moveValue *= direction;
        rotateValue *= direction;
        if (canDrive()) {
            drive.arcadeDrive(moveValue, rotateValue);
        }
    }

    /**
     * Drive method for Mecanum wheeled robots.
     */
    public void mecanumDrive(Joystick joystick) {
        /*
         * Three-axis joystick mecanum control.
         * Let x represent strafe left/right
         * Let y represent rev/fwd
         * Let z represent spin CCW/CW axes
         * where each varies from -1 to +1.
         * So:
         * y = -1 corresponds to full speed reverse,
         * y= +1 corresponds to full speed forward,
         * x= -1 corresponds to full speed strafe left,
         * x= +1 corresponds to full speed strafe right,
         * z= -1 corresponds to full speed spin CCW,
         * z= +1 corresponds to full speed spin CW
         *
         * Axis indexes:
         * 1 - LeftX
         * 2 - LeftY
         * 3 - Triggers (Each trigger = 0 to 1, axis value = right - left)
         * 4 - RightX
         * 5 - RightY
         * 6 - DPad Left/Right
         */

        double rawLeftX = joystick.getRawAxis(1);
        double rawLeftY = joystick.getRawAxis(2);
        double rawZ = joystick.getRawAxis(3);

        double scaledLeftX = Utils.scale(rawLeftX);
        double scaledLeftY = Utils.scale(rawLeftY);

        double right     = -scaledLeftX;
        double forward   =  scaledLeftY;
        double rotation  = -rawZ;
        double clockwise =  rawZ;

        drive.mecanumDrive_Cartesian(-right, forward, rotation, clockwise);

        //printJaguarOutputCurrent();
        printJaguarSpeed();
    }

    public void straight(double speed) {
        speed *= direction;
        if (canDrive()) {
            drive.mecanumDrive_Cartesian(0, speed, 0, 0);
        }
    }

    public void turnLeft() { // sets the motor speeds to start a left turn
        arcadeDrive(0.0, .3);
    }

    public void driveWithJoystick(Joystick joystick) {
        drive.arcadeDrive(joystick);
    }

    public void driveWithGamepad(Joystick joystick) {
        mecanumDrive(joystick);
    }

    private String toString(CANJaguar.ControlMode controlMode) {
        switch (controlMode.value) {
            case 0: return "kPercentVbus";
            case 1: return "kCurrent";
            case 2: return "kSpeed";
            case 3: return "kPosition";
            case 4: return "kVoltage";
            default: return "kPercentVbus";
        }
    }

    private String toString(CANJaguar.NeutralMode neutralMode) {
        switch (neutralMode.value) {
            case 0: return "kJumper";
            case 1: return "kBrake";
            case 2: return "kCoast";
            default: return "kCoast";
        }
    }

    private String toString(CANJaguar.SpeedReference speedReference) {
        switch (speedReference.value) {
            case 0: return "kEncoder";
            case 1: return "kInvEncoder";
            case 2: return "kQuadEncoder";
            case 3: return "kNone";
            default: return "kNone";
        }
    }

    private void printJaguarOutputCurrent() {
        try {
            System.out.println("Output current LF " + leftFrontJag.getOutputCurrent()
                    + ", LR " + leftRearJag.getOutputCurrent()
                    + ", RF " + rightFrontJag.getOutputCurrent()
                    + ", RR " + rightRearJag.getOutputCurrent());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarOutputVoltage() {
        try {
            System.out.println("Output voltage LF " + leftFrontJag.getOutputVoltage()
                    + ", LR " + leftRearJag.getOutputVoltage()
                    + ", RF " + rightFrontJag.getOutputVoltage()
                    + ", RR " + rightRearJag.getOutputVoltage());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    private void printJaguarSpeed() {
        try {
            System.out.println("Speed LF " + leftFrontJag.getSpeed()
                    + ", LR " + leftRearJag.getSpeed()
                    + ", RF " + rightFrontJag.getSpeed()
                    + ", RR " + rightRearJag.getSpeed());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

}
