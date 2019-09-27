package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;
//import java.awt.geom.RoundRectangle2D;
import java.awt.geom.RoundRectangle2D;


public class FishFood extends WorldObject {
	
	public FishFood(World world) {
		super(world);
		
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.green);
		RoundRectangle2D fishFood = new RoundRectangle2D.Double(-.5,-.5,1,1,0.3,0.3);
		g.fill(fishFood);
	}
	
	@Override
	public void step() {
		// Falling rocks don't actually *do* anything, either.	
		
	} 

}
