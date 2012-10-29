import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import uchicago.src.collection.BaseMatrix;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.space.Object2DTorus;
import uchicago.src.sim.util.Random;

public class Space {
	public Object2DTorus trees_;
	public Object2DTorus fire_;
	public int width_ = 300, height_ = 300;
	public WindDirection wind_;

	public Space() {
		trees_ = new Object2DTorus(width_, height_);
		fire_ = new Object2DTorus(width_, height_);
		wind_ = WindDirection.NW;
		
		build();
		startFire();
	}

	void startFire() {
		for (int i = 0; i < 1; ++i) {
			addAgent();
		}
	}

	public void addAgent() {
		int x, y;
		x = Random.uniform.nextIntFromTo(0, width_ - 1);
		y = Random.uniform.nextIntFromTo(0, height_ - 1);
		FireAgent fire_agent = new FireAgent(x, y, this);
		fire_agent.fire_intensity_ = 1;
		fire_.putObjectAt(x, y, fire_agent);
	}

	public void addAgent(int x, int y) {
		fire_.putObjectAt(x, y, new FireAgent(x, y, this));
	}

	void build() {
		HeightMapGenerator hmg = new HeightMapGenerator();
		hmg.setSize(height_, width_);
		hmg.setVariance(16);
		double[][] map = hmg.generate();

		for (int i = 0; i < width_; ++i)
			for (int j = 0; j < height_; ++j) {
				double v = map[i][j] + 17;
				if (v < 1)
					v = 1;
				if (v > 32)
					v = 32;
				trees_.putObjectAt(i, j, new Tree(i, j, (int) v));

			}
		// System.out.println(Arrays.deepToString(map));
	}

	// TODO: Refactor
	public Value2DDisplay getSpaceDisplay() {
		class myColorMap extends ColorMap {
			myColorMap() {
				for (int i = 1; i <= 32; ++i) {
					int v = 255 - (i - 1) * 7;
					this.mapColor(i, new Color(32, v, 32));
				}
				this.mapColor(33, new Color(209, 177, 135));
			}

			public Color getColor(Integer i) {
				return this.getColor(i.intValue());
			}

			public Color getColor(int i) {
				// if(i > 31) i=31;
				// if(i < 0) i=0;
				return super.getColor(i);
			}

		}
		;

		return new Value2DDisplay(trees_, new myColorMap());
	}

	public Object2DDisplay getAgentDisplay() {
		return new Object2DDisplay(fire_);
	}

	public void step() {
		// build();//discoteca
	}

	public void fireStep() {
		ArrayList<FireAgent> agents = new ArrayList<FireAgent>();
		BaseMatrix m = fire_.getMatrix();
		for (int i = 0; i < width_; ++i)
			for (int j = 0; j < height_; ++j)
				if (m.get(i, j) != null)
					agents.add((FireAgent) m.get(i, j));

		// SimUtilities.shuffle( agents );
		int siz = agents.size();

		FireAgent agent;
		for (int i = 0; i < siz; ++i) {
			agent = agents.get(i);
			if (agent.fire_intensity_ <= 0) {
				fire_.putObjectAt(agent.x_, agent.y_, null);
			} else {
				agent.step();
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return 8 sized array. the 3 first results are the wind favored ones.
	 *         null when already on fire or (x,y) on border
	 */
	public ArrayList<Tree> getNeighbors(int x, int y) {
		LinkedList<Tree> temp = new LinkedList<Tree>();
		// if(y==0 || x==0 || y==h-1 || x==w-1)
		// System.out.println("fff");
		temp.add(fire_.getObjectAt(x, y - 1) == null && y != 0 ? (Tree) trees_
				.getObjectAt(x, y - 1) : null);
		
		temp.add(fire_.getObjectAt(x + 1, y - 1) == null && x != width_ - 1 && y != 0 ? (Tree) trees_
				.getObjectAt(x + 1, y - 1) : null);
		
		temp.add(fire_.getObjectAt(x + 1, y) == null && x != width_ - 1 ? (Tree) trees_
				.getObjectAt(x + 1, y) : null);
		
		temp.add(fire_.getObjectAt(x + 1, y + 1) == null && x != width_ - 1
				&& y != height_ - 1 ? (Tree) trees_.getObjectAt(x + 1, y + 1) : null);
		
		temp.add(fire_.getObjectAt(x, y + 1) == null && y != height_ - 1 ? (Tree) trees_
				.getObjectAt(x, y + 1) : null);
		
		temp.add(fire_.getObjectAt(x - 1, y + 1) == null && x != 0 && y != height_ - 1 ? (Tree) trees_
				.getObjectAt(x - 1, y + 1) : null);
		
		temp.add(fire_.getObjectAt(x - 1, y) == null && x != 0 ? (Tree) trees_
				.getObjectAt(x - 1, y) : null);
		
		temp.add(fire_.getObjectAt(x - 1, y - 1) == null && x != 0 && y != 0 ? (Tree) trees_
				.getObjectAt(x - 1, y - 1) : null);

		ArrayList<Tree> result = new ArrayList<Tree>(8);
		for (int i = 0; i < 8; ++i) {
			result.add(null);
		}
		
		for (int val = 0; val < 8; ++val) {
			int a = (val - (wind_.value() - 1) + 8) % 8;
			Tree t = temp.get(val);
			result.set(a, t);
		}

		return result;
	}
}
