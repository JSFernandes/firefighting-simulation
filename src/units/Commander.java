package units;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import command.Command;
import command.CommandType;

import environment.FireFighterModel;
import environment.Space;
import strategy.Strategy;
import uchicago.src.sim.engine.Stepable;


public class Commander implements Stepable {
	Firetruck[] trucks_;
	public Firefighter[] units_;
	
	FireFighterModel mod_;
	boolean[][] sight_;
	
	Strategy strat_;
	
	int time_to_decision_ = 20;
	
	public Commander(Firefighter[] units, FireFighterModel mod, Strategy strat) {
		units_ = units;
		mod_ = mod;
		strat_ = strat;
		trucks_ = new Firetruck[0];
	}
	
	public void decision() {
		ArrayList<Point2D> pos = strat_.determineFightersPos(mod_, sight_, trucks_.length, units_.length);
		for(int i = 0; i < pos.size(); ++i) {
			units_[i].orders_.add(new Command(CommandType.MOVE, pos.get(i)));
			units_[i].orders_.add(new Command(CommandType.WATER, null));
		}
	}
	
	public void new_orders() {
		for(int i = 0; i <units_.length; ++i) {
			units_[i].current_order_ = null;
		}
		decision();
	}
	
	@Override
	public void step() {
		--time_to_decision_;
		if(time_to_decision_ == 0) {
			decision();
		}
		// TODO Auto-generated method stub
	}

}
