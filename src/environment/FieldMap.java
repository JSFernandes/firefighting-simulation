package environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import uchicago.src.sim.space.Object2DTorus;

public class FieldMap {
	public static int width_;
	public static int height_;
	
	public static Object2DTorus getMap() throws Exception {
		Object2DTorus map;
		String currentDir = new File(".").getAbsolutePath();
		 System.out.println(currentDir);
		 BufferedReader reader = new BufferedReader(new FileReader("map.txt"));
		 
		    try {
		        String[] dimensions = reader.readLine().split(" ");
		        
		        width_ = Integer.parseInt(dimensions[0]);
		        height_ = Integer.parseInt(dimensions[1]);
		        
		        map = new Object2DTorus(width_, height_);

		        for (int y = 0; y < height_; ++y) {
		        	String line = reader.readLine();
		        	System.out.println(line);
		        	for (int x = 0; x < width_; ++x) {
		        		map.putObjectAt(x, y, new Tree(x, y, Integer.valueOf(line.charAt(x)) - 48 ));
		        	}
		        }

		    } finally {
		        reader.close();
		    }
		
		return map;
	}
}
