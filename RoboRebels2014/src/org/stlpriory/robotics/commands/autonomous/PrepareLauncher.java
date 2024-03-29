/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.stlpriory.robotics.commands.launcher.ResetWithTimeout;
import org.stlpriory.robotics.commands.launcher.Stop;

/**
 *
 */
public class PrepareLauncher extends CommandGroup {
    
    public PrepareLauncher() { 

        // retract the puncher to limit switch position and then stop
        addSequential(new ResetWithTimeout()); 
        addSequential(new Stop());
    }
}
