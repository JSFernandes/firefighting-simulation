package units;
import environment.FireFighterModel;
import environment.Space;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;


public class Firefighter extends FirefightUnit implements Drawable {
	public Firefighter(int x, int y, Space space, Commander leader) {
		super(x, y, space, leader);
		// TODO Auto-generated constructor stub
	}

	Firetruck connected_truck_;

	@Override
	public void draw(SimGraphics g) {
		g.drawFastRoundRect(java.awt.Color.yellow);
	}

	@Override
	public int getX() {
		return super.x_;
	}

	@Override
	public int getY() {
		return super.y_;
	}


}
