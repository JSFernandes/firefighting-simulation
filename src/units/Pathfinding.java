package units;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import environment.FireAgent;
import environment.Space;
import environment.Zone;


public class Pathfinding {
	
	double heuristic(Point source, Point target) {
		return source.distance(target);
	}
	
	private int getMinScore(ArrayList<Point> points, HashMap<Point, Double> scores) {
		int min_point = 0;
		Double min_score = Double.MAX_VALUE;
		for(int i = 0; i < points.size(); ++i) {
			if(scores.get(points.get(i)) < min_score) {
				min_point = i;
				min_score = scores.get(points.get(i));
			}
		}
		return min_point;
	}
	
	FireAgent fireInRange(Point point, Space space, int range, boolean shuffle) {
		Vector<Object> agents = space.agents_.getMooreNeighbors((int)point.x, (int)point.y, range, range, false);
		if(shuffle)
			Collections.shuffle(agents);
		for(int i = 0; i < agents.size(); ++i) {
			if(agents.get(i).getClass() == FireAgent.class) {
				return (FireAgent) agents.get(i);
			}
		}
		return null;
	}
	
	boolean validatePoint(Point point, Space space) {
		double x = point.x;
		double y = point.y;
		return x >= 0 && 
			x < space.width_
			&& y >= 0 && y < space.height_;
			//&& (space.agents_.getObjectAt((int)x, (int)y) == null);
			//&& (fireInRange(point, space, Constants.MOVE_SAFE_DISTANCE, false) == null);
		
	}
	
	void reconstructPath(Point current_point, HashMap<Point, Point> came_from, ArrayList<Point> path) {
		Point prev = came_from.get(current_point);
		if(prev != null) {
			path.add(prev);
			reconstructPath(prev, came_from, path);
		}
	}
	
	Point[] findPath(Point source, Zone target, Space space) {
		ArrayList<Point> closed_set = new ArrayList<Point>();
		ArrayList<Point> open_set = new ArrayList<Point>();
		HashMap<Point, Point> came_from = new HashMap<Point, Point>();
		
		open_set.add(source);
		
		HashMap<Point, Double> g_score = new HashMap<Point, Double>();
		HashMap<Point, Double> h_score = new HashMap<Point, Double>();
		HashMap<Point, Double> f_score = new HashMap<Point, Double>();
		
		g_score.put(source, 0.0);
		h_score.put(source, heuristic(source, target.center_));
		f_score.put(source, h_score.get(source));
		
		int current_index;
		Point current_point, next_point;
		Point[] neighbours = new Point[8];
		boolean update = false;
		while(!open_set.isEmpty()) {
			current_index = getMinScore(open_set, f_score);
			current_point = open_set.get(current_index);
			if(current_point.equals(target.center_)) {
				ArrayList<Point> path = new ArrayList<Point>();
				reconstructPath(current_point, came_from, path);
				Collections.reverse(path);
				return (Point[]) path.toArray(new Point[path.size()]);
			}
			
			open_set.remove(current_index);
			closed_set.add(current_point);
			
			neighbours[0] = new Point(current_point.x +1, current_point.y);
			neighbours[1] = new Point(current_point.x-1, current_point.y);
			neighbours[2] = new Point(current_point.x, current_point.y+1);
			neighbours[3] = new Point(current_point.x, current_point.y-1);
			neighbours[4] = new Point(current_point.x+1, current_point.y+1);
			neighbours[5] = new Point(current_point.x+1, current_point.y-1);
			neighbours[6] = new Point(current_point.x-1, current_point.y+1);
			neighbours[7] = new Point(current_point.x-1, current_point.y-1);
			
			for(int i = 0; i < neighbours.length; ++i) {
				next_point = neighbours[i];
				if(validatePoint(next_point, space)) {
					if(!closed_set.contains(next_point)) {
						if(!open_set.contains(next_point)) {
							open_set.add(next_point);
							h_score.put(next_point, heuristic(next_point, target.center_));
							update = true;
						}
						else if(g_score.get(current_point) + 1 < g_score.get(next_point))
							update = true;
						else
							update = false;
						
						if(update) {
							came_from.put(next_point, current_point);
							g_score.put(next_point, g_score.get(current_point)+1);
							f_score.put(next_point, g_score.get(next_point) + h_score.get(next_point));
						}
					}
				}
			}
		}
		
		return null;
	}

}
