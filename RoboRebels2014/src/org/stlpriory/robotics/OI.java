package org.stlpriory.robotics;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.stlpriory.robotics.commands.claw.InvertPiston;
import org.stlpriory.robotics.commands.claw.InvertWheelPiston;
import org.stlpriory.robotics.commands.claw.ShootForLowGoal;
import org.stlpriory.robotics.commands.claw.StartClawWheels;
import org.stlpriory.robotics.commands.claw.StopClawWheels;
import org.stlpriory.robotics.commands.drivetrain.Shift;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.commands.launcher.*;
import org.stlpriory.robotics.misc.Keymap;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands and command groups
 * that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
    /*
     * The bottons on the XBox controller follow this mapping
     * 1:  A
     * 2:  B
     * 3:  X
     * 4:  Y
     * 5:  Left Bumper
     * 6:  Right Bumper
     * 7:  Back
     * 8:  Start
     * 9:  Left thumbstick
     * 10: Right thumbstick
     *
     * The axis on the controller follow this mapping
     * (all output is between -1 to 1)
     * 1:  Left stick X axis  (left:negative, right:positve)
     * 2:  Left stick Y axis  (up:negative, down:positive)
     * 3:  Triggers           (left:positive, right:negative)
     * 4:  Right stick X axis (left:negative, right:positive)
     * 5:  Right stick Y axis (up:negative, down:positive)
     * 6:  Directional pad
     */
    private static OI instance = null;
    private Joystick xboxControler;
    private Joystick joystickControler;
    private JoystickButton shiftButton;
    private JoystickButton expandRetractClawButton;
    private JoystickButton clawButton;
    private JoystickButton clawWheelButton;
    private JoystickButton clawWheelButton2;
    private JoystickButton lowGoalShootButton;
    private JoystickButton launchButton;
    private JoystickButton resetButton;

//    private JoystickButton toggleModeStateButton;
//    private JoystickButton sensorButton;
//    private JoystickButton retractButton;
    public OI() {
        Debug.println("[OI] Instantiating ...");

        Debug.println("[OI] Initializing gamepad to Drivers station USB port " + RobotMap.DRIVER_STATION_USB_PORT1);
        xboxControler = new Joystick(1);
        joystickControler = new Joystick(2);

// COMMENTED OUT 3/7/2014
//        Debug.println("[OI] Initializing gamepad for non button triggers");
//        gamepad = new Gamepad();

        Debug.println("[OI] Initializing gamepad to launch ball a disc when the right bumper pressed");
        launchButton = new JoystickButton(xboxControler, Keymap.LAUNCH_BUTTON_KEY_MAP);
        launchButton.whenPressed(new Launch());
        launchButton.whenReleased(new Retract());
//
//        Debug.println("[OI] Initializing gamepad to reset launcher gearbox piston when the left bumper pressed");
//        retractButton = new JoystickButton(joystick,Keymap.RETRACT_BUTTON_KEY_MAP);
//        retractButton.whenPressed(new Retract());
//        
        Debug.println("[OI] Initializing gamepad to load launcher when the X button is pressed");
        resetButton = new JoystickButton(xboxControler, Keymap.RESET_BUTTON_KEY_MAP);
//        resetButton.whenPressed(new RetractPunter());
        resetButton.whenPressed(new Reset());
        resetButton.whenReleased(new Stop());

        Debug.println("[OI] Initializing joystick to shift gears for the drivetrain wheels");
        shiftButton = new JoystickButton(joystickControler, Keymap.SHIFT_JOYSTICK_BUTTON_KEY_MAP);
        shiftButton.whenPressed(new Shift());

        Debug.println("[OI] Initializing gamepad to tilt the entiire claw assembly");
        expandRetractClawButton = new JoystickButton(xboxControler, Keymap.CLAW_ASSEMBLY_TILT_BUTTON_KEY_MAP);
        expandRetractClawButton.whenPressed(new InvertPiston());

        Debug.println("[OI] Initializing gamepad to start/stop the claw wheels");
        clawButton = new JoystickButton(xboxControler, Keymap.CLAW_START_STOP_WHEELS_BUTTON_KEY_MAP);
        clawButton.whenPressed(new StartClawWheels());
        clawButton.whenReleased(new StopClawWheels());

//        Debug.println("[OI] Initializing gamepad read distance from the arduino");
//        sensorButton = new JoystickButton(joystick,Keymap.SENSOR_DISTANCE_BUTTON);
//        sensorButton.whenPressed(new ReadDistance());

        Debug.println("[OI] Initializing gamepad to tilt the claw wheels");
        clawWheelButton = new JoystickButton(xboxControler, Keymap.CLAW_WHEEL_TILT_BUTTON_KEY_MAP);
        clawWheelButton.whenPressed(new InvertWheelPiston());

        clawWheelButton2 = new JoystickButton(xboxControler, Keymap.CLAW_WHEEL_TILT_BUTTON_KEY_MAP_2);
        clawWheelButton2.whenPressed(new InvertWheelPiston());

        Debug.println("[OI] Initializing gamepad to shoot for low goal");
        lowGoalShootButton = new JoystickButton(xboxControler, Keymap.CLAW_LOW_GOAL_SHOOT_BUTTON_KEY_MAP);
        lowGoalShootButton.whenPressed(new ShootForLowGoal());
        lowGoalShootButton.whenReleased(new StopClawWheels());

//        Debug.println("[OI] Initializing gamepad to toggle mode(Automatic or Manual)");
//        toggleModeStateButton = new JoystickButton(joystick,Keymap.MODE_TOGGLE_STATE_BUTTON_KEY_MAP);
//        toggleModeStateButton.whenPressed(new ToggleMode());



        // SmartDashboard Buttons
//        SmartDashboard.putData("Autonomous Command", new Auton1());
//        SmartDashboard.putData("DriveInASquare", new DriveInASquare());
//        SmartDashboard.putData("DriveWithJoystick", new DriveWithJoystick());
//        SmartDashboard.putData("DriveWithGamepad", new DriveWithGamepad());
//        SmartDashboard.putData("LoadDisc", new LoadDisc());
//        SmartDashboard.putData("ShootDisc", new ShootDisc());

        // associate the DriveInSquare command group with the
        // trigger button on the right joystick. Whenever the
        // joystick is pressed, the robot will drive
        // in a square pattern. When the command is completed,
        // the default command for the DriveLine subsystem will
        // run - DriveViaJoysticks
        //       this.trigger.whenPressed(new DriveInASquare());

// COMMENTED OUT 3/7/2014
//        Debug.println("[OI] Inilizing Gamepad to shift gears when right trigger pulled");
//        gamepad.whenRightTriggerHeld(new Shift());

        Debug.println("[OI] Instantiation complete.");
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    public Joystick getXboxControler() {
        return this.xboxControler;
    }

    public Joystick getJoystickControler() {
        return this.joystickControler;
    }
}
