package strategy;

import java.awt.Point;
import java.util.ArrayList;

import units.Constants;

import environment.FireFighterModel;
import environment.Space;

public abstract class Strategy {

	public abstract ArrayList<ArrayList<Point>> determineFightersPos(FireFighterModel model, int num_firemen);
	
	ArrayList<ArrayList<Point>> attributeFighters(int num_firemen, Line[] lines, int width, int height, FireExtremes xtr) {
		ArrayList<ArrayList<Point>> positions_array = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> positions;
		int allocated_men, x_alloc, y_alloc;
		Line line;
		int[] firemen_per_line = new int[lines.length];
		int leftovers = num_firemen % lines.length;
		
		for(int i = 0; i < firemen_per_line.length; ++i) {
			firemen_per_line[i] = num_firemen/lines.length;
			if(leftovers > 0) {
				++firemen_per_line[i];
				--leftovers;
			}
		}
		
		for(int i = 0; i < lines.length; ++i) {
			line = lines[i];
			System.out.println("center: " + line.center_);
			if(line.center_.x < 0 || line.center_.y < 0 || line.center_.x >= width || line.center_.y >= height) {
				return replaceLine(line, width, height, num_firemen, xtr);
			}
			positions = new ArrayList<Point>();
			positions_array.add(positions);
			allocated_men = 1;
			positions.add(line.center_);
			while(allocated_men < firemen_per_line[i]) {
				x_alloc = line.center_.x + line.x_change_ * allocated_men * line.firemen_distance_;
				y_alloc = line.center_.y + line.y_change_ * allocated_men * line.firemen_distance_;
				positions.add(new Point(x_alloc, y_alloc));
				line.x_change_ *= -1;
				line.y_change_ *= -1;
				++allocated_men;
			}
		}
		return positions_array;
	}
	
	ArrayList<ArrayList<Point>> replaceLine(Line invalid_line, int width, int height, int num_firemen, FireExtremes xtr) {
		Line[] l = {new Line()};
		if(invalid_line.center_.x < 0) {
			l[0].center_ = new Point(xtr.max_x_ + Constants.SAFE_DISTANCE, (xtr.max_y_+xtr.min_y_)/2);
			l[0].x_change_ = 0;
			l[0].y_change_ = 1;
			l[0].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/num_firemen);
		}
		else if(invalid_line.center_.y < 0) {
			l[0].center_ = new Point((xtr.max_x_ + xtr.min_x_)/2, xtr.max_y_ + Constants.SAFE_DISTANCE);
			l[0].x_change_ = 1;
			l[0].y_change_ = 0;
			l[0].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/num_firemen);
		}
		else if(invalid_line.center_.x >= width) {
			l[0].center_ = new Point(xtr.min_x_ - Constants.SAFE_DISTANCE, (xtr.max_y_+xtr.min_y_)/2);
			l[0].x_change_ = 0;
			l[0].y_change_ = 1;
			l[0].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/num_firemen);
		}
		else if(invalid_line.center_.y >= height) {
			l[0].center_ = new Point((xtr.max_x_ + xtr.min_x_)/2, xtr.min_y_ - Constants.SAFE_DISTANCE);
			l[0].x_change_ = 1;
			l[0].y_change_ = 0;
			l[0].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/num_firemen);
		}
		
		return attributeFighters(num_firemen, l, width, height, xtr);
	}
}
