package environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;


import strategy.CenterStrategy;
import strategy.FlankingStrategy;
import strategy.FrontStrategy;
import strategy.QuadStrategy;
import uchicago.src.reflector.ListPropertyDescriptor;
import uchicago.src.reflector.PropertyWidget;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import units.CommanderAgent;
import units.FirefighterAgent;

public class FireFighterModel extends SimModelImpl {

	protected DisplaySurface display_surface_;

	protected Schedule schedule_;
	public Space space_;
	public ArrayList<FireAgent> fire_agents_;
	protected Object2DDisplay agent_display_;
	CommanderAgent com_;

	//PARAMS
	private int windDirection = 0;
	private int windStrength = 2;
	public static int spreadMultiplier = 3;
	public static int burnMultiplier = 3;
	
	public static void main(String[] args) {
		SimInit init = new SimInit();
		FireFighterModel model = new FireFighterModel();
		init.loadModel(model, "", false);
	}

	public FireFighterModel() {
		
	}
	
	private void configureWind(){
		if (space_ != null)
			space_.configureWind(windDirection,windStrength);
	}
	
	public void begin() {
		buildModel();
		buildDisplay();
		buildSchedule();
		
		configureWind();
		
		display_surface_.display();
	}

	private void buildSchedule() {
		schedule_.scheduleActionBeginning(0, this, "step");
		 BasicAction b = new BasicAction() {
		 public void execute() {
				space_.fireStep();
		 }
		 };
		 schedule_.scheduleActionAtInterval(7,b);
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
		com_ = new CommanderAgent(null, this, new FrontStrategy());
		space_.agents_.putObjectAt(30, 30, new FirefighterAgent(new Point(30, 30), space_, com_));
		space_.agents_.putObjectAt(30, 31, new FirefighterAgent(new Point(30, 31), space_, com_));
		space_.agents_.putObjectAt(31, 31, new FirefighterAgent(new Point(31, 31), space_, com_));
		space_.agents_.putObjectAt(31, 30, new FirefighterAgent(new Point(31, 30), space_, com_));
		space_.agents_.putObjectAt(30, 29, new FirefighterAgent(new Point(30, 29), space_, com_));
		space_.firefighters_.add((FirefighterAgent) space_.agents_.getObjectAt(30, 30));
		space_.firefighters_.add((FirefighterAgent) space_.agents_.getObjectAt(30, 31));
		space_.firefighters_.add((FirefighterAgent) space_.agents_.getObjectAt(31, 31));
		space_.firefighters_.add((FirefighterAgent) space_.agents_.getObjectAt(31, 30));
		space_.firefighters_.add((FirefighterAgent) space_.agents_.getObjectAt(30, 29));
		com_.units_ = space_.firefighters_.toArray(new FirefighterAgent[space_.firefighters_.size()]);
	}

	private void initParamDropdowns(){
		Hashtable<Integer,String> h1 = new Hashtable<Integer,String>();
		h1.put(new Integer(0), "North");
		h1.put(new Integer(1), "NorthEast");
		h1.put(new Integer(2), "East");
		h1.put(new Integer(3), "SouthEast");
		h1.put(new Integer(4), "South");
		h1.put(new Integer(5), "SouthWest");
		h1.put(new Integer(6), "West");
		h1.put(new Integer(7), "NorthWest");
		ListPropertyDescriptor pd = new ListPropertyDescriptor("WindDirection", h1);
		
		
		descriptors.put("WindDirection", pd);
//		System.out.println(pd.getWidget().getValue());
//		pd.getWidget().setValue(5);
//		System.out.println(pd.getWidget().getValue());
		
	}
	
	public String[] getInitParam() {
		String[] params = { "WindDirection", "WindStrength", "SpreadMultiplier", "BurnMultiplier" };	
		
		return params;
	}
	
	public void setBurnMultiplier(int a){
		burnMultiplier = a;
	}
	
	public int getBurnMultiplier(){
		return burnMultiplier;
	}
	
	public void setSpreadMultiplier(int a){
		spreadMultiplier = a;
	}
	
	public int getSpreadMultiplier(){
		return spreadMultiplier;
	}
	
	public void setWindDirection(int a){
		windDirection = a;
		configureWind();
	}
	
	public int getWindDirection(){
		return windDirection;
	}
	
	public void setWindStrength(int a){
		windStrength =  a;
		configureWind();
		
	}
	
	public int getWindStrength(){
		return windStrength;
	}

	public String getName() {
		return "FireFighter";
	}

	public Schedule getSchedule() {
		return schedule_;
	}

	public void setup() {
		initParamDropdowns();
		
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
		//if(getTickCount() < 20)
		//	space_.fireStep();
		space_.step();
		com_.step();
		// agentDisplay = space.getAgentDisplay();
		display_surface_.updateDisplay();
	}
}
