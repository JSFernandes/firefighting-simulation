package units;
import java.awt.geom.Point2D;

import environment.Space;


public class Pathfinding {
	
	Space space_;
	
	double heuristic(Point2D source, Point2D target) {
		return source.distance(target);
	}
	
	Point2D[] findPath(Point2D source, Point2D target, String unit_type) {
		return null;
	}

}
