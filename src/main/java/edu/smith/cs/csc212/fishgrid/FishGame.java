package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class manages our model of gameplay: missing and found fish, etc.
 * 
 * @author jfoley
 *
 */
public class FishGame {
	/**
	 * This is the world in which the fish are missing. (It's mostly a List!).
	 */
	World world;
	/**
	 * The player (a Fish.COLORS[0]-colored fish) goes seeking their friends.
	 */
	P2Fish player;
	/**
	 * The home location.
	 */
	FishHome home;
	/**
	 * These are the missing fish!
	 */
	List<P2Fish> missing;

	/**
	 * These are fish we've found!
	 */
	List<P2Fish> found;

	/**
	 * Number of steps!
	 */
	int stepsTaken;

	/**
	 * Score!
	 */
	int score;

	// initialize snail
	Snail snail;
	
	//initialize falling rock
	FallingRock fallingRock;
	
	//initialize fish food
	FishFood fishFood;

	boolean fastScared;

	/**
	 * Create a FishGame of a particular size.
	 * 
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */
	public FishGame(int w, int h) {
		world = new World(w, h);

		missing = new ArrayList<P2Fish>();
		found = new ArrayList<P2Fish>();

		// Add a home!
		home = world.insertFishHome();

		// TODO(lab) Generate some more rocks!
		// TODO(lab) Make 5 into a constant, so it's easier to find & change.
		for (int i = 0; i < 5; i++) {
			world.insertRockRandomly();
		}

		// TODO(lab) Make the snail!
		snail = world.insertSnailRandomly();

		// Make the falling rock(s)!
		fallingRock = world.insertFallingRockRandomly();
		
		//Make fish food
		fishFood = world.insertFishFoodRandomly();

		// Make the player out of the 0th fish color.
		player = new P2Fish(0, world);
		// Start the player at "home".
		player.setPosition(home.getX(), home.getY());
		player.markAsPlayer();
		world.register(player);

		// Generate fish of all the colors but the first into the "missing" List.
		for (int ft = 1; ft < P2Fish.COLORS.length; ft++) {
			P2Fish friend = world.insertFishRandomly(ft);
			missing.add(friend);
		}
	}

	/**
	 * How we tell if the game is over: if missingFishLeft() == 0.
	 * 
	 * @return the size of the missing list.
	 */
	public int missingFishLeft() {
		return missing.size();
	}

	/**
	 * This method is how the Main app tells whether we're done.
	 * 
	 * @return true if the player has won (or maybe lost?).
	 */
	public boolean gameOver() {
		// TODO(FishGrid) We want to bring the fish home before we win!
		return missing.isEmpty();
	}

	/**
	 * Update positions of everything (the user has just pressed a button).
	 */
	public void step() {
		// Keep track of how long the game has run.
		this.stepsTaken += 1;
		
		Random rand = ThreadLocalRandom.current();

		if (rand.nextDouble() < 0.3) { // 30% of the time 
			if(fishFood == null) {
				fishFood = world.insertFishFoodRandomly();
			}
		} 

		// These are all the objects in the world in the same cell as the player.
		List<WorldObject> overlap = this.player.findSameCell();
		// The player is there, too, let's skip them.
		overlap.remove(this.player);

		// If we find a fish, remove it from missing.
		for (WorldObject wo : overlap) {
			// It is missing if it's in our missing list.
			if (missing.contains(wo)) {
				// Remove this fish from the missing list.
				missing.remove(wo);

				// Remove from world.
				// TODO(lab): add to found instead! (So we see objectsFollow work!)

				P2Fish f = (P2Fish) wo;
				found.add(f);

				// Increase score when you find a fish!

				if (f.getColor().equals(Color.red)) {
					score += 10;
				} else if (f.getColor().equals(Color.green)) {
					score += 12;
				} else if (f.getColor().equals(Color.yellow)) {
					score += 14;
				} else if (f.getColor().equals(Color.orange)) {
					score += 16;
				} else if (f.getColor().equals(Color.blue)) {
					score += 18;
				}
			
			}
			//Increase score when you find food!
			if (wo instanceof FishFood) {
				System.out.println("Get fish food.");
				score += 5;
				world.remove(wo);
				fishFood = null;
			}
			
			
		}
		for (P2Fish fish: missing) {
			if (fishFood != null && fish.getX() == fishFood.getX() && fish.getY() == fishFood.getY()) {
				world.remove(fishFood);
				fishFood = null;
			}
		}

		// Make sure missing fish *do* something.
		wanderMissingFish();
		// When fish get added to "found" they will follow the player around.
		World.objectsFollow(player, found);
		// Step any world-objects that run themselves.
		world.stepAll();
	}

	/**
	 * Call moveRandomly() on all of the missing fish to make them seem alive.
	 */
	private void wanderMissingFish() {
		Random rand = ThreadLocalRandom.current();
		for (P2Fish lost : missing) {
			// 30% of the time, lost fish move randomly.
			if (lost.fastScared == true) {
				if (rand.nextDouble() < 0.3) { // 30% of the time 
					// TODO(lab): What goes here?
					lost.moveRandomly();
				} 
			} else {
				// if not fastScared
				if (rand.nextDouble() < 0.8) { // else 80% of the time
					lost.moveRandomly();
				}
			}
		}
	}

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the
	 * game.
	 * 
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(int x, int y) {
		// TODO(FishGrid) use this print to debug your World.canSwim changes!
		System.out.println("Clicked on: " + x + "," + y + " world.canSwim(player,...)=" + world.canSwim(player, x, y));
		List<WorldObject> atPoint = world.find(x, y);
		// TODO(FishGrid) allow the user to click and remove rocks.
		for (WorldObject it : atPoint) {
			if (it instanceof Rock) {
				world.remove(it);
			}

		}

	}

}
