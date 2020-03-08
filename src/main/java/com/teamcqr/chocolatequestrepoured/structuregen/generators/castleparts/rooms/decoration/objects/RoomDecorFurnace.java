package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorFurnace extends RoomDecorBlocksBase {
	public RoomDecorFurnace() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.FURNACE));
	}

	@Override
	protected IBlockState getRotatedBlockState(Block block, EnumFacing side) {
		IBlockState result = block.getDefaultState();

		if (block == Blocks.FURNACE) {
			result = result.withProperty(BlockFurnace.FACING, side.getOpposite());
		}

		return result;
	}
}
