package environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;


import strategy.CenterStrategy;
import strategy.FlankingStrategy;
import strategy.FrontStrategy;
import strategy.QuadStrategy;
import strategy.Strategy;
import uchicago.src.reflector.ListPropertyDescriptor;
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
	public ArrayList<FireManipulator> fire_agents_;
	protected Object2DDisplay agent_display_;
	private CommanderAgent com_;
	private Strategy agentStrategy;

	//PARAMS
	private int windDirection = 0;
	private int windStrength = 2;
	private int strategy = 0;
	public static int spreadMultiplier = 3;
	public static int burnMultiplier = 3;
	private int fireTickRate = 22;
	private int agentTickRate = 2;
	private String mapFile = "map1.txt";
	private int firePosIndex = 3;
	private int agentPosIndex = 0;
	private int firePosX=1,firePosY=1;
	private int agentPosX=1,agentPosY=1;

	private int agentNumber=5;
	
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
	
	
	private void configureStrategy() {
		if(com_ != null)
			com_.configureStrategy(agentStrategy);
		
	}
	
	public void begin() {
		buildModel();
		buildDisplay();
		buildSchedule();
		
		configureWind();
		setExtinguishStrategy(strategy);
		configureStrategy();
		setAgentStartPos(agentPosIndex);
		
		display_surface_.display();
	}

	private void buildSchedule() {
		 schedule_.scheduleActionBeginning(0, this, "step");
		 BasicAction b = new BasicAction() {
		 public void execute() {
				space_.fireStep();
		 	}	
		 };
		 
		 schedule_.scheduleActionAtInterval(22,b);
		 
		 BasicAction c = new BasicAction() {
		 public void execute() {
			 	space_.step();
		 	}
		 };
		 schedule_.scheduleActionAtInterval(2, c);
	}

	private void buildDisplay() {

		Value2DDisplay space_display = space_.getSpaceDisplay();
		agent_display_ = space_.getAgentDisplay();

		display_surface_.addDisplayableProbeable(space_display, getName() + "Space");
		display_surface_.addDisplayableProbeable(agent_display_, getName() + "Agents");

		addSimEventListener(display_surface_);
	}

	private void buildModel() {
		
		space_ = new Space(mapFile,firePosIndex);
		com_ = new CommanderAgent(null, this, agentStrategy);
		calcAgentPos();
		buildAgents(agentNumber);
		com_.units_ = space_.firefighters_.toArray(new FirefighterAgent[space_.firefighters_.size()]);
	}
	
	void buildAgents(int n){
		int x,y;
		int line=8;
		for(int i=0; i<n; ++i){
			x=agentPosX+i%line*2;
			y=agentPosY+i/line*2;
			space_.agents_.putObjectAt(x, y, new FirefighterAgent(new Point(x, y), space_, com_));
			space_.firefighters_.add((FirefighterAgent) space_.agents_.getObjectAt(x, y));
		}
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
		
		Hashtable<Integer,String> h2 = new Hashtable<Integer,String>();
		h2.put(new Integer(0), "Front");
		h2.put(new Integer(1), "Flanking");
		h2.put(new Integer(2), "Quad");
		h2.put(new Integer(3), "Center");
		ListPropertyDescriptor pd2 = new ListPropertyDescriptor("ExtinguishStrategy", h2);
		
		descriptors.put("ExtinguishStrategy", pd2);


		Hashtable<String,String> h3 = new Hashtable<String,String>();
		h3.put("map1.txt", "Map1");
		h3.put("map2.txt", "Map2");
		h3.put("map3.txt", "Map3");
		h3.put("Random", "Random");
		ListPropertyDescriptor pd3 = new ListPropertyDescriptor("Map", h3);
		
		descriptors.put("Map", pd3);
		
		
		Hashtable<Integer,String> h4 = new Hashtable<Integer,String>();
		h4.put(new Integer(0), "NorthWest");
		h4.put(new Integer(1), "North");
		h4.put(new Integer(2), "NorthEast");
		h4.put(new Integer(3), "East");
		h4.put(new Integer(4), "SouthEast");
		h4.put(new Integer(5), "South");
		h4.put(new Integer(6), "SouthWest");
		h4.put(new Integer(7), "West");
		h4.put(new Integer(8), "Center");
		ListPropertyDescriptor pd4 = new ListPropertyDescriptor("FireStartPos", h4);
		
		descriptors.put("FireStartPos", pd4);
		
		Hashtable<Integer,String> h5 = new Hashtable<Integer,String>();
		h5.put(new Integer(0), "NorthWest");
		h5.put(new Integer(1), "North");
		h5.put(new Integer(2), "NorthEast");
		h5.put(new Integer(3), "East");
		h5.put(new Integer(4), "SouthEast");
		h5.put(new Integer(5), "South");
		h5.put(new Integer(6), "SouthWest");
		h5.put(new Integer(7), "West");
		h5.put(new Integer(8), "Center");
		ListPropertyDescriptor pd5 = new ListPropertyDescriptor("AgentStartPos", h5);
		
		descriptors.put("AgentStartPos", pd5);
		
	}
	
	public String[] getInitParam() {
		String[] params = { "WindDirection", "WindStrength", "SpreadMultiplier",
							"BurnMultiplier", "ExtinguishStrategy","ViewRange",
							"WaterRange","SafetyDistance","SquadMaxSpreadDistance",
							"CommanderOrderTimeLimit","FireTickRate","AgentTickRate",
							"Map","FireStartPos", "AgentStartPos", "AgentNumber" };	
		
		return params;
	}
	
	public int getAgentNumber(){
		return agentNumber;
	}
	
	public void setAgentNumber(int a){
		agentNumber=a;
	}
	
	
	public int getAgentStartPos(){
		return agentPosIndex;
	}
	
	public void setAgentStartPos(int a){
		agentPosIndex=a;
		
	}
	
	public void calcAgentPos(){
		double little= 0.15;
		double middle= 0.5;
		double big=0.85;
		
		int width_ = space_.width_;
		int height_ = space_.height_;
		
		
		switch (agentPosIndex) {
		case 0:
			agentPosX = (int) (width_*little);
			agentPosY = (int) (height_*little);
			break;
		case 1:
			agentPosX = (int) (width_*middle);
			agentPosY = (int) (height_*little);
			break;
		case 2:
			agentPosX = (int) (width_*big);
			agentPosY = (int) (height_*little);
			break;
		case 3:
			agentPosX = (int) (width_*big);
			agentPosY = (int) (height_*middle);
			break;
		case 4:
			agentPosX = (int) (width_*big);
			agentPosY = (int) (height_*big);
			break;
		case 5:
			agentPosX = (int) (width_*middle);
			agentPosY = (int) (height_*big);
			break;
		case 6:
			agentPosX = (int) (width_*little);
			agentPosY = (int) (height_*big);
			break;
		case 7:
			agentPosX = (int) (width_*little);
			agentPosY = (int) (height_*middle);
			break;
		case 8:
			agentPosX = (int) (width_*middle);
			agentPosY = (int) (height_*middle);
			break;
		default:
			break;
		}
	}
	
	public int getFireStartPos(){
		return firePosIndex;
	}
	
	public void setFireStartPos(int a){
		firePosIndex=a;
	}
	
	public String getMap(){
		return mapFile;
	}
	
	public void setMap(String a){
		mapFile=a;
	}
	
	public void setAgentTickRate(int a){
		if(a<=1)a=1;
		agentTickRate = a;
	}
	
	public int getAgentTickRate(){
		return agentTickRate;
	}
	
	public void setFireTickRate(int a){
		if(a<=1)a=1;
		fireTickRate = a;
	}
	
	public int getFireTickRate(){
		return fireTickRate;
	}
	
	public void setCommanderOrderTimeLimit(int a){
		if(a<0)a=0;
		units.Constants.TICKS_UNTIL_START = a;
	}
	
	public int getCommanderOrderTimeLimit(){
		return units.Constants.TICKS_UNTIL_START;
	}
	
	public void setSquadMaxSpreadDistance(int a){
		if(a<0)a=0;
		units.Constants.MAX_SPREAD_RADIUS_PER_MEN = a;
	}
	
	public int getSquadMaxSpreadDistance(){
		return units.Constants.MAX_SPREAD_RADIUS_PER_MEN;
	}
	
	public void setSafetyDistance(int a){
		if(a<0)a=0;
		units.Constants.SAFE_DISTANCE = a;
	}
	
	public int getSafetyDistance(){
		return units.Constants.SAFE_DISTANCE;
	}
	
	public void setWaterRange(int a){
		if(a<0)a=0;
		units.Constants.DEFAULT_WATER_RANGE = a;
	}
	
	public int getWaterRange(){
		return units.Constants.DEFAULT_WATER_RANGE;
	}
	
	public void setViewRange(int a){
		if(a<0)a=0;
		units.Constants.DEFAULT_VISION_RADIUS = a;
	}
	
	public int getViewRange(){
		return units.Constants.DEFAULT_VISION_RADIUS;
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
	
	public int getExtinguishStrategy(){
		return strategy;
	}
	
	public void setExtinguishStrategy(int a){
		strategy = a;
		switch (strategy) {
		case 0:
			agentStrategy = new FrontStrategy();
			break;
		case 1:
			agentStrategy = new FlankingStrategy();
			break;
		case 2:
			agentStrategy = new QuadStrategy();
			break;
		case 3:
			agentStrategy = new CenterStrategy();
			break;
		default:
			break;
		}
		configureStrategy();
	}

	public void setWindStrength(int a){
		if(a<0)a=0;
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
		//space_.step();
		com_.step();
		// agentDisplay = space.getAgentDisplay();
		display_surface_.updateDisplay();
	}
}
