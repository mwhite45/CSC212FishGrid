package edu.smith.cs.csc212.fishgrid;

public class FallingRock extends Rock {
	
	public FallingRock(World world) {
		super(world);
		
	}
	
	@Override
	public void step() {
		// Falling rocks don't actually *do* anything, either.	
		this.moveDown();
	}

}
