package environment;

import java.awt.Point;
import units.Constants;

public class Zone {
	public Point center_;
	public static final int radius_ = Constants.DEFAULT_WATER_RANGE;
	
	public boolean isInZone(Point p) {
		return (p.distance(center_) < radius_);
	}
	
	public Zone(Point c) {
		center_ = c;
	}
}
