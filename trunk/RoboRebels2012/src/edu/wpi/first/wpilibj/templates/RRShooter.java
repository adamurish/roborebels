/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * The shooter class has the following components:
 * 
 * - Shooting wheel
 * - Getting/setting shooting wheel speed
 * - Tilting mechanism
 * - Lazy Susan
 * 
 * Currently, all manual control has been developed for
 * controlling the various parts of the shooter module.  
 * 
 * TODO:
 * 
 * - Test!
 * - Implement accelerometer code
 * - Extend public methods for autonomous control
 * 
 * @author dmw
 */
public class RRShooter 
{
    private final double        MAX_SHOOTING_SPEED = 1.0;
    private final double        LS_SPEED = 0.4;
    private final double        TILT_SPEED = 0.4;
    
    private     int             swj_channel;        // Shooter Wheel Jaguar channel
    private     int             lsv_channel;        // Lazy Susan Victor channel
    private     int             tltv_channel;       // Tilter Victor channel
    private     int             tltls_channel;      // Tilter Limit Swith channel
    
    private     double          shootingWheelSpeed = 0.0;
    private     double          lazySusanSpeed = 0.0;
    private     double          tiltSpeed = 0.0;
    
    private     boolean         shootingWheelState;
    
    private     boolean         shootingButtonPressed;
            
    private     Jaguar          shootingWheelJaguar;
    private     Victor          tiltVictor;
    private     Victor          lsVictor;
    private     DigitalInput    tiltLimitSwitch;
    
    private     Joystick        shootingJoystick;
    
    private     RRTracker       tracker;
    
    
    /**
     * Sets up the speed controllers for the shooter
     * @param swjc Shooting Wheel Jaguar Channel
     * @param lsvc Lazy Susan Victor Channel
     * @param tltvc Tilter Victor Channel 
     * @param tltlsc Tilter Limit Switch Channel
     * @param js Joystick to monitor for button/axis events
     */
    public RRShooter(int  swjc, int lsvc, int tltvc, int tltlsc, Joystick js, RRTracker t)
    {
        swj_channel = swjc;
        lsv_channel = lsvc;
        tltv_channel = tltvc;
        tltls_channel = tltlsc;
        
        shootingWheelState = false;         // start with the shooting wheel off!
        
        shootingButtonPressed = false;      // indicates if the 
        
        if ( js != null )
            shootingJoystick = js;
        else
            throw new NullPointerException("RRShooter was passed a null Joystick object!");
        
        
        if ( t != null )
            tracker = t;
        else
            throw new NullPointerException("RRShooter was passed a null RRTracker object!");
        
        shootingWheelJaguar = new Jaguar(swj_channel);
        tiltVictor = new Victor(tltv_channel);
        lsVictor = new Victor(lsv_channel);
        tiltLimitSwitch = new DigitalInput(tltls_channel);
    }
    
    
    /**
     * 
     * @param distance distance is distance to basket in feet
     * @param targetID targetID indicates if lower (0), middle (1), or upper (2) is the target
     * @return Returns the angle for which to move the shooter at
     */
    
