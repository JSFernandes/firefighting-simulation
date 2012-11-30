package strategy;

import environment.FireAgent;
import environment.FireFighterModel;

public class FireExtremes {
	public int min_x_;
	public int min_y_;
	public int max_x_;
	public int max_y_;
	
	public FireExtremes(FireFighterModel model) {
		max_x_ = max_y_ = 0;
		min_x_ = model.space_.width_;
		min_y_ = model.space_.height_;
		
		for(int x = 0; x < model.space_.agents_.getSizeX(); ++x) {
			for(int y = 0; y < model.space_.agents_.getSizeY(); ++y) {
				if(model.space_.agents_.getObjectAt(x, y) != null && model.space_.agents_.getObjectAt(x, y).getClass() == FireAgent.class) {
					if(x < min_x_)
						min_x_ = x;
					if(x > max_x_)
						max_x_ = x;
					if(y < min_y_)
						min_y_ = y;
					if(y > max_y_)
						max_y_ = y;
				}
			}
		}
	}

}
