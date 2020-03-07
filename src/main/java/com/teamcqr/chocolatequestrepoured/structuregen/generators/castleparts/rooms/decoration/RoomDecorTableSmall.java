package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;

public class RoomDecorTableSmall extends RoomDecorBlocksBase {
	public RoomDecorTableSmall() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockOffset(0, 0, 0, ModBlocks.TABLE_OAK));
	}

}
