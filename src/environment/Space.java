package environment;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	public int width_ = 200, height_ = 200;
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
		hmg.setVariance(32);
		double[][] map = hmg.generate();

		for (int i = 0; i < width_; ++i)
			for (int j = 0; j < height_; ++j) {
				double v = map[i][j] + 17;
				if (v < 1)
					v = 1;
				if (v > 32)
					v = 32;
				
				int a=0;
				if(v<8)
					a=1;
				else if(v<16)
					a=2;
				else if(v<24)
					a=3;
				else 
					a=4;
				trees_.putObjectAt(i, j, new Tree(i, j,  a));

			}
		// System.out.println(Arrays.deepToString(map));
		/*String s="";
		for(int i=0; i<200; ++i){
			s+="\n";
			for(int j=0; j<200; ++j){
				int a = ((Tree) trees_.getObjectAt(i, j)).density;
				String c;
				if(a<8)
					c="1";
				else if(a<16)
					c="2";
				else if(a<24)
					c="3";
				else 
					c="4";
				s+= c;
				if(j!=199)
					s+=", ";
			}
		}
		
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter("C:\\aforest.txt"));
			out.print(s); 
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
	}

	// TODO: Refactor
	public Value2DDisplay getSpaceDisplay() {
		class myColorMap extends ColorMap {
			myColorMap() {
				/*for (int i = 1; i <= 32; ++i) {
					int v = 255 - (i - 1) * 7;
					this.mapColor(i, new Color(32, v, 32));
				}
				this.mapColor(33, new Color(209, 255, 135));
				
				for(int i=1; i<=32; ++i)
					if(i<8)
						this.mapColor(i, new Color(32, 180, 32));
					else if(i<16)
						this.mapColor(i, new Color(32, 140, 32));
					else if(i<24)
						this.mapColor(i, new Color(32, 100, 32));
					else 
						this.mapColor(i, new Color(32, 60, 32));*/
				this.mapColor(5, new Color(209, 255, 135));
				this.mapColor(1, new Color(32, 180, 32));
				this.mapColor(2, new Color(32, 140, 32));
				this.mapColor(3, new Color(32, 100, 32));
				this.mapColor(4, new Color(32, 60, 32));
			}

			public Color getColor(Integer i) {
				if(i<1 || i>4)
					i=1;
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
