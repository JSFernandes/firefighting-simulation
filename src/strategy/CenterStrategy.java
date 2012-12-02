package strategy;

import java.awt.Point;
import java.util.ArrayList;

import units.Constants;

import environment.FireFighterModel;
import environment.Space;
import environment.WindDirection;

public class CenterStrategy extends Strategy {

	Line determineBestCenter(Point center, Line[] center_array, WindDirection dir) {
		ArrayList<Line> candidates = new ArrayList<Line>();
		candidates.add(center_array[0]);
		double min_distance = center.distance(center_array[0].center_);
		double distance = 0;
		for(int i = 1; i < center_array.length; ++i) {
			distance = center.distance(center_array[i].center_);
			if(distance < min_distance) {
				min_distance = distance;
				candidates.clear();
				candidates.add(center_array[i]);
			}
			else if(distance == min_distance) {
				//System.out.println("what are the odds");
				candidates.add(center_array[i]);
			}
		}
		//System.out.println(candidates.size());
		//System.out.println(min_distance);
		if(candidates.size() == 1) {
			return candidates.get(0);
		}
		//rarely gets here
		int best_candidate = 0;
		// N or NE or NW
		if(dir.value() == 0 || dir.value() == 1 || dir.value() == 7) {
			int min_y = candidates.get(0).center_.y;
			for(int i = 1; i < candidates.size(); ++i) {
				if(candidates.get(i).center_.y < min_y) {
					min_y = candidates.get(i).center_.y;
					best_candidate = i;
				}
			}
		}
		// E
		else if(dir.value() == 2) {
			int max_x = candidates.get(0).center_.x;
			for(int i = 1; i < candidates.size(); ++i) {
				if(candidates.get(i).center_.x > max_x) {
					max_x = candidates.get(i).center_.x;
					best_candidate = i;
				}
			}
		}
		// S or SE or SW
		else if(dir.value() == 4 || dir.value() == 3 || dir.value() == 5) {
			int max_y = candidates.get(0).center_.y;
			for(int i = 1; i < candidates.size(); ++i) {
				if(candidates.get(i).center_.y > max_y) {
					max_y = candidates.get(i).center_.y;
					best_candidate = i;
				}
			}
		}
		// W
		else if(dir.value() == 6) {
			int min_x = candidates.get(0).center_.x;
			for(int i = 1; i < candidates.size(); ++i) {
				if(candidates.get(i).center_.x < min_x) {
					min_x = candidates.get(i).center_.x;
					best_candidate = i;
				}
			}
		}
		
		return candidates.get(best_candidate);
	}
	
	@Override
	public ArrayList<ArrayList<Point>> determineFightersPos(FireFighterModel model,
			boolean[][] sight, int num_firemen) {
		FireExtremes extremes = new FireExtremes(model);
		
		Point center = new Point((extremes.max_x_ + extremes.min_x_)/2, (extremes.max_y_ + extremes.min_y_)/2);
		
		Line top_line = new Line();
		top_line.center_ = new Point(center.x, extremes.min_y_ - Constants.SAFE_DISTANCE);
		top_line.x_change_ = 1;
		top_line.y_change_ = 0;
		top_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(extremes.max_x_ - extremes.min_x_, 2))/num_firemen);
		
		Line left_line = new Line();
		left_line.center_ = new Point(extremes.min_x_ - Constants.SAFE_DISTANCE, center.y);
		left_line.x_change_ = 0;
		left_line.y_change_ = 1;
		left_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(extremes.max_y_ - extremes.min_y_, 2))/num_firemen);
		
		Line right_line = new Line();
		right_line.center_ = new Point(extremes.max_x_ + Constants.SAFE_DISTANCE, center.y);
		right_line.x_change_ = 0;
		right_line.y_change_ = 1;
		right_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(extremes.max_y_ - extremes.min_y_, 2))/num_firemen);
		
		Line bottom_line = new Line();
		bottom_line.center_ = new Point(center.x, extremes.max_y_ + Constants.SAFE_DISTANCE);
		bottom_line.x_change_ = 1;
		bottom_line.y_change_ = 0;
		bottom_line.firemen_distance_ = (int) (Math.sqrt(Math.pow(extremes.max_x_ - extremes.min_x_, 2))/num_firemen);
		
		Line[] lines = {top_line, left_line, right_line, bottom_line};
		
		Line best_line = determineBestCenter(center, lines, model.space_.wind_);
		System.out.println("sup " + best_line.center_);
		//System.out.println("center " + center);
		Line[] line_array = {best_line};
		
		return attributeFighters(num_firemen, line_array, model.space_.width_, model.space_.height_, extremes);
	}



}
