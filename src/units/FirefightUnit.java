package units;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import command.Command;
import command.CommandType;
import environment.FireAgent;
import environment.FireFighterModel;
import environment.Space;


public abstract class FirefightUnit {
	protected int x_, y_;
	protected Space space_; // back links to our space and model objects
	//protected FireFighterModel model_;
	
	LinkedList<Command> orders_;
	Commander leader_;
	Command current_order_;
	boolean moving_;
	Point[] move_path_;
	int next_path_point_;
	static Pathfinding pathfinding_algorithm_ = new Pathfinding();
	
	protected final int vision_radius_ = Constants.DEFAULT_VISION_RADIUS;
	
	public FirefightUnit(int x, int y, Space space, Commander leader) {
		x_ = x;
		y_ = y;
		space_ = space;
		//model_ = model;
		orders_ = new LinkedList<Command>();
		moving_ = false;
		leader_ = leader;
	}
	
	public void step() {
		if (moving_ == true) {
			move_step();
		}
		if(current_order_ == null)
			current_order_ = orders_.poll();
		else
			process(current_order_);
	}
	
	public void move_step() {
		space_.agents_.putObjectAt(x_, y_, null);
		x_ = move_path_[next_path_point_].x;
		y_ = move_path_[next_path_point_].y;
		if(space_.agents_.getObjectAt(x_, y_) != null) {
			x_ = (int) move_path_[next_path_point_ - 1].getX();
			y_ = (int) move_path_[next_path_point_ - 1].getY();
			space_.agents_.putObjectAt(x_, y_, this);
			move_path_ = pathfinding_algorithm_.findPath(new Point(x_, y_), move_path_[move_path_.length - 1], space_);
			next_path_point_ = 0;
			return;
		}
		space_.agents_.putObjectAt(x_, y_, this);
		++next_path_point_;
		if(next_path_point_ == move_path_.length) {
			moving_ = false;
			current_order_ = null;
		}
	}
	
	public void water_step() {
		Vector<Object> agents = space_.agents_.getMooreNeighbors(x_, y_, Constants.DEFAULT_WATER_RANGE, Constants.DEFAULT_WATER_RANGE, false);
		Collections.shuffle(agents);
		for(int i = 0; i < agents.size(); ++i) {
			if(agents.get(i).getClass() == FireAgent.class) {
				space_.agents_.putObjectAt(((FireAgent)agents.get(i)).getX(), ((FireAgent)agents.get(i)).getY(), null);
				return;
			}
		}
		leader_.new_orders();
	}

	private void process(Command order) {
		switch(order.type_) {
			case MOVE:
				moving_ = true;
				move_path_ = pathfinding_algorithm_.findPath(new Point(x_, y_), order.target_, space_);
				next_path_point_ = 0;
				move_step();
			break;
			case WATER:
				water_step();
			break;
		}
	}

}
