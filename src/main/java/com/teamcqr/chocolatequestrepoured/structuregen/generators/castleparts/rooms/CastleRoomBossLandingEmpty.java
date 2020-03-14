package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomBossLandingEmpty extends CastleRoomBase {
	private EnumFacing doorSide;

	public CastleRoomBossLandingEmpty(BlockPos startPos, int sideLength, int height, EnumFacing doorSide, int floor) {
		super(startPos, sideLength, height, floor);
		this.roomType = EnumRoomType.LANDING_BOSS;
		this.pathable = false;
		this.doorSide = doorSide;
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
	}

	@Override
	public void addInnerWall(EnumFacing side) {
		if (!(this.doorSide.getAxis() == EnumFacing.Axis.X && side == EnumFacing.SOUTH) && !(this.doorSide.getAxis() == EnumFacing.Axis.Z && side == EnumFacing.EAST) && !(side == this.doorSide)) {
			super.addInnerWall(side);
		}
	}

}
