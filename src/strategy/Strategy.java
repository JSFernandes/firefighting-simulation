package strategy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import environment.Space;

public abstract class Strategy {

	public abstract ArrayList<Point2D> determineFightersPos(Space space, boolean[][] sight, int num_trucks, int num_firemen);
	
	public ArrayList<Point2D> determineTruckPos(Space space, boolean[][] sight, ArrayList<Point2D> fighters, int num_trucks) {
		return null;
	}
}
