package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomRoofBossEmpty extends CastleRoom {
	public CastleRoomRoofBossEmpty(BlockPos startPos, int sideLength, int height, int floor) {
		super(startPos, sideLength, height, floor);
		this.roomType = EnumRoomType.ROOF_BOSS_EMPTY;
		this.pathable = false;
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
	}

	@Override
	protected void generateWalls(World world, CastleDungeon dungeon) {
	}
}
