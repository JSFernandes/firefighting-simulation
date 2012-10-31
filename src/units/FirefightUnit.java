package units;
import java.util.LinkedList;

import command.Command;
import environment.FireFighterModel;
import environment.Space;


public abstract class FirefightUnit {
	protected int x_, y_;
	protected Space space_; // back links to our space and model objects
	protected FireFighterModel model_;
	
	LinkedList<Command> orders_;
	
	protected final int vision_radius_ = Constants.DEFAULT_VISION_RADIUS;
	
	public abstract void step();

}
