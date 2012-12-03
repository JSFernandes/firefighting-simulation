package environment;
import uchicago.src.sim.util.Random;


public class Tree extends java.lang.Number {
	private static final long serialVersionUID = 1L;

	public enum TreeState { 
		FRESH,
		BURNING,
		ASHES
	};
	
	public TreeState state;
	public int density;
	public int life, initLife;
	public int x, y;
	
	public Tree(int x, int y,int d) {
		this.x=x;
		this.y=y;
		density=d;
		initLife=40;
		life=new Integer(initLife);
		state=TreeState.FRESH;
		
		if(d==0)
			state=TreeState.ASHES;
	}

	public boolean isFresh(){
		return state==TreeState.FRESH;
	}
	
	public boolean isBurning(){
		return state==TreeState.BURNING;
	}
	
	public boolean isAshes(){
		return state==TreeState.ASHES;
	}
	
	public TreeState getState(){
		return state;
	}
	
	public void setState(TreeState st){
		state=st;
	}
	
	public boolean burn(double prob){
		if(isFresh()){
			
			//density 0-31
			//burnProb 0.4-0.6
			
			int v = Random.uniform.nextIntFromTo(0, 100);
			int multiplyer=FireFighterModel.burnMultiplier;

			if(v < density*multiplyer+prob)
				state=TreeState.BURNING;
			else
				return false;
		}
		else 
		{
			//int d = (life/initLife)*density;
			life-=(5-density)*FireFighterModel.spreadMultiplier;
			if(life<=0){
				state=TreeState.ASHES;
				density=5;
				return false;
			}
		}
		return true;
	}
	
	
	
	//for Value2DDisplay constructor
	public double doubleValue() {
		return density;
	}

	public float floatValue() {
		return density;
	}

	public int intValue() {
		return density;
	}

	public long longValue() {
		return density;
	}
	
	//for prints
	@Override
	public String toString() {
		
		return "x: "+x+" y: "+y;
	}

	public int percentage() {
		return (int)(100.0-(double)life/(double)initLife*100.0);
	}
	
}
