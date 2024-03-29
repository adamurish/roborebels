/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author aidan/alan
 */
public class RRAutonomous {

    int target_selected = RoboRebels.AUTO_TARGET;
//    private     RRDIPSwitch     dipSwitch;
    private RRTracker tracker;
    private RRShooter shooter;
    private RRBallSensor sensor;
    private RRGatherer gatherer;
    private boolean delay_shooting = true;
    private boolean extra_delay_shooting = false;

//    public  RRAutonomous(RRDIPSwitch ds, RRTracker tr, RRShooter sh, RRBallSensor sr, RRGatherer gr)
    public RRAutonomous(RRTracker tr, RRShooter sh, RRBallSensor sr, RRGatherer gr) {
//        dipSwitch = ds; 
        tracker = tr;
        shooter = sh;
        sensor = sr;
        gatherer = gr;
    }

    /*
     * called once at start of Autonomous period
     */
    void auton_init() {
        RoboRebels.time_started_waiting = Timer.getFPGATimestamp();

        // don't check dip switches
//        
//        if (dipSwitch.getState(0)) {
//            target_selected = RoboRebels.HIGHEST_TARGET;   // Read first DIP Switch
//            RRLogger.logDebug(this.getClass(),"auton_init()","COOLIO! We're locked onto the top target! =)");
//
//        }  else if (dipSwitch.getState(1) && dipSwitch.getState(2)) {
//            target_selected = RoboRebels.LOWEST_TARGET;
//            RRLogger.logDebug(this.getClass(),"auton_init()","We're locked onto the lowest target! =)");
//
//        } else if (dipSwitch.getState(1)) {
//            target_selected = RoboRebels.LEFT_TARGET;
//            RRLogger.logDebug(this.getClass(),"auton_init()","COOLIO! We're locked onto the left target! =)");
//
//        } else if (dipSwitch.getState(2)) {
//            target_selected = RoboRebels.RIGHT_TARGET;
//            RRLogger.logDebug(this.getClass(),"auton_init()","COOLIO! We're locked onto the right target! =)");
//
//        }
//        if (dipSwitch.getState(3)) {
//            RRLogger.logDebug(this.getClass(),"auton_init()","OK! Let's wait for the other team to shoot first...)");
//            extra_delay_shooting = true;
//        } else {
//            delay_shooting = false;  // Don't delay shooting at start of autonomous
//        }

        extra_delay_shooting = false;
        target_selected = RoboRebels.AUTO_TARGET;
        delay_shooting = true;

        shooter.expandShooter(); // Works now! :-)
        //RoboRebels.autonomous_complete = true;                  // For now

    }

