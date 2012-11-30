package strategy;

import java.awt.Point;
import java.util.ArrayList;

import environment.FireFighterModel;
import environment.Space;
import environment.WindDirection;

public class FlankingStrategy extends Strategy {
	
	Line[] determineFlankLines (FireExtremes xtr, WindDirection dir, int num_firemen) {
		Line[] lines = new Line[2];
		lines[0] = new Line();
		lines[1] = new Line();
		int men_per_line = num_firemen/2;
		
		//N or S
		if(dir.value() == 0 || dir.value() == 4) {
			lines[0].center_ = new Point(xtr.min_x_ - units.Constants.SAFE_DISTANCE,(xtr.min_y_ + xtr.max_y_)/2);
			lines[0].x_change_ = 0;
			lines[0].y_change_ = 1;
			lines[0].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/men_per_line);
			
			lines[1].center_ = new Point(xtr.max_x_ + units.Constants.SAFE_DISTANCE,(xtr.min_y_ + xtr.max_y_)/2);
			lines[1].x_change_ = 0;
			lines[1].y_change_ = 1;
			lines[1].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/men_per_line);
		}
		// NE or SW
		else if(dir.value() == 1 || dir.value() == 5) {
			lines[0].center_ = new Point(xtr.max_x_ + units.Constants.SAFE_DISTANCE/2, xtr.max_y_ + units.Constants.SAFE_DISTANCE/2);
			lines[0].x_change_ = 1;
			lines[0].y_change_ = -1;
			lines[0].firemen_distance_ = 1;
			
			lines[1].center_ = new Point(xtr.min_x_ - units.Constants.SAFE_DISTANCE/2, xtr.min_y_ - units.Constants.SAFE_DISTANCE/2);
			lines[1].x_change_ = 1;
			lines[1].y_change_ = -1;
			lines[1].firemen_distance_ = 1;
		}
		// E or W
		else if(dir.value() == 2 || dir.value() == 6) {
			lines[0].center_ = new Point((xtr.min_x_ + xtr.max_x_)/2, xtr.min_y_ - units.Constants.SAFE_DISTANCE);
			lines[0].x_change_ = 1;
			lines[0].y_change_ = 0;
			lines[0].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/men_per_line);
			
			lines[1].center_ = new Point((xtr.min_x_ + xtr.max_x_)/2, xtr.max_y_ + units.Constants.SAFE_DISTANCE);
			lines[1].x_change_ = 1;
			lines[1].y_change_ = 0;
			lines[1].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/men_per_line);
		}
		// SE or NW
		else if(dir.value() == 3 || dir.value() == 7) {
			lines[0].center_ = new Point(xtr.max_x_ + units.Constants.SAFE_DISTANCE/2, xtr.min_y_ - units.Constants.SAFE_DISTANCE/2);
			lines[0].x_change_ = 1;
			lines[0].y_change_ = 1;
			lines[0].firemen_distance_ = 1;
			
			lines[1].center_ = new Point(xtr.min_x_ - units.Constants.SAFE_DISTANCE/2, xtr.max_y_ + units.Constants.SAFE_DISTANCE/2);
			lines[1].x_change_ = 1;
			lines[1].y_change_ = 1;
			lines[1].firemen_distance_ = 1;
		}
		
		return lines;
	}

	@Override
	public ArrayList<Point> determineFightersPos(FireFighterModel model,
			boolean[][] sight, int num_firemen) {
		FireExtremes extremes = new FireExtremes(model);
		
		Line[] lines = determineFlankLines(extremes, model.space_.wind_, num_firemen);
		
		ArrayList<Point> first_line = attributeFighters(num_firemen/2, lines[0]);
		ArrayList<Point> second_line = attributeFighters(num_firemen/2, lines[1]);
		
		first_line.addAll(second_line);
		
		return first_line;
	}

}
