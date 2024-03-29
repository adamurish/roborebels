/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The gatherer class has the following components:
 * 
 * - The spinner
 * - The ball storage system
 * - The ball sensor system
 * 
 * 
 * Joystick Buttons/axis	Action	        This button layout is if the auto ball sensing is not functional

 *
 *
 * TODO:
 * 
 * - TEST!
 * - Implement ball sensor system
 * - Implement autonomous extensions
 * 
 * @author dmw
 */
public class RRGatherer {

    private final double SPINNER_MAX_SPEED = 0.5;
    private final double CONVEYER_MAX_SPEED = 0.4;  // 0.5;
    private int spinner_channel;
    private int ball_conveyer_channel;
    private int bottom_ball_sensor_channel,
            middle_ball_sensor_channel,
            top_ball_sensor_channel;
    /*   ------- experimental ------ */
    private int elevatorActionDuration = 0;
    private int DECELERATION_CURVE_MAX = 20;
    private int elevatorDecelerationDirection = 0;
    /*   ------- experimental ------ */
    private double spinnerSpeed = 0.0;
    private double conveyerSpeed = 0.0;
    private int spinnerState = 0;           // 0 = off, 1 = up, 2 = down
    private boolean spinnerButtonPressed = false;
    private boolean autoElevateUp = false;
    private boolean autoElevateDown = false;
    private Joystick js;
    private Victor spinnerVictor;
    private Victor ballConveyerVictor;
    private DigitalInput bbsDigitalInput,
            mbsDigitalInput,
            tbsDigitalInput;

    /**
     * 
     * @param sc Spinner channel
     * @param bcc Ball conveyer channel
     * @param bbsc Bottom ball sensor channel
     * @param mbsc Middle ball sensor channel
     * @param tbsc Top ball sensor channel
     * @param j Joystick
     */
    RRGatherer(int sc, int bcc, int bbsc, int mbsc, int tbsc) {
        spinner_channel = sc;
        ball_conveyer_channel = bcc;
        bottom_ball_sensor_channel = bbsc;
        middle_ball_sensor_channel = mbsc;
        top_ball_sensor_channel = tbsc;


        spinnerVictor = new Victor(spinner_channel);
        ballConveyerVictor = new Victor(ball_conveyer_channel);
        bbsDigitalInput = new DigitalInput(bottom_ball_sensor_channel);
        mbsDigitalInput = new DigitalInput(middle_ball_sensor_channel);
        tbsDigitalInput = new DigitalInput(top_ball_sensor_channel);
    }

    /**
     * 
     * 
     *  Joystick Buttons/axis	Action	        This button layout is if the auto ball sensing is not functional

    2                       Loader Up
    3	                Loader Down	        Also, this is in right hand config mode

    10	                Spinner in, reverse, stop
     */
    private void gatherInputStates() {
        //RRLogger.logDebug(this.getClass(),"gatherInputStates()","RRGatherer::gatherInputStates()");

        boolean loader_up = RRButtonMap.getActionObject(RRButtonMap.LOADER_UP).valueOf(),
                loader_down = RRButtonMap.getActionObject(RRButtonMap.LOADER_DOWN).valueOf();
        double spinnerForward = RRButtonMap.getActionObject(RRButtonMap.SPINNER_FORWARD).getAxisState(),
                spinnerReverse = RRButtonMap.getActionObject(RRButtonMap.SPINNER_REVERSED).getAxisState();

        // Get conveyer button state
        if ((loader_up && !loader_down) || autoElevateDown) {
//            RRLogger.logDebug(this.getClass(),"gatherInputStates()","Loader up button pressed!");
//            System.out.flush();
            conveyerSpeed = -1.0 * CONVEYER_MAX_SPEED;
//            elevatorActionDuration += ELEVATOR_ACTION_SENSITIVITY;
            elevatorActionDuration = DECELERATION_CURVE_MAX;
            elevatorDecelerationDirection = -1;
        } else if ((loader_down && !loader_up) || autoElevateUp) {
//            RRLogger.logDebug(this.getClass(),"gatherInputStates()","Loader down button pressed!");
//            System.out.flush();
            conveyerSpeed = CONVEYER_MAX_SPEED;
//            elevatorActionDuration += ELEVATOR_ACTION_SENSITIVITY;
            elevatorActionDuration = DECELERATION_CURVE_MAX;
            elevatorDecelerationDirection = 1;
        } else if (!loader_up && !loader_down) {
            if (elevatorActionDuration-- <= 0) {
//                RRLogger.logDebug(this.getClass(),"gatherInputStates()","** Stopping loader!");
//                System.out.flush();
//                conveyerSpeed = 0.0;
                elevatorActionDuration = 0;
                elevatorDecelerationDirection = 0;
            }
//            else
//            {
//                RRLogger.logDebug(this.getClass(),"gatherInputStates()","** Decelerating loader " + elevatorActionDuration);
//                System.out.flush();
//                elevatorActionDuration--;
//            }

            conveyerSpeed = ((double) elevatorActionDuration) / ((double) DECELERATION_CURVE_MAX) * CONVEYER_MAX_SPEED * elevatorDecelerationDirection;

//            RRLogger.logDebug(this.getClass(),"gatherInputStates()","** conveyerSpeed " + conveyerSpeed + " elevatorActionDuration " + elevatorActionDuration);
//            System.out.flush();

            if (conveyerSpeed <= 0.1 || conveyerSpeed >= -0.1) {
                conveyerSpeed = 0.0;
            }
        }


        if (spinnerForward <= 1.0 && spinnerForward > 0.0) {
            // Left trigger pushed, spin forward
            spinnerSpeed = -1.0 * SPINNER_MAX_SPEED;
        } else if (spinnerReverse >= -1.0 && spinnerReverse < 0.0) {
            spinnerSpeed = SPINNER_MAX_SPEED;
        } else if (spinnerForward == 0.0 || spinnerReverse == 0.0) {
            spinnerSpeed = 0.0;
        }


    }

    /**
     * 
     */
    public void gather() {
        gatherInputStates();

        setGathererSpeeds();
    }

    public void gatherAuton() {
        setGathererSpeeds();
    }

    public void elevate() {
        //conveyerSpeed = -1.0 * CONVEYER_SPEED;
        autoElevateUp = true;
    }

    public void descend() {
        //conveyerSpeed = CONVEYER_SPEED;
        autoElevateDown = true;
    }

    public void stop() {
        //conveyerSpeed = 0.0;

        autoElevateDown = autoElevateUp = false;
    }

    /**
     * 
     */
    private void setGathererSpeeds() {
        spinnerVictor.set(spinnerSpeed);
        ballConveyerVictor.set(conveyerSpeed);
    }
}
