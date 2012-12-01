package units;
import java.awt.Point;
import java.util.ArrayList;

import command.Command;
import command.CommandType;

import environment.FireFighterModel;
import environment.Space;
import environment.Zone;
import strategy.Strategy;
import uchicago.src.sim.engine.Stepable;


public class CommanderAgent implements Stepable {
	public FirefighterAgent[] units_;
	
	FireFighterModel mod_;
	boolean[][] sight_;
	
	Strategy strat_;
	
	int time_to_decision_ = 20;
	
	public CommanderAgent(FirefighterAgent[] units, FireFighterModel mod, Strategy strat) {
		units_ = units;
		mod_ = mod;
		strat_ = strat;
	}
	
	public void decision() {
		ArrayList<Point> pos = strat_.determineFightersPos(mod_, sight_, units_.length);
		for(int i = 0; i < pos.size(); ++i) {
			//System.out.println(pos.get(i));
			units_[i].orders_.add(new Command(CommandType.MOVE, new Zone(pos.get(i))));
			units_[i].current_order_ = null;
			units_[i].moving_ = false;
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
