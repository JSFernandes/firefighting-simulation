package environment;
import java.util.ArrayList;


import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;

public class FireFighterModel extends SimModelImpl {

	protected DisplaySurface display_surface_;

	protected Schedule schedule_;
	protected Space space_;
	protected ArrayList<FireAgent> fire_agents_;
	protected Object2DDisplay agent_display_;

	public static void main(String[] args) {
		SimInit init = new SimInit();
		FireFighterModel model = new FireFighterModel();
		init.loadModel(model, "", false);
	}

	public FireFighterModel() {
	}

	public void begin() {
		buildModel();
		buildDisplay();
		buildSchedule();

		display_surface_.display();
	}

	private void buildSchedule() {
		schedule_.scheduleActionBeginning(0, this, "step");
		// BasicAction b = new BasicAction() {
		// public void execute() {
		// space.addAgent();space.addAgent();
		// }
		// };
		// schedule.scheduleActionAtInterval(1,b);
	}

	private void buildDisplay() {

		Value2DDisplay space_display = space_.getSpaceDisplay();
		agent_display_ = space_.getAgentDisplay();

		display_surface_.addDisplayableProbeable(space_display, getName() + "Space");
		display_surface_.addDisplayableProbeable(agent_display_, getName() + "Agents");

		addSimEventListener(display_surface_);
	}

	private void buildModel() {
		space_ = new Space();
	}

	public String[] getInitParam() {
		String[] params = { "teste1", "teste2" };
		return params;
	}

	public String getName() {
		return "FireFighter";
	}

	public Schedule getSchedule() {
		return schedule_;
	}

	public void setup() {
		schedule_ = null;
		space_ = null;

		if (display_surface_ != null) {
			display_surface_.dispose();
		}
		
		display_surface_ = null;

		System.gc();

		schedule_ = new Schedule(1);

		display_surface_ = new DisplaySurface(this, getName());
		registerDisplaySurface(getName(), display_surface_);
	}

	public void step() {
		space_.fireStep();
		space_.step();
		// agentDisplay = space.getAgentDisplay();
		display_surface_.updateDisplay();
	}
}