    /*
     * called repeatedly suring Autonomous period
     */
    void auton_periodic() {

//        RRLogger.logDebug(this.getClass(),"auton_periodic()","Periodic State: " + RoboRebels.azimuth_lock + " " + RoboRebels.elevation_lock + " " + shooter.locked() + " " +
//                RoboRebels.muzzle_velocity_lock + " " + RoboRebels.autonomous_mode_tracking + " " + RoboRebels.autonomous_tracking_failed + " " +
//                RoboRebels.no_balls_shot + " " + 
//                RoboRebels.isShooting + " " +  delay_shooting + " " + RoboRebels.isFinishedShooting + " " +  
//                RoboRebels.shot_first_ball + " " +  RoboRebels.delay_between_balls + " " +  RoboRebels.second_ball_started_shoot + " " +
//                RoboRebels.shot_second_ball + " " +  RoboRebels.delay_after_two_balls + " " + RoboRebels.driving_to_bridge + " " +
//                RoboRebels.autonomous_complete
//                );
//        
//        RRLogger.logDebug(this.getClass(),"auton_periodic()","DIP Switch State: " + dipSwitch.getState(0) + " " +  dipSwitch.getState(1) + " " +
//                dipSwitch.getState(2) + " " +  dipSwitch.getState(3));

        //lock onto correct target

        tracker.trackTarget(target_selected);

        //if (RoboRebels.DEBUG_ON) {
        //   RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: auton_periodic running " + tracker.round2(Timer.getFPGATimestamp()));
        //}

        if (!shooter.tracking) {
            shooter.shoot();
            gatherer.gather();
        }

        if (RoboRebels.autonomous_tracking_failed) {
            // If tracking failed, end shooting
            RoboRebels.autonomous_complete = true;   // TODO: Make robot still drive towards bridge to get balls
        }
        if (delay_shooting) {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Delaying at Start");
            }

            double time_current = Timer.getFPGATimestamp();

            if (extra_delay_shooting) {
                if (time_current > (RoboRebels.time_started_waiting + RoboRebels.DELAY_AT_START_OF_AUTON + RoboRebels.EXPAND_TIME)) {
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Finished Delaying at Start");
                    }
                    delay_shooting = false;
                    RoboRebels.autonomous_mode_tracking = true;
                }
            } else {
                if (time_current > (RoboRebels.time_started_waiting + RoboRebels.EXPAND_TIME)) {
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Finished Delaying at Start");
                    }
                    delay_shooting = false;
                    RoboRebels.autonomous_mode_tracking = true;
                }
            }

        } //check to see if target locked
        else if (shooter.locked() && RoboRebels.no_balls_shot && !RoboRebels.shot_first_ball
                && !RoboRebels.isShooting && !RoboRebels.driving_to_bridge && !RoboRebels.autonomous_complete) {
            shooter.auton_shoot();
            shooter.shootBall();
            RoboRebels.no_balls_shot = false;

            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Starting shooting first ball now");
            }
        } else if (!RoboRebels.no_balls_shot && RoboRebels.isFinishedShooting && !RoboRebels.shot_first_ball && !RoboRebels.autonomous_complete) {
            /*
             *Need to delay so we don't shoot again immediately
             */

            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Shooting first ball is now Complete!! " + RoboRebels.isFinishedShooting + " " + RoboRebels.shot_first_ball);
            }
            RoboRebels.delay_between_balls = true;
            RoboRebels.isFinishedShooting = false;
            RoboRebels.shot_first_ball = true;
            RoboRebels.time_started_waiting = Timer.getFPGATimestamp();
        } else if (RoboRebels.delay_between_balls && !RoboRebels.autonomous_complete) {

            // wait for ball to shoot and score with no tracking on
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Delaying between balls");
            }

            double time_current = Timer.getFPGATimestamp();

            if (time_current > (RoboRebels.time_started_waiting + RoboRebels.DELAY_BETWEEN_SHOTS)) {
                RoboRebels.delay_between_balls = false;  // Done waiting              
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Finished delaying between balls");
                }
                RoboRebels.autonomous_mode_tracking = true;                     // Start tracking again
            }
        } //shoot a second time
        else if (shooter.locked() && RoboRebels.shot_first_ball && !RoboRebels.second_ball_started_shoot
                && !RoboRebels.shot_second_ball && !RoboRebels.delay_between_balls
                && !RoboRebels.isShooting && !RoboRebels.autonomous_complete) {
            shooter.auton_shoot();
            shooter.shootBall();
            RoboRebels.second_ball_started_shoot = true;

            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Starting shooting second ball now");
            }
        } else if (RoboRebels.isFinishedShooting && RoboRebels.shot_first_ball && RoboRebels.second_ball_started_shoot
                && !RoboRebels.shot_second_ball && !RoboRebels.autonomous_complete) {
            /*
             *Need to delay so we don't shoot again immediately
             */

            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Shooting second ball is now Complete");
            }
            RoboRebels.delay_after_two_balls = true;
            RoboRebels.isFinishedShooting = false;
            RoboRebels.shot_second_ball = true;
            RoboRebels.time_started_waiting = Timer.getFPGATimestamp();
        } else if (RoboRebels.delay_after_two_balls && !RoboRebels.autonomous_complete) {

            // wait for ball to score while re-calculating trajectory
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Delaying after second ball");
            }

            double time_current = Timer.getFPGATimestamp();

            if (time_current > (RoboRebels.time_started_waiting + RoboRebels.DELAY_BETWEEN_SHOTS)) {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Delaying after second ball is now Complete");
                }

                RoboRebels.delay_after_two_balls = false;  // Done waiting
                RoboRebels.driving_to_bridge = true;

                //shooter.retractShooter();           // Didn't work - tried to keep retracting.  Retract shooter so we can deploy bridge arm as soon as teleop begins!

                RoboRebels.time_started_driving = Timer.getFPGATimestamp();  // Now start driving timer

                // Drive, robot, drive!!
            }
        } else if (RoboRebels.driving_to_bridge && !RoboRebels.autonomous_complete) {

            // drive towards bridge.

            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Could be Driving to bridge...");
            }

            // Drive, drive!

            double time_current = Timer.getFPGATimestamp();

            if (time_current > (RoboRebels.time_started_driving + RoboRebels.DRIVE_TIME_TO_BRIDGE)) {
                //              RRLogger.logDebug(this.getClass(),"auton_periodic()","Driving to bridge is complete - stopping");

                RoboRebels.driving_to_bridge = false;

                // Stop driving!!

                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Now complete!");
                }
                RoboRebels.autonomous_complete = true;
            }
        } else {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"auton_periodic()","Autonomous: Idle");
            }
        }
        // drive backwards about 5-10 feet (to be closer than other robots to the balls on the bridge)

        //EXPLODE

        // Pwn other robots       
    }
}
