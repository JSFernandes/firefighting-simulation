package command;

import java.awt.geom.Point2D;

public class Command {
	
	public CommandType type_;
	public Point2D target_;
	
	public Command(CommandType type, Point2D target) {
		type_ = type;
		target_ = target;
	}

}
