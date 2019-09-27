package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class FallingRock extends Rock {
	
	
	
	public FallingRock(World world) {
		super(world);
		
	}
	
	/**
	 * Draw a rock!
	 */
	@Override
	public void draw(Graphics2D g) {
		// TODO(lab): use the right color in here... 
		// Don't need to do this part b/c of first to do.
		g.setColor(this.randomColor);
		RoundRectangle2D rock = new RoundRectangle2D.Double(-.5,-.5,1,1,0.3,0.3);
		g.fill(rock);
	}
	
	
	
	@Override
	public void step() {
		// Falling rocks don't actually *do* anything, either.	
		this.moveDown();
	}
	

}
