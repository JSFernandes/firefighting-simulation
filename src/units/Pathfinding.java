package units;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import environment.Space;


public class Pathfinding {
	
	double heuristic(Point2D source, Point2D target) {
		return source.distance(target);
	}
	
	private int getMinScore(ArrayList<Point2D> points, HashMap<Point2D, Double> scores) {
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
	
	boolean validatePoint(Point2D point, Space space) {
		double x = point.getX();
		double y = point.getY();
		return x >= 0 && 
			x < space.width_
			&& y >= 0 && y < space.height_
			&& (space.agents_.getObjectAt((int)x, (int)y) == null);
		
	}
	
	void reconstructPath(Point2D current_point, HashMap<Point2D, Point2D> came_from, ArrayList<Point2D> path) {
		Point2D prev = came_from.get(current_point);
		if(prev != null) {
			path.add(prev);
			reconstructPath(prev, came_from, path);
		}
	}
	
	Point2D[] findPath(Point2D source, Point2D target, Space space) {
		
		ArrayList<Point2D> closed_set = new ArrayList<Point2D>();
		ArrayList<Point2D> open_set = new ArrayList<Point2D>();
		HashMap<Point2D, Point2D> came_from = new HashMap<Point2D, Point2D>();
		
		open_set.add(source);
		
		HashMap<Point2D, Double> g_score = new HashMap<Point2D, Double>();
		HashMap<Point2D, Double> h_score = new HashMap<Point2D, Double>();
		HashMap<Point2D, Double> f_score = new HashMap<Point2D, Double>();
		
		g_score.put(source, 0.0);
		h_score.put(source, heuristic(source, target));
		f_score.put(source, h_score.get(source));
		
		int current_index;
		Point2D current_point, next_point;
		Point2D[] neighbours = new Point2D[8];
		boolean update = false;
		while(!open_set.isEmpty()) {
			current_index = getMinScore(open_set, f_score);
			current_point = open_set.get(current_index);
			if(current_point.equals(target)) {
				ArrayList<Point2D> path = new ArrayList<Point2D>();
				reconstructPath(current_point, came_from, path);
				Collections.reverse(path);
				return (Point2D[]) path.toArray(new Point2D[path.size()]);
			}
			
			open_set.remove(current_index);
			closed_set.add(current_point);
			
			neighbours[0] = new Point2D.Double(current_point.getX()+1, current_point.getY());
			neighbours[1] = new Point2D.Double(current_point.getX()-1, current_point.getY());
			neighbours[2] = new Point2D.Double(current_point.getX(), current_point.getY()+1);
			neighbours[3] = new Point2D.Double(current_point.getX(), current_point.getY()-1);
			neighbours[4] = new Point2D.Double(current_point.getX()+1, current_point.getY()+1);
			neighbours[5] = new Point2D.Double(current_point.getX()+1, current_point.getY()-1);
			neighbours[6] = new Point2D.Double(current_point.getX()-1, current_point.getY()+1);
			neighbours[7] = new Point2D.Double(current_point.getX()-1, current_point.getY()-1);
			
			for(int i = 0; i < neighbours.length; ++i) {
				next_point = neighbours[i];
				if(validatePoint(next_point, space)) {
					if(!closed_set.contains(next_point)) {
						if(!open_set.contains(next_point)) {
							open_set.add(next_point);
							h_score.put(next_point, heuristic(next_point, target));
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
