package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VillageGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class VillageDungeon extends DungeonBase {
	
	private HashMap<Integer, List<File>> chanceFileMap = new HashMap<Integer, List<File>>();
	private File centerStructureFolder;
	
	private int minBuildings = 7;
	private int maxBuilding = 14;
	
	private int minDistance = 15;
	private int maxDistance = 30;
	
	private boolean buildPaths = true;
	private boolean placeInCircle = false;
	private String pathBlock = "minecraft:cobblestone";
	
	@Override
	public IDungeonGenerator getGenerator() {
		return new VillageGenerator(this);
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk) {
		super.generate(x, z, world, chunk);
		
		int buildings = DungeonGenUtils.getIntBetweenBorders(this.minBuildings, this.maxBuilding, world.getSeed());
		for(int i = 0; i < buildings; i++) {
			File building = getRandomBuilding(world.getSeed());
			((VillageGenerator)this.generator).addStructure(building);
			building = this.centerStructureFolder;
			if(this.centerStructureFolder.isDirectory()) {
				building = this.centerStructureFolder.listFiles()[new Random().nextInt(this.centerStructureFolder.listFiles().length)];
			}
			((VillageGenerator)this.generator).setCenterStructure(building);
		}
		//TODO: add code xD
	}
	
	private File getRandomBuilding(long seed) {
		Random rdm = new Random();
		rdm.setSeed(seed);
		
		int chance = rdm.nextInt(100) +1;
		List<Integer> indexes = new ArrayList<Integer>();
		
		for(Integer i : this.chanceFileMap.keySet()) {
			if(i >= chance) {
				indexes.add(i);
			}
		}
		if(!indexes.isEmpty()) {
			int i = indexes.get(rdm.nextInt(indexes.size()));
			File f = this.chanceFileMap.get(i).get(rdm.nextInt(this.chanceFileMap.get(i).size()));
			if(f.isDirectory()) {
				return f.listFiles()[rdm.nextInt(f.listFiles().length)];
			}
			return f;
		}
		return null;
	}
	
	public int getMinDistance() {
		return this.minDistance;
	}
	public int getMaxDistance() {
		return this.maxDistance;
	}
	public boolean buildPaths() {
		return this.buildPaths;
	}
	public boolean placeInCircle() {
		return this.placeInCircle;
	}
	public Block getPathMaterial() {
		try {
			Block b = Block.getBlockFromName(this.pathBlock);
			return b;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return Blocks.GRASS_PATH;
	}
	
}
