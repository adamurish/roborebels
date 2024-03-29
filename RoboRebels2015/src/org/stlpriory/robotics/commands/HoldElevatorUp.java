package org.stlpriory.robotics.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class HoldElevatorUp extends CommandGroup {

	boolean on = true;

	public HoldElevatorUp() {
		// Add Commands here:
		// e.g. addSequential(new Command1());
		// addSequential(new Command2());
		// these will run in order.

		// To run multiple commands at the same time,
		// use addParallel()
		// e.g. addParallel(new Command1());
		// addSequential(new Command2());
		// Command1 and Command2 will run in parallel.

		// A command group will require all of the subsystems that each member
		// would require.
		// e.g. if Command1 requires chassis, and Command2 requires arm,
		// a CommandGroup containing them would require both
		// the chassis and the
		// arm.
		addThings();
	}

	public HoldElevatorUp(boolean onin) {
		on = onin;
		addThings();
	}

	private void addThings() {
		if (!on) return;
		addSequential(new ElevatorUpSlow());
		addSequential(new WaitCommand(.05));
		addSequential(new ElevatorStop());
		addSequential(new WaitCommand(.005));
	}
}
