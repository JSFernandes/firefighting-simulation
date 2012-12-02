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
	Squad squad_;
	
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
		else if(autonomousStep() == false) {
			if(current_order_ == null) {
				current_order_ = orders_.poll();
				if(current_order_ != null)
					process(current_order_);
				else if(squad_ != null)
					squad_.getThingsToDo(this, leader_);
			}
			else
				process(current_order_);
		}
	}
	
	boolean coordsInRange(Point p, Space s) {
		return (p.x >= 0 && p.y >= 0 && p.y < s.height_ && p.x < s.width_);
	}
	
	private boolean autonomousStep() {
		FireAgent fire;
		fire = pathfinding_algorithm_.fireInRange(pos_, space_, Constants.SAFE_DISTANCE, false);
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
			if(coordsInRange(new Point(next_x, next_y), space_) && space_.agents_.getObjectAt(next_x, next_y) == null) {
				space_.agents_.putObjectAt(pos_.x, pos_.y, null);
				pos_.x = next_x;
				pos_.y = next_y;
				space_.agents_.putObjectAt(pos_.x, pos_.y, this);
			}
			
			return true;
		}
		fire = pathfinding_algorithm_.fireInRange(pos_, space_, Constants.DEFAULT_WATER_RANGE, true);
	    if (fire != null) {
	    	//FIXME código para apagar fogo
	    	space_.agents_.putObjectAt(fire.getX(), fire.getY(), null);
			return true;
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
			
			if(squad_ != null && squad_.isInRange(new Point(next_x, next_y)) 
					&& coordsInRange(new Point(next_x, next_y), space_) &&
					space_.agents_.getObjectAt(next_x, next_y) == null) {
				space_.agents_.putObjectAt(pos_.x, pos_.y, null);
				pos_.x = next_x;
				pos_.y = next_y;
				space_.agents_.putObjectAt(pos_.x, pos_.y, this);
			}
			//TODO else????
			
			return true;
		}
		
		return false;
		
		//TODO e se nem vir fogo?????????????????????
		
	}

	public void move_step() {
		space_.agents_.putObjectAt(pos_.x, pos_.y, null);
		pos_.x = move_path_[next_path_point_].x;
		pos_.y = move_path_[next_path_point_].y;
		if(space_.agents_.getObjectAt(pos_.x, pos_.y) != null) {
			System.out.println("oh woops " + pos_);
			System.out.println("next path point " + move_path_[next_path_point_]);
			System.out.println("current path point " + move_path_[next_path_point_-1]);
			pos_.x = (int) move_path_[next_path_point_ - 1].getX();
			pos_.y = (int) move_path_[next_path_point_ - 1].getY();
			space_.agents_.putObjectAt(pos_.x, pos_.y, this);
			System.out.println("back to " + pos_);
			move_path_ = pathfinding_algorithm_.findPath(new Point(pos_.x, pos_.y), new Zone(move_path_[move_path_.length - 1]), space_);
			System.out.println("I have a new path");
			if(move_path_ == null)
				leader_.decision();
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

	private void process(Command order) {
		moving_ = true;
		current_order_ = order;
		System.out.println("nonsense");
		move_path_ = pathfinding_algorithm_.findPath(pos_, order.target_, space_);
		if(move_path_ == null) {
			leader_.decision();
			return;
		}
		next_path_point_ = 0;
		//System.out.println("starting steps");
		move_step();
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
	
	public void getNewOrder() {
		Point p = squad_.findPointInNeed();
		if (p == null) {
			squad_.relocateSquad(leader_);
		}
		else {
			orders_.add(new Command(new Zone(p)));
		}
	}
	
	public boolean seesFire() {
		return (pathfinding_algorithm_.fireInRange(pos_, space_, Constants.DEFAULT_VISION_RADIUS, false) != null);
	}


}
