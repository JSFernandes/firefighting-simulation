package environment;

import java.util.ArrayList;


import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.util.Random;

public class FireAgent implements Drawable {
	protected int x_, y_; // current coordinates in space
	protected Space space_; // back links to our space and model objects
	protected FireFighterModel model_;
	protected int fire_intensity_;
	private double incenerating_prob_;

	public FireAgent(int x, int y, Space space) {
		x_ = x;
		y_ = y;
		
		space_ = space;
		fire_intensity_ = 1;
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

	public int windProb(int i){
		
		int[] winds = { 20, 20, 20, 20, 20, 20, 20, 20};
		int p = space_.wind_.strength()*5;
		
		if (i < 3) {
			winds[i] += p;
		//} else if (i == 3 || i == 7) {
		//	winds[i] = 20;
		} else {
			winds[i] -= p;
		}
		
		return winds[i];
	}
	
	public void step() {
		Tree tt = (Tree) space_.trees_.getObjectAt(x_, y_);

		if (!tt.burn(100)) {
			fire_intensity_ = 0;
		}

		ArrayList<Tree> v = space_.getNeighbors(x_, y_);

		for (int i = 0; i < v.size(); ++i) {

			incenerating_prob_ = windProb(i);
			
			Tree t = v.get(i);
			if (t != null) {
				switch (t.getState()) {
					case FRESH :
						if (t.burn(incenerating_prob_))
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

} 
