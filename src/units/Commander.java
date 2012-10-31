package units;
import environment.Space;
import strategy.Strategy;
import uchicago.src.sim.engine.Stepable;


public class Commander implements Stepable {
	Firetruck[] trucks_;
	Firefighter[] units_;
	
	Space space_;
	boolean[][] sight_;
	
	Strategy strat_;
	
	@Override
	public void step() {
		// TODO Auto-generated method stub
	}

}
