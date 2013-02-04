/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.shooter;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class StopShooting extends CommandBase {

    public StopShooting() {
        super("StopShooting");
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Debug.print("[" + getName() + "] initialize");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        shooter.stopShooter();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Debug.print("[" + getName() + "] end");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Debug.print("[" + getName() + "] interrupted");
    }
}
