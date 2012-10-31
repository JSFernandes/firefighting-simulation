package environment;

import java.util.ArrayList;


import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.util.Random;

public class FireAgent implements Drawable {
	protected int x_, y_; // current coordinates in space
	protected Space space_; // back links to our space and model objects
	protected FireFighterModel model_;
	protected int fire_intensity_, max_fire_intensity_;
	private double incenerating_prob_;

	public FireAgent(int x, int y, Space space) {
		x_ = x;
		y_ = y;
		
		space_ = space;
		
		max_fire_intensity_ = 15;
		fire_intensity_ = 1;
	}

	private double treeLifeToFireIntensity(int tl) {
		// System.out.println("tl: "+ tl);
		if (tl < 75) {
			return (tl / 75 * (max_fire_intensity_ - 1) + 1);
		} else {
			return (max_fire_intensity_ - (tl - 75) / 25 * max_fire_intensity_);
		}
	}

	public void setXY(int x, int y) {
		x_ = x;
		y_ = y;
	}

	public int getX() {
		return x_;
	}

	public int getY() {
		return y_;
	}

	public void draw(SimGraphics g) {
		g.drawFastRoundRect(java.awt.Color.red);
	}

	public void step() {
		Tree tt = (Tree) space_.trees_.getObjectAt(x_, y_);
		fire_intensity_ = (int) treeLifeToFireIntensity(tt.percentage());

		if (!tt.burn(100)) {
			fire_intensity_ = 0;
		}

		ArrayList<Tree> v = space_.getNeighbors(x_, y_);

		for (int i = 0; i < v.size(); ++i) {

			if (i < 3) {
				incenerating_prob_ = 35;
			} else if (i == 3 || i == 7) {
				incenerating_prob_ = 15;
			} else {
				incenerating_prob_ = 5;
			}
			
			Tree t = v.get(i);
			if (t != null) {
				switch (t.getState()) {
					case FRESH :
						if (t.burn(incenerating_prob_ + fire_intensity_))
							space_.fire_.putObjectAt(t.x, t.y, new FireAgent(t.x,
									t.y, space_));
						break;
					case BURNING :
						//
						break;
					case ASHES :
						// kill the fire agent on this xy
						break;
				}
			}
		}
		// space.addAgent(x, y);
	}

	public void step2() {
		int dir;
		//int rval;
		int new_x = x_; // use local copies because we update position
		int new_y = y_;
		
		do {
			new_x = x_; // use local copies because we update position
			new_y = y_; // using moveAgent()...

			// move in the great circle pattern:
			// 3 2 1
			// 4 A 0
			// 5 6 7

			dir = Random.uniform.nextIntFromTo(0, 7);
			switch (dir) {
				case 0:
					++new_x;
					break;
				case 1:
					++new_x;
					++new_y;
					break;
				case 2:
					++new_y;
					break;
				case 3:
					--new_x;
					++new_y;
					break;
				case 4:
					--new_x;
					break;
				case 5:
					--new_x;
					--new_y;
					break;
				case 6:
					--new_y;
					break;
				case 7:
					++new_x;
					--new_y;
					break;
				default:
			}
			
			if (new_x < 0) {
				new_x = 0;
			}
			if (new_y < 0) {
				new_y = 0;
			}
			if (new_y >= space_.height_) {
				new_y = space_.height_;
			}
			if (new_x >= space_.width_) {
				new_x = space_.width_;
			}
		} while (false);// space.fire.getObjectAt(newx, newy)!=null);

		// move ourself in the space, but only to unoccupied locations
		// this also takes care of torus wrap-around in agent.x,y
		// keep track of collisions just to have something to graph
		// rval = model.moveAgent( this, newx, newy );
		space_.fire_.putObjectAt(x_, y_, null);
		x_ = new_x;
		y_ = new_y;

		space_.fire_.putObjectAt(x_, y_, this);

		// System.out.println("x: "+x+" y: "+y);

		// save which way we went for logging and stuff...
		direction = dir;
	}

	// model specific stuff

	// Just as the model needs get and set accessor methods in order for
	// its initial parameters to be displayed and thus subject to modification,
	// an agent (or any other object) can be probed through similar get and set
	// methods.
	//
	// Probing consists of clicking on an object in the display, causing that
	// object's current state to be displayed. What is displayed depends on
	// the various get and set methods implemented by the object. For example,
	// if an object has setDirection() and getDirection() methods, a Direction
	// field will be displayed providing its current value and allowing the
	// user to enter a new value and press enter.
	// As of the 3.1 release only number, String, and boolean fields can be
	// displayed. Of course a user can use the get and set methods
	// to turn a Vector, for example, into a String or whatever is appropriate.

	// for this dumb model we keep track of the followng just to make graphs
	int direction; // which way we went last
	int collideSpace = 0; // count of collisions with space boundaries
	int collideAgent = 0; // count of collisions with other agents

	/**
	 * @param d
	 *            set the last direction value.
	 */
	public void setDirection(int d) {
		direction = d;
	}

	/**
	 * @return the last direction value.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @return the collideAgent count.
	 */
	public int getCollideAgent() {
		return this.collideAgent;
	}

	/**
	 * @param collideAgent
	 *            The collideAgent count to set.
	 */
	public void setCollideAgent(int collideAgent) {
		this.collideAgent = collideAgent;
	}

	/**
	 * @return the collideSpace count.
	 */
	public int getCollideSpace() {
		return this.collideSpace;
	}

	/**
	 * @param collideSpace
	 *            The collideSpace count to set.
	 */
	public void setCollideSpace(int collideSpace) {
		this.collideSpace = collideSpace;
	}

} // end'o'T2DAgent
