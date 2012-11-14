package strategy;

import java.awt.Point;
import java.util.ArrayList;

import environment.FireFighterModel;
import environment.Space;

public abstract class Strategy {

	public abstract ArrayList<Point> determineFightersPos(FireFighterModel model, boolean[][] sight, int num_trucks, int num_firemen);
	
	public ArrayList<Point> determineTruckPos(FireFighterModel model, boolean[][] sight, ArrayList<Point> fighters, int num_trucks) {
		return null;
	}
}
