package command;

import java.awt.Point;

public class Command {
	
	public CommandType type_;
	public Point target_;
	
	public Command(CommandType type, Point target) {
		type_ = type;
		target_ = target;
	}

}
