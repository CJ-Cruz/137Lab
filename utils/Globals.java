package utils;

import java.awt.Color;

//This class contains several static codes used throughout the game.
public interface Globals {

	//Universal Codes
	final public static int UNKNOWN = 0;
	final public static int UNIQUE_CODE = -123;
	
	//Troop Types
	final public static int TROOP_BASIC = 1;
	final public static int TROOP_ARCHER = 2;
	final public static int TROOP_GIANT = 3;
	final public static int TROOP_WALLBREAKER = 4;
	
	//Building Types
	final public static int BUILDING_BOX = 1;
	
	//Building Block Sizes
	final public static int[] BLOCK_WIDTH = {	-1,
			3,	//Box
			1	//Wall
	};
	final public static int[] BLOCK_HEIGHT = {	-1,
			3,	//Box
			1	//Wall
	};
	
	//Color Codes
	final public static Color PLAYER_1 = Color.RED;
	final public static Color PLAYER_2 = Color.BLUE;
	final public static Color PLAYER_3 = Color.CYAN;
	
	//Server Codes
	final public static int SERVER_WAITING = 1;
	final public static int SERVER_STARTING = 2;
	final public static int SERVER_PLAYING = 3;
	final public static int SERVER_END = 4;
	
	//Player Codes
	final public static int PLAYER_CONNECTING = 1;
	final public static int PLAYER_BASE_SETUP = 2;
	final public static int PLAYER_UNITS_SETUP = 3;
	final public static int PLAYER_PLAYING = 4;
	
	//Animation Codes
		//Up
		final public static int ANIMATION_STAND_UP = 1;
		final public static int ANIMATION_WALK_UP = 2;
		final public static int ANIMATION_WALK_UP_ALT = 3;
		final public static int ANIMATION_ATTACK_UP = 4;
		final public static int ANIMATION_ATTACK_UP_ALT = 5;
		//Down
		final public static int ANIMATION_STAND_DOWN = 6;
		final public static int ANIMATION_WALK_DOWN = 7;
		final public static int ANIMATION_WALK_DOWN_ALT = 8;
		final public static int ANIMATION_ATTACK_DOWN = 9;
		final public static int ANIMATION_ATTACK_DOWN_ALT = 10;
		//Left
		final public static int ANIMATION_STAND_LEFT = 11;
		final public static int ANIMATION_WALK_LEFT = 12;
		final public static int ANIMATION_WALK_LEFT_ALT = 13;
		final public static int ANIMATION_ATTACK_LEFT = 14;
		final public static int ANIMATION_ATTACK_LEFT_ALT = 15;
		//Right
		final public static int ANIMATION_STAND_RIGHT = 16;
		final public static int ANIMATION_WALK_RIGHT = 17;
		final public static int ANIMATION_WALK_RIGHT_ALT = 18;
		final public static int ANIMATION_ATTACK_RIGHT = 19;
		final public static int ANIMATION_ATTACK_RIGHT_ALT = 20;
		//Death
		final public static int ANIMATION_DEATH = 21;
		final public static int ANIMATION_DEATH_ALT = 22;
		
		
	//Priorities
	final public static int PRIORITY_NEAREST = 1;
	final public static int PRIORITY_BUILDINGS = 2;
	final public static int PRIORITY_DEFENSES = 3;
	final public static int PRIORITY_WALL = 4;
	final public static int PRIORITY_NEAREST_NOT_WALL = 5;
	
	
}