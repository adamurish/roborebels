/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.drivetrain.DriveStraight;
import org.stlpriory.robotics.commands.shooter.LoadDisc;
import org.stlpriory.robotics.commands.shooter.ResetLoadDisc;
import org.stlpriory.robotics.commands.shooter.Rumble;
import org.stlpriory.robotics.commands.shooter.StartShooting;
import org.stlpriory.robotics.commands.shooter.StopShooting;

/**
 *
 */
public class AutonomousShoot extends CommandGroup {
    private static final double DRIVING_SPEED = -0.4;
    private static final double DRIVE_STAIGHT_TIMEOUT = 0.5;

    private static final double WAIT_BEFORE_LOAD  = 1;
    private static final double WAIT_BEFORE_RESET = 0.5;

    public AutonomousShoot() {
        super("AutonomousShoot");

        addSequential(new StartShooting());
//        addSequential(new DriveStraight(DRIVING_SPEED,DRIVE_STAIGHT_TIMEOUT));

        for (int i = 0; i < 3; i++) {
            addSequential(new WaitCommand(WAIT_BEFORE_LOAD));
            addSequential(new LoadDisc());
            addSequential(new WaitCommand(WAIT_BEFORE_RESET));
            addSequential(new ResetLoadDisc());
            addSequential(new Rumble());
        }

        addSequential(new ResetLoadDisc());
        addSequential(new DriveStraight(DRIVING_SPEED,DRIVE_STAIGHT_TIMEOUT));
        addSequential(new StopShooting());

    }
}
