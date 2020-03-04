package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleGenerator implements IDungeonGenerator {

	private CastleDungeon dungeon;
	private int maxSize;
	private int roomSize;
	private Random random;
	private CastleRoomSelector roomHelper;
	private int totalX;
	private int totalY;
	private int totalZ;
	private static final int FLOORS_PER_LAYER = 2;

	public CastleGenerator(CastleDungeon dungeon) {
		this.dungeon = dungeon;
		this.maxSize = this.dungeon.getMaxSize();
		this.roomSize = this.dungeon.getRoomSize();
		this.random = this.dungeon.getRandom();
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		int maxRoomsX;
		int maxRoomsZ;

		maxRoomsX = this.maxSize / this.roomSize;
		maxRoomsZ = this.maxSize / this.roomSize;

		this.roomHelper = new CastleRoomSelector(new BlockPos(x, y, z), this.dungeon.getRoomSize(), this.dungeon.getFloorHeight(), FLOORS_PER_LAYER, maxRoomsX, maxRoomsZ, this.random);
		this.roomHelper.randomizeCastle();

		// Builds the support hill;
		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			List<CastleRoomSelector.SupportArea> supportAreas = this.roomHelper.getSupportAreas();

			for (CastleRoomSelector.SupportArea area : supportAreas) {
				supportBuilder.createSupportHill(this.random, world, area.getNwCorner(), area.getBlocksX(), area.getBlocksZ(), EPosType.CORNER_NW);
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		ArrayList<String> bossUuids = new ArrayList<>();
		this.roomHelper.generate(world, this.dungeon, bossUuids);

		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x, y, z), new BlockPos(x + this.totalX, y + this.totalY, z + this.totalZ), world, bossUuids);
		MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Does nothing here
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Also does nothing
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Also does nothing
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// TODO Auto-generated method stub

	}
}
