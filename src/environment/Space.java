package environment;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import uchicago.src.collection.BaseMatrix;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.space.Object2DTorus;
import uchicago.src.sim.util.Random;
import units.FirefighterAgent;

public class Space {
	public Object2DTorus trees_;
	public Object2DTorus agents_;
	public int width_ = 150, height_ = 150;
	public WindDirection wind_;
	public boolean eightDirections = true;
	ArrayList<FirefighterAgent> firefighters_;
	
	
	public Space(String file, int firePos) {
		try {
			if(file=="Random") throw new Exception();
			
			trees_ = FieldMap.getMap(file);
			width_ = FieldMap.width_;
			height_ = FieldMap.height_;
			
			agents_ = new Object2DTorus(width_, height_);
		} catch (Exception e) {
			e.printStackTrace();
			
			trees_ = new Object2DTorus(width_, height_);
			agents_ = new Object2DTorus(width_, height_);
			build();
		}
		
		toFieldMap();
		
		double little= 0.2;
		double middle= 0.5;
		double big=0.8;
		
		int firePosX=1,firePosY=1;
		switch (firePos) {
		case 0:
			firePosX = (int) (width_*little);
			firePosY = (int) (height_*little);
			break;
		case 1:
			firePosX = (int) (width_*middle);
			firePosY = (int) (height_*little);
			break;
		case 2:
			firePosX = (int) (width_*big);
			firePosY = (int) (height_*little);
			break;
		case 3:
			firePosX = (int) (width_*big);
			firePosY = (int) (height_*middle);
			break;
		case 4:
			firePosX = (int) (width_*big);
			firePosY = (int) (height_*big);
			break;
		case 5:
			firePosX = (int) (width_*middle);
			firePosY = (int) (height_*big);
			break;
		case 6:
			firePosX = (int) (width_*little);
			firePosY = (int) (height_*big);
			break;
		case 7:
			firePosX = (int) (width_*little);
			firePosY = (int) (height_*middle);
			break;
		case 8:
			firePosX = (int) (width_*middle);
			firePosY = (int) (height_*middle);
			break;
		default:
			break;
		}
		
		System.out.println(firePos+" "+firePosX+" "+firePosY);
		
		startFire(firePosX,firePosY);
		firefighters_ = new ArrayList<FirefighterAgent>();
	}

	public void configureWind(int windDirection, int windStrength){
		wind_ = new WindDirection(windDirection, windStrength);
	}
	
	void startFire(int x,int y) {
		FireManipulator fire_agent = new FireManipulator(x, y, this);
		//fire_agent.fire_intensity_ = 1;
		agents_.putObjectAt(x, y, fire_agent);
	}

	void toFieldMap(){
		String s = new String();
		s += width_ + " " + height_ + "\n";
		for(int y=0; y<height_; ++y){
			for(int x=0; x<width_; ++x){
				s+=((Tree)trees_.getObjectAt(x, y)).density;
			}
			s+="\n";
		}
		
		try{
			 
			  FileWriter fstream = new FileWriter("out.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  System.out.println(s);
			  out.write(s);
			  out.close();
			  }catch (Exception e){  }
			  
		
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
		
		for(int i=20; i<180; i++)
			trees_.putObjectAt(100, i, new Tree(100, i,  0));
		
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
			
			e.printStackTrace();
		} */
	}

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
				this.mapColor(0, new Color(255, 255, 255));
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
		return new Object2DDisplay(agents_);
	}

	public void step() {
		// build();//discoteca
		for(int i = 0; i < firefighters_.size(); ++i)
			firefighters_.get(i).step();
	}

	public void fireStep() {
		ArrayList<FireManipulator> agents = new ArrayList<FireManipulator>();
		BaseMatrix m = agents_.getMatrix();
		for (int i = 0; i < width_; ++i)
			for (int j = 0; j < height_; ++j)
				if (m.get(i,j) != null && m.get(i, j).getClass().equals(FireManipulator.class))
					agents.add((FireManipulator) m.get(i, j));

		// SimUtilities.shuffle( agents );
		int siz = agents.size();

		FireManipulator agent;
		for (int i = 0; i < siz; ++i) {
			agent = agents.get(i);
			if (agent.fire_intensity_ <= 0) {
				agents_.putObjectAt(agent.x_, agent.y_, null);
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
		
		temp.add(agents_.getObjectAt(x, y - 1) == null && y != 0 ? (Tree) trees_
				.getObjectAt(x, y - 1) : null);
		
		if(eightDirections)
			temp.add(agents_.getObjectAt(x + 1, y - 1) == null && x != width_ - 1 && y != 0 ? (Tree) trees_
				.getObjectAt(x + 1, y - 1) : null);
		
		temp.add(agents_.getObjectAt(x + 1, y) == null && x != width_ - 1 ? (Tree) trees_
				.getObjectAt(x + 1, y) : null);
		
		if(eightDirections)
			temp.add(agents_.getObjectAt(x + 1, y + 1) == null && x != width_ - 1
				&& y != height_ - 1 ? (Tree) trees_.getObjectAt(x + 1, y + 1) : null);
		
		temp.add(agents_.getObjectAt(x, y + 1) == null && y != height_ - 1 ? (Tree) trees_
				.getObjectAt(x, y + 1) : null);
		
		if(eightDirections)
			temp.add(agents_.getObjectAt(x - 1, y + 1) == null && x != 0 && y != height_ - 1 ? (Tree) trees_
				.getObjectAt(x - 1, y + 1) : null);
		
		temp.add(agents_.getObjectAt(x - 1, y) == null && x != 0 ? (Tree) trees_
				.getObjectAt(x - 1, y) : null);
		
		if(eightDirections)
		temp.add(agents_.getObjectAt(x - 1, y - 1) == null && x != 0 && y != 0 ? (Tree) trees_
				.getObjectAt(x - 1, y - 1) : null);

		
		int siz= eightDirections ? 8 : 4;
		
		
		ArrayList<Tree> result = new ArrayList<Tree>(siz);
		for (int i = 0; i < siz; ++i) {
			result.add(null);
		}
		
		for (int val = 0; val < siz; ++val) {
			int a = (val - (wind_.value() - 1) + siz) % siz;
			Tree t = temp.get(val);
			result.set(a, t);
		}

		return result;
	}
}
