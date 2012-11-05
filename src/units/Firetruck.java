package units;
import environment.FireFighterModel;
import environment.Space;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;


public class Firetruck extends FirefightUnit implements Drawable {
	
	public Firetruck(int x, int y, Space space, Commander leader) {
		super(x, y, space, leader);
		// TODO Auto-generated constructor stub
	}

	Firefighter[] dependant_fighters_;

	@Override
	public void draw(SimGraphics arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}

}
