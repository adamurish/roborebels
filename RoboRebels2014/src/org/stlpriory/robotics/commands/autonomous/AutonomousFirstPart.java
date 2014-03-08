/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author William
 */
public class AutonomousFirstPart extends CommandGroup {
    
    public AutonomousFirstPart() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
        addParallel(new Driving());
        addParallel(new ImageProcessing());
    }
    
    protected void execute ( ) {
        System.out.println("AutonoumousFirstPart at " + System.currentTimeMillis());
        super.execute();
    }
    
    protected void end() {
            System.out.println("AutonoumousFirstPart ending at " + System.currentTimeMillis());
            super.end();
        }
}
