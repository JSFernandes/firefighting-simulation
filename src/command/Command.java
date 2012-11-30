package command;

import java.awt.Point;

import environment.Zone;

public class Command {
	
	public CommandType type_;
	public Zone target_;
	
	public Command(CommandType type, Zone target) {
		type_ = type;
		target_ = target;
	}

}
