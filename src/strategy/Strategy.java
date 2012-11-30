package strategy;

import java.awt.Point;
import java.util.ArrayList;

import environment.FireFighterModel;
import environment.Space;

public abstract class Strategy {

	public abstract ArrayList<Point> determineFightersPos(FireFighterModel model, boolean[][] sight, int num_firemen);
	
	ArrayList<Point> attributeFighters(int num_firemen, Line line) {
		ArrayList<Point> positions = new ArrayList<Point>();
		int allocated_men = 1;
		positions.add(line.center_);
		
		while(allocated_men < num_firemen) {
			positions.add(new Point(line.center_.x + line.x_change_ * allocated_men * line.firemen_distance_, 
					line.center_.y + line.y_change_ * allocated_men * line.firemen_distance_));
			line.x_change_ *= -1;
			line.y_change_ *= -1;
			++allocated_men;
		}
		return positions;
	}
}
