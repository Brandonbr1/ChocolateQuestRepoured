package com.teamcqr.chocolatequestrepoured.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigFileHelper {

	private int dungeonDistance = 20;
	private int dungeonSpawnDistance = 25;
	private double spawnerActivationDistance = 25.0;
	private int turnBackIntoSpawnerDistance = 48;
	private int wallDistance = 500;  //Measured in chunks  500 = 8000 blocks
	private int wallTopY = 140;
	private int wallTowerDistance = 3; //3 -> 2 chunks between each tower
	private int supportHillWallSize = 8;
	private boolean dungeonsInFlat = false;
	private boolean enableWallInTheNorth = true;
	private boolean wallHasObsiCore = true;
	
	public ConfigFileHelper() {
		
	}
	//DONE: fill out method (?)
	public void loadValues(Configuration config) {
		config.load();
		
		// Distance is measured in C H U N K S
		Property prop = config.get("general", "dungeonSeparation", 20);
		this.dungeonDistance = prop.getInt(20);
		
		// Distance of chunks to spawn
		prop = config.get("general", "dungeonSpawnDistance", 25);
		this.dungeonSpawnDistance = prop.getInt(25);
		
		//Spawner activation distance
		prop = config.get("general", "spawnerActivationDistance", 25.0);
		this.spawnerActivationDistance = prop.getDouble(25.0);
		
		// Retreat into spawner distance
		prop = config.get("general", "despawnDistance", 48);
		this.turnBackIntoSpawnerDistance = prop.getInt(48);
		
		// Spawn dungeons in flat
		prop = config.get("general", "dungeonsInFlat", false);
		this.dungeonsInFlat = prop.getBoolean(false);
		
		//Dungeons
		prop = config.get("general", "supportHillWallSize", 8);
		this.supportHillWallSize = prop.getInt(8);
		
		// Wall
		// enabled
		prop = config.get("wall", "enabled", true);
		this.enableWallInTheNorth = prop.getBoolean(true);
		
		// position !! IN CHUNKS !!
		prop = config.get("wall", "distance", 500);
		this.wallDistance = Math.abs(prop.getInt(500));
		
		// TowerDistance
		prop = config.get("wall", "towerdistance", 3);
		this.wallTowerDistance = Math.abs(prop.getInt(3));
		
		// Top Y
		prop = config.get("wall", "topY", 140);
		int yTmp = Math.abs(prop.getInt(140));
		this.wallTopY = yTmp >= 255 ? 140:yTmp;
		
		// If the wall has a obsidian core
		prop = config.get("wall", "obsidianCore", true);
		this.wallHasObsiCore = prop.getBoolean(true);
		
		config.save();
	}
	
	public int getDungeonDistance() {
		return this.dungeonDistance;
	}
	public int getDungeonSpawnDistance() {
		return this.dungeonSpawnDistance;
	}
	public double getSpawnerActivationDistance() {
		return this.spawnerActivationDistance;
	}
	public int getWallTopY() {
		return this.wallTopY;
	}
	public int getWallTowerDistance() {
		return this.wallTowerDistance;
	}
	public int getWallSpawnDistance() {
		return this.wallDistance;
	}
	public int getTurnBackIntoSpawnerDistance() {
		return this.turnBackIntoSpawnerDistance;
	}
	public boolean buildWall() {
		return this.enableWallInTheNorth;
	}
	public boolean generateDungeonsInFlat() {
		return this.dungeonsInFlat;
	}
	public boolean wallHasObsiCore() {
		return this.wallHasObsiCore;
	}
	public int getSupportHillWallSize() {
		return supportHillWallSize;
	}

}
