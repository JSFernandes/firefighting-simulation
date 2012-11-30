package strategy;

import java.awt.Point;
import java.util.ArrayList;

import environment.FireAgent;
import environment.FireFighterModel;
import environment.Space;
import environment.WindDirection;

public class FrontStrategy extends Strategy {

	Line determineLine(FireExtremes xtr, WindDirection dir, int num_firemen) {
		Line ret_line = new Line();
		// N
		if(dir.value() == 0) {
			ret_line.center_ = new Point((xtr.min_x_ + xtr.max_x_)/2, xtr.min_y_ - units.Constants.SAFE_DISTANCE);
			ret_line.x_change_ = 1;
			ret_line.y_change_ = 0;
			ret_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/num_firemen);
		}
		// NE
		else if(dir.value() == 1) {
			ret_line.center_ = new Point(xtr.min_x_ - units.Constants.SAFE_DISTANCE/2, xtr.min_y_ - units.Constants.SAFE_DISTANCE/2);
			ret_line.x_change_ = 1;
			ret_line.y_change_ = -1;
			ret_line.firemen_distance_ = 1;
		}
		// E
		else if(dir.value() == 2) {
			ret_line.center_ = new Point(xtr.min_x_ - units.Constants.SAFE_DISTANCE,(xtr.min_y_ + xtr.max_y_)/2);
			ret_line.x_change_ = 0;
			ret_line.y_change_ = 1;
			ret_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/num_firemen);
		}
		// SE
		else if(dir.value() == 3) {
			ret_line.center_ = new Point(xtr.min_x_ - units.Constants.SAFE_DISTANCE/2, xtr.max_y_ + units.Constants.SAFE_DISTANCE/2);
			ret_line.x_change_ = 1;
			ret_line.y_change_ = 1;
			ret_line.firemen_distance_ = 1;
		}
		// S
		else if(dir.value() == 4) {
			ret_line.center_ = new Point((xtr.min_x_ + xtr.max_x_)/2, xtr.max_y_ + units.Constants.SAFE_DISTANCE);
			ret_line.x_change_ = 1;
			ret_line.y_change_ = 0;
			ret_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/num_firemen);
		}
		// SW
		else if(dir.value() == 5) {
			ret_line.center_ = new Point(xtr.max_x_ + units.Constants.SAFE_DISTANCE/2, xtr.max_y_ + units.Constants.SAFE_DISTANCE/2);
			ret_line.x_change_ = 1;
			ret_line.y_change_ = -1;
			ret_line.firemen_distance_ = 1;
		}
		// W
		else if(dir.value() == 6) {
			ret_line.center_ = new Point(xtr.max_x_ + units.Constants.SAFE_DISTANCE, (xtr.min_y_ + xtr.max_y_)/2);
			ret_line.x_change_ = 0;
			ret_line.y_change_ = 1;
			ret_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/num_firemen);
		}
		// NW
		else if(dir.value() == 7) {
			ret_line.center_ = new Point(xtr.max_x_ + units.Constants.SAFE_DISTANCE/2, xtr.min_y_ - units.Constants.SAFE_DISTANCE/2);
			ret_line.x_change_ = 1;
			ret_line.y_change_ = 1;
			ret_line.firemen_distance_ = 1;
		}
		
		
		return ret_line;
	}
	
	@Override
	public ArrayList<Point> determineFightersPos(FireFighterModel model,
			boolean[][] sight, int num_firemen) {
		FireExtremes extremes = new FireExtremes(model);
		
		Line l = determineLine(extremes, model.space_.wind_, num_firemen);
		
		return attributeFighters(num_firemen, l);
	}


}
