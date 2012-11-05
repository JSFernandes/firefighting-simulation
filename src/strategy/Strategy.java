package strategy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import environment.FireFighterModel;
import environment.Space;

public abstract class Strategy {

	public abstract ArrayList<Point2D> determineFightersPos(FireFighterModel model, boolean[][] sight, int num_trucks, int num_firemen);
	
	public ArrayList<Point2D> determineTruckPos(FireFighterModel model, boolean[][] sight, ArrayList<Point2D> fighters, int num_trucks) {
		return null;
	}
}
