package environment;

public class WindDirection {
	
	private static int N = 0;
	private static int NE = 1;
	private static int E = 2;
	private static int SE = 3;
	private static int S = 4;
	private static int SW = 5;
	private static int W = 6;
	private static int NW = 7;
	
	private int dir_;
	private int strength_;
	
	WindDirection(int dir, int str) {
		dir_ = dir;
		strength_ = str;
	}

	public int value() {
		return dir_;
	}
	
//	public void setStrength(int s){
//		strength_ = s;
//	}
	
	public int strength() {
		return strength_;
	}

}
