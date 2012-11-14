package strategy;

import java.awt.Point;
import java.util.ArrayList;

import environment.FireAgent;
import environment.FireFighterModel;
import environment.Space;
import environment.WindDirection;

public class FrontStrategy extends Strategy {

	Point[] determineLineEdges(int max_x, int min_x, int min_y, int max_y, WindDirection dir) {
		Point[] edges = new Point[2];
		edges[0] = new Point();
		edges[1] = new Point();
		// N
		if(dir.value() == 0) {
			edges[0].setLocation(min_x, min_y - units.Constants.SAFE_DISTANCE);
			edges[1].setLocation(max_x, min_y - units.Constants.SAFE_DISTANCE);
		}
		// NE
		else if(dir.value() == 1) {
			edges[0].setLocation(min_x - units.Constants.SAFE_DISTANCE/2, min_y + units.Constants.SAFE_DISTANCE/2);
			edges[1].setLocation(min_x + units.Constants.SAFE_DISTANCE/2, min_y - units.Constants.SAFE_DISTANCE/2);
		}
		// E
		else if(dir.value() == 2) {
			edges[0].setLocation(min_x - units.Constants.SAFE_DISTANCE, min_y);
			edges[1].setLocation(min_x - units.Constants.SAFE_DISTANCE, max_y);
		}
		// SE
		else if(dir.value() == 3) {
			edges[0].setLocation(min_x - units.Constants.SAFE_DISTANCE/2, max_y - units.Constants.SAFE_DISTANCE/2);
			edges[1].setLocation(min_x + units.Constants.SAFE_DISTANCE/2, max_y + units.Constants.SAFE_DISTANCE/2);
			
		}
		// S
		else if(dir.value() == 4) {
			edges[0].setLocation(min_x, max_y + units.Constants.SAFE_DISTANCE);
			edges[1].setLocation(max_x, max_y + units.Constants.SAFE_DISTANCE);
		}
		// SW
		else if(dir.value() == 5) {
			edges[0].setLocation(max_x - units.Constants.SAFE_DISTANCE/2, max_y + units.Constants.SAFE_DISTANCE/2);
			edges[1].setLocation(max_x + units.Constants.SAFE_DISTANCE/2, max_y - units.Constants.SAFE_DISTANCE/2);
		}
		// W
		else if(dir.value() == 6) {
			edges[0].setLocation(max_x + units.Constants.SAFE_DISTANCE, min_y);
			edges[1].setLocation(max_x + units.Constants.SAFE_DISTANCE, max_y);
		}
		// NW
		else if(dir.value() == 7) {
			edges[0].setLocation(max_x + units.Constants.SAFE_DISTANCE/2, min_y + units.Constants.SAFE_DISTANCE/2);
			edges[1].setLocation(min_x - units.Constants.SAFE_DISTANCE/2, max_y - units.Constants.SAFE_DISTANCE/2);
		}
		
		
		return edges;
	}
	
	
	ArrayList<Point> attributeFighters(int num_firemen, Point[] edges) {
		ArrayList<Point> fire_pos = new ArrayList<Point>();
		int edge_distance = (int)(edges[0].distance(edges[1]));
		double fireman_per_tile = ((double)num_firemen/(double)edge_distance);
		
		Point current_point = (Point) edges[0].clone();
		int increment_x =  (edges[1].x-edges[0].x);
		int increment_y =  (edges[1].y-edges[0].y);
		
		if(increment_x > 0)
			increment_x = 1;
		else if(increment_x < 0)
			increment_x = -1;
		else
			increment_x = 0;
		
		if(increment_y > 0)
			increment_y = 1;
		else if(increment_y < 0)
			increment_y = -1;
		else
			increment_y = 0;
		
		double current_fmen_count = 0;
		double prev_fmen_count = 0;
		while(current_point.getX() != edges[1].getX() || current_point.getY() != edges[1].getY()) {
			current_fmen_count += fireman_per_tile;
			if(Math.floor(current_fmen_count) > Math.floor(prev_fmen_count))
				fire_pos.add((Point)current_point.clone());
			prev_fmen_count = current_fmen_count;
			current_point.setLocation(current_point.getX()+increment_x, current_point.getY()+increment_y);
		}
		
		return fire_pos;
		
	}
	@Override
	public ArrayList<Point> determineFightersPos(FireFighterModel model,
			boolean[][] sight, int num_trucks, int num_firemen) {
		int max_x, max_y, min_x, min_y;
		max_x = max_y = 0;
		min_x = model.space_.width_;
		min_y = model.space_.height_;
		
		for(int x = 0; x < model.space_.agents_.getSizeX(); ++x) {
			for(int y = 0; y < model.space_.agents_.getSizeY(); ++y) {
				if(model.space_.agents_.getObjectAt(x, y) != null && model.space_.agents_.getObjectAt(x, y).getClass() == FireAgent.class) {
					if(x < min_x)
						min_x = x;
					if(x > max_x)
						max_x = x;
					if(y < min_y)
						min_y = y;
					if(y > max_y)
						max_y = y;
				}
			}
			
		}
		
		Point[] edges = determineLineEdges(max_x, min_x, min_y, max_y, model.space_.wind_);
		
		return attributeFighters(num_firemen, edges);
	}


}
