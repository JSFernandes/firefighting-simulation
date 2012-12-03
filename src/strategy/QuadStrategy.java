package strategy;

import java.awt.Point;
import java.util.ArrayList;

import units.Constants;

import environment.FireFighterModel;
import environment.Space;

public class QuadStrategy extends Strategy {

	Line[] determineLines(FireExtremes xtr, int num_firemen) {
		Line[] lines = {new Line(), new Line(), new Line(), new Line()};
		int firemen_per_line = num_firemen/4;
		
		//linha de cima
		lines[0].center_ = new Point((xtr.min_x_+xtr.max_x_)/2, xtr.min_y_ - Constants.SAFE_DISTANCE);
		lines[0].x_change_ = 1;
		lines[0].x_change_ = 0;
		lines[0].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/firemen_per_line);
		
		//linha de baixo
		lines[1].center_ = new Point((xtr.min_x_+xtr.max_x_)/2, xtr.max_y_ + Constants.SAFE_DISTANCE);
		lines[1].x_change_ = 1;
		lines[1].x_change_ = 0;
		lines[1].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_x_ - xtr.min_x_, 2))/firemen_per_line);
		
		//linha da esquerda
		lines[2].center_ = new Point(xtr.min_x_ - Constants.SAFE_DISTANCE, (xtr.max_y_ + xtr.min_y_)/2);
		lines[2].x_change_ = 0;
		lines[2].y_change_ = 1;
		lines[2].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/firemen_per_line);
		
		//linha da direita
		lines[3].center_ = new Point(xtr.max_x_ + Constants.SAFE_DISTANCE, (xtr.max_y_ + xtr.min_y_)/2);
		lines[3].x_change_ = 0;
		lines[3].y_change_ = 1;
		lines[3].firemen_distance_ = (int) (Math.sqrt(Math.pow(xtr.max_y_ - xtr.min_y_, 2))/firemen_per_line);
		
		return lines;
	}
	
	public ArrayList<ArrayList<Point>> determineFightersPos(FireFighterModel model,int num_firemen) {
		FireExtremes extremes = new FireExtremes(model);
		Line[] lines = determineLines(extremes, num_firemen);
		System.out.println("no prob");
		return attributeFighters(num_firemen, lines, model.space_.width_, model.space_.height_, extremes);
	}


}
