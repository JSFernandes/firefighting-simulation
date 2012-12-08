package units;

import java.awt.Point;
import java.util.ArrayList;

import command.Command;
import environment.Zone;

public class Squad {
	ArrayList<FirefighterAgent> firemen_ = new ArrayList<FirefighterAgent>();
	
	public boolean isInRange(Point p) {
		for(int i = 0; i < firemen_.size(); ++i) {
			if(p.distance(firemen_.get(i).pos_) > Constants.MAX_SPREAD_RADIUS_PER_MEN*firemen_.size())
				return false;
		}
		return true;
	}
	
	public Point findPointInNeed(FirefighterAgent agent_asking) {
		for(int i = 0; i < firemen_.size(); ++i)
			if(firemen_.get(i).seesFire() && firemen_.get(i) != agent_asking)
				return firemen_.get(i).pos_;
		
		return null;
	}
	
	public void getThingsToDo(FirefighterAgent agent, CommanderAgent leader) {
		Point p = findPointInNeed(agent);
		if(p != null) {
			Point adjacent = new Point(p.x-1, p.y-1);
			agent.orders_.add(new Command(new Zone(adjacent)));
		}
		else {
			relocateSquad(leader);
		}
	}
	
	public void relocateSquad(CommanderAgent leader) {
		Point need_point = leader.findPointInNeed(null);
		for(int i = 0; i < firemen_.size(); ++i) {
			firemen_.get(i).orders_.add(new Command(new Zone(need_point)));
		}
	}
}
