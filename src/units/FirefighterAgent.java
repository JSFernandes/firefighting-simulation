package units;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import command.Command;

import environment.FireAgent;
import environment.FireFighterModel;
import environment.Space;
import environment.Zone;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;


public class FirefighterAgent implements Drawable {
	
	Point pos_;
	Space space_; // back links to our space and model objects
	//protected FireFighterModel model_;
	
	LinkedList<Command> orders_;
	CommanderAgent leader_;
	Command current_order_;
	boolean moving_;
	Point[] move_path_;
	int next_path_point_;
	static Pathfinding pathfinding_algorithm_ = new Pathfinding();
	
	public FirefighterAgent(Point p, Space space, CommanderAgent leader) {
		pos_ = (Point) p.clone();
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
		if(current_order_ == null) {
			current_order_ = orders_.poll();
			if(current_order_ == null)
				autonomousStep();
			else
				process(current_order_);
		}
		else
			process(current_order_);
	}
	
	private void autonomousStep() {
		FireAgent fire;
		fire = pathfinding_algorithm_.fireInRange(pos_, space_, Constants.SAFE_DISTANCE, true);
		if (fire != null) {
			int fire_x = fire.getX();
			int fire_y = fire.getY();
			
			int next_x, next_y;
			
			if(pos_.y < fire_y) {
				next_y = pos_.y - 1;
			}
			else {
				next_y = pos_.y + 1;
			}
			
			if(pos_.x < fire_x) {
				next_x = pos_.x - 1;
			}
			else {
				next_x = pos_.x + 1;
			}
			
			space_.agents_.putObjectAt(pos_.x, pos_.y, null);
			pos_.x = next_x;
			pos_.y = next_y;
			space_.agents_.putObjectAt(pos_.x, pos_.y, this);
			
			return;
		}
		fire = pathfinding_algorithm_.fireInRange(pos_, space_, Constants.DEFAULT_WATER_RANGE, true);
	    if (fire != null) {
	    	//FIXME código para apagar fogo
	    	space_.agents_.putObjectAt(fire.getX(), fire.getY(), null);
			return;
		}
		fire = pathfinding_algorithm_.fireInRange(pos_, space_, Constants.DEFAULT_VISION_RADIUS, true);
		if (fire != null) {
			int fire_x = fire.getX();
			int fire_y = fire.getY();
			
			int next_x, next_y;
			
			if(pos_.y < fire_y) {
				next_y = pos_.y + 1;
			}
			else if (pos_.y > fire_y){
				next_y = pos_.y - 1;
			}
			else {
				next_y = pos_.y;
			}
			
			if(pos_.x < fire_x) {
				next_x = pos_.x + 1;
			}
			else if(pos_.x > fire_x){
				next_x = pos_.x - 1;
			}
			else {
				next_x = pos_.x;
			}
			
			space_.agents_.putObjectAt(pos_.x, pos_.y, null);
			pos_.x = next_x;
			pos_.y = next_y;
			space_.agents_.putObjectAt(pos_.x, pos_.y, this);
			
			return;
		}
		
		//TODO e se nem vir fogo?????????????????????
		
	}

	public void move_step() {
		space_.agents_.putObjectAt(pos_.x, pos_.y, null);
		pos_.x = move_path_[next_path_point_].x;
		pos_.y = move_path_[next_path_point_].y;
		if(space_.agents_.getObjectAt(pos_.x, pos_.y) != null) {
			pos_.x = (int) move_path_[next_path_point_ - 1].getX();
			pos_.y = (int) move_path_[next_path_point_ - 1].getY();
			space_.agents_.putObjectAt(pos_.x, pos_.y, this);
			move_path_ = pathfinding_algorithm_.findPath(new Point(pos_.x, pos_.y), new Zone(move_path_[move_path_.length - 1]), space_);
			next_path_point_ = 0;
			return;
		}
		space_.agents_.putObjectAt(pos_.x, pos_.y, this);
		++next_path_point_;
		if(current_order_.target_.isInZone(pos_)) {
			moving_ = false;
			current_order_ = null;
		}
	}
	
	public void water_step() {
		Vector<Object> agents = space_.agents_.getMooreNeighbors(pos_.x, pos_.y, Constants.DEFAULT_WATER_RANGE, Constants.DEFAULT_WATER_RANGE, false);
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
				System.out.println("starting movement");
				moving_ = true;
				move_path_ = pathfinding_algorithm_.findPath(pos_, order.target_, space_);
				System.out.println("CRASHO NO PATHFINDING BABY");
				next_path_point_ = 0;
				System.out.println("starting steps");
				move_step();
			break;
			case WATER:
				water_step();
			break;
		}
	}

	@Override
	public void draw(SimGraphics g) {
		g.drawFastRoundRect(java.awt.Color.yellow);
	}

	@Override
	public int getX() {
		return pos_.x;
	}

	@Override
	public int getY() {
		return pos_.y;
	}


}
