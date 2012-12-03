package units;
import java.awt.Point;
import java.util.ArrayList;

import command.Command;

import environment.FireFighterModel;
import environment.Space;
import environment.Zone;
import strategy.Strategy;
import uchicago.src.sim.engine.Stepable;


public class CommanderAgent implements Stepable {
	public FirefighterAgent[] units_;
	
	FireFighterModel mod_;
	
	Strategy strat_;
	
	ArrayList<Squad> squads_;
	
	int time_to_decision_ = 100;
	
	public CommanderAgent(FirefighterAgent[] units, FireFighterModel mod, Strategy strat) {
		units_ = units;
		mod_ = mod;
		strat_ = strat;
		
		squads_ = new ArrayList<Squad>();
	}
	
	public void decision() {
		ArrayList<ArrayList<Point>> pos = strat_.determineFightersPos(mod_, units_.length);
		Squad sq;
		int current_u = 0;
		for(int x = 0; x < pos.size(); ++x) {
			sq = new Squad();
			squads_.add(sq);
			for(int i = 0; i < pos.get(x).size(); ++i) {
				//System.out.println(pos.get(i));
				units_[current_u].orders_.add(new Command(new Zone(pos.get(x).get(i))));
				units_[current_u].current_order_ = null;
				units_[current_u].moving_ = false;
				units_[current_u].squad_ = sq;
				sq.firemen_.add(units_[current_u]);
				++current_u;
			}
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
	}
	
	public Point findPointInNeed(FirefighterAgent agent) {
		Point need_point = null;
		for(int i = 0; i < squads_.size(); ++i) {
			need_point = squads_.get(i).findPointInNeed(agent);
			if(need_point != null)
				return need_point;
		}
		return null;
	}

	public void configureStrategy(Strategy s) {
		strat_ = s;
	}

}