    static double determineAngle(double distance, int targetID)
    {            
        double muzzleVelocity = 7.1; //meters per second

        double gravity = 9.81;  //meters per (second)^2
        double yLower = .466953;
        double yMiddle = 1.30515;
        double yHigher = 2.24485;
        double xDistance = 3.6576; // distance to base of basket (as if shooting from key)
        double shooterHeight = .914; // meters off the ground
        double y; // define by asking driver "top, middle, or bottom?" (BELOW)

        xDistance = distance /3.28; // converts feet into meters

        

        if (targetID == 0)
        {
            y = yLower; 

        } 
        else if (targetID == 1)
        {
            y = yMiddle;
        }
        else if ( targetID == 2 )
        {
            y = yHigher;

        }
        else 
        {
            y = yMiddle;
        }
        
        /*
                double tempSqrtEquationHigher = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yHigher)-(gravity*gravity*xDistance));
                double tempSqrtEquationMiddle = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yMiddle)-(gravity*gravity*xDistance));
                double tempSqrtEquationLower = Math.sqrt((muzzleVelocity*muzzleVelocity)-(2*gravity*muzzleVelocity*muzzleVelocity*yLower)-(gravity*gravity*xDistance));
        */

        double tempSqrtEquation = Math.sqrt((muzzleVelocity*muzzleVelocity*muzzleVelocity*muzzleVelocity)-
                                            (2*gravity*muzzleVelocity*muzzleVelocity*y)-(gravity*gravity*xDistance*xDistance));
        System.out.println("tempSqrtEq: " + tempSqrtEquation);
        double theta = MathUtils.atan(((muzzleVelocity*muzzleVelocity)+(tempSqrtEquation))/(gravity*xDistance));

        theta = theta * ( 180 / 3.14159265); // converts radians to degreese

        return theta;
    }
    
    
    /**
     * Gathers input states and sets up the necessary motor speeds
     */
    private void gatherInputStates()
    {
        // Spin up if trigger is pressed (button 1)
        if ( shootingJoystick.getRawButton(RRButtonMap.SHOOT) && !shootingButtonPressed )
        {
            System.out.println("Trigger");
            if ( shootingWheelState )
            {
                shootingWheelSpeed = 0.0;
                shootingWheelState = false;
            }
            else
            {
                shootingWheelSpeed = this.getTransformedZValue();
                shootingWheelState = true;
            }
        }
        else if ( !shootingJoystick.getRawButton(RRButtonMap.SHOOT) )
        {
            shootingButtonPressed = false;
        }
        
        
        // Check for tilting button up, down (button 6, 7)
        if ( shootingJoystick.getRawButton(RRButtonMap.TILT_UP) )
        {
            System.out.println("Tilt up");
            
            tiltSpeed = -1.0 * TILT_SPEED;
            
        }
        else if ( shootingJoystick.getRawButton(RRButtonMap.TILT_DOWN) )
        {
            System.out.println("Tilt down");
            /*
            if ( !tiltLimitSwitch.get() )
            {
                tiltSpeed = TILT_SPEED;
            }
            else
            {
                System.out.println("Tilter limit switch pressed!");
                tiltSpeed = 0.0;
            }
            *
            * 
            */
            
            tiltSpeed = TILT_SPEED;
        }
        else if ( !shootingJoystick.getRawButton(RRButtonMap.TILT_UP) && !shootingJoystick.getRawButton(RRButtonMap.TILT_UP) )
        {
            tiltSpeed = 0.0;
        }
        
        
        // Check for lazy susan button left, right (button 4, 5)
        if ( shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_LEFT) )
        {
            System.out.println("Lazy susan left"); //TODO: should this be lazy susan left or right
            lazySusanSpeed = LS_SPEED;
        }
        else if ( shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_RIGHT) )
        {
            System.out.println("Lazy susan left"); //TODO: should this be lazy susan left or right
            lazySusanSpeed = -1.0 * LS_SPEED;
        }
        else if ( !shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_LEFT) && !shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_RIGHT) )
        {
            lazySusanSpeed = 0.0;
        }
        
        
    }
    
   
    
    /**
     * This is the general shoot periodic method for shooting functions.
     */
    
    public void shoot()
    {
        // Process input from joystick and other inputs
        gatherInputStates();
        
        // Process shooter states
        setShooterSpeeds();
        
        // track target
        tracker.trackTarget();
    }
    
    
    /**
     * This private method sets the determined speeds for the various
     * mechanisms used by the shooter module.  
     */
    
    private void setShooterSpeeds()
    {
        shootingWheelJaguar.set(-1.0 * shootingWheelSpeed);
        lsVictor.set(lazySusanSpeed);
        System.out.println("s: " + tiltSpeed);
        tiltVictor.set(tiltSpeed);
    }
    
    /**
     * This method gets the value of the Z dial on a joystick and
     * transforms it to fit within the speed range of the shooter.
     * 
     * @return transformed speed from the corresponding Z value
     */
    
    private double getTransformedZValue()
    {
        return MAX_SHOOTING_SPEED * (shootingJoystick.getZ() + 1.0) / 2.0;
    }
}