package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.CastlePartSquare;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.ICastlePart;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleGenerator implements IDungeonGenerator{

	private CastleDungeon dungeon;
	private int maxSize;
    private int roomSize;
    private int floorHeight;
    private Random random;

	public CastleGenerator(CastleDungeon dungeon) {
		this.dungeon = dungeon;
		this.maxSize = this.dungeon.getMaxSize();
		this.roomSize = this.dungeon.getRoomSize();
		this.floorHeight = this.dungeon.getFloorHeight();
		this.random = new Random();
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//Builds the support hill;
		if(this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.generate(new Random(), world, x, y + this.dungeon.getUnderGroundOffset(), z, maxSize, maxSize);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z)
	{
		int sizeX;
		int sizeZ;
		int offsetX;
		int offsetZ;
		int quarterSizeX;
		int quarterSizeZ;
		int buildAreaX = maxSize;
		int buildAreaZ = maxSize;
		int floors = 2;

		// Calculate random size based on maximum size
		quarterSizeX = buildAreaX / 4;
		quarterSizeZ = buildAreaZ / 4;
		sizeX = quarterSizeX + random.nextInt(quarterSizeX * 3);
		sizeZ = quarterSizeZ + random.nextInt(quarterSizeZ * 3);

		List<ICastlePart> parts = new ArrayList<>();

		while (Math.min(sizeX, sizeZ) > roomSize)
		{
			offsetX = random.nextInt(quarterSizeX);
			offsetZ = random.nextInt(quarterSizeZ);

			// Size of building must be at least 1 room
			sizeX = Math.max(roomSize, sizeX);
			sizeZ = Math.max(roomSize, sizeZ);

			// Apply the offset
			x += offsetX;
			z += offsetZ;

			// Add the main building
			parts.add(new CastlePartSquare(new BlockPos(x, y, z), sizeX, sizeZ, floors, this.dungeon, getRandomFacing()));

			int subSizeX;
			int subSizeZ;
			int towerWidth;
			int subX;
			int subZ;
			int roomToBuildX;
			int roomToBuildZ;
			EnumFacing facing;

			facing = EnumFacing.NORTH;
			roomToBuildX = sizeX;
			roomToBuildZ = offsetZ;
			// Add substructure to the north, if there is room
			if ((roomToBuildX > roomSize) && (roomToBuildZ > roomSize))
			{
				subSizeX = Math.max(random.nextInt(roomToBuildX), roomSize);
				subSizeZ = Math.max(random.nextInt(roomToBuildZ), roomSize);
				subX = random.nextBoolean() ? x : x + sizeX - subSizeX;
				subZ = z - subSizeZ;
				parts.add(new CastlePartSquare(new BlockPos(subX, y, subZ), subSizeX, subSizeZ, floors, this.dungeon, facing));
			}

			facing = EnumFacing.EAST;
			roomToBuildX = buildAreaX - sizeX - offsetX;
			roomToBuildZ = sizeZ;
			// Add substructure to the east, if there is room
			if ((roomToBuildX > roomSize) && (roomToBuildZ > roomSize))
			{
				subSizeX = Math.max(random.nextInt(roomToBuildX), roomSize);
				subSizeZ = Math.max(random.nextInt(roomToBuildZ), roomSize);
				subX = x + sizeX;
				subZ = random.nextBoolean() ? z : z + sizeZ - subSizeZ;
				parts.add(new CastlePartSquare(new BlockPos(subX, y, subZ), subSizeX, subSizeZ, floors, this.dungeon, facing));
			}

			facing = EnumFacing.SOUTH;
			roomToBuildX = sizeX;
			roomToBuildZ = buildAreaZ - sizeZ - offsetZ;
			// Add substructure to the south, if there is room
			if ((roomToBuildX > roomSize) && (roomToBuildZ > roomSize))
			{
				subSizeX = Math.max(random.nextInt(roomToBuildX), roomSize);
				subSizeZ = Math.max(random.nextInt(roomToBuildZ), roomSize);
				subX = random.nextBoolean() ? x : x + sizeX - subSizeX;
				subZ = z + sizeZ;
				parts.add(new CastlePartSquare(new BlockPos(subX, y, subZ), subSizeX, subSizeZ, floors, this.dungeon, facing));
			}

			facing = EnumFacing.WEST;
			roomToBuildX = offsetX;
			roomToBuildZ = sizeZ;
			// Add substructure to the west, if there is room
			if ((roomToBuildX > roomSize) && (roomToBuildZ > roomSize))
			{
				subSizeX = Math.max(random.nextInt(roomToBuildX), roomSize);
				subSizeZ = Math.max(random.nextInt(roomToBuildZ), roomSize);
				subX = x - subSizeX;
				subZ = random.nextBoolean() ? z : z + sizeZ - subSizeZ;
				parts.add(new CastlePartSquare(new BlockPos(subX, y, subZ), subSizeX, subSizeZ, floors, this.dungeon, facing));
			}

			// Now try to build a new structure on top of this one
			quarterSizeX = sizeX / 4;
			quarterSizeZ = sizeZ / 4;
			buildAreaX = sizeX;
			buildAreaZ = sizeZ;
			sizeX = quarterSizeX + random.nextInt(quarterSizeX * 3);
			sizeZ = quarterSizeZ + random.nextInt(quarterSizeZ * 3);
			y += (floorHeight + 1) * floors;
		}

		for (ICastlePart part : parts)
		{
			part.generatePart(world);
		}


		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x,y,z), new BlockPos(x + sizeX, y, z + sizeZ), chunk.getPos(), world);
		MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//Does nothing here
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Also does nothing
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Also does nothing
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub

	}

	private EnumFacing getRandomFacing()
	{
		int idx = random.nextInt(4);
		switch (idx)
		{
			case 0:
				return EnumFacing.NORTH;
			case 1:
				return EnumFacing.EAST;
			case 2:
				return EnumFacing.SOUTH;
			case 3:
			default:
				return EnumFacing.WEST;
		}
	}

}
