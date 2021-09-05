package team.cqr.cqrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartBlock;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartEntity;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartPlateau;
import team.cqr.cqrepoured.structuregen.generators.castleparts.CastleRoomSelector;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.util.BlockStateGenArray;

/**
 * Copyright (c) 25.05.2019 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 */
public class GeneratorRandomizedCastle extends AbstractDungeonGenerator<DungeonRandomizedCastle> {

	private CastleRoomSelector roomHelper;

	public GeneratorRandomizedCastle(World world, BlockPos pos, DungeonRandomizedCastle dungeon, Random rand) {
		super(world, pos, dungeon, rand);
	}

	@Override
	public void preProcess() {
		this.roomHelper = new CastleRoomSelector(this.dungeon, this.random);
		this.roomHelper.randomizeCastle();

		if (this.dungeon.doBuildSupportPlatform()) {
			for (CastleRoomSelector.SupportArea area : this.roomHelper.getSupportAreas()) {
				// CQRMain.logger.info("{} {} {}", area.getNwCorner(), area.getBlocksX(), area.getBlocksZ());
				BlockPos p1 = this.pos.add(area.getNwCorner());
				BlockPos p2 = p1.add(area.getBlocksX(), 0, area.getBlocksZ());
				this.dungeonGenerator.add(new DungeonPartPlateau(this.world, this.dungeonGenerator, p1.getX(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), 8));
			}
		}
	}

	@Override
	public void buildStructure() {
		BlockStateGenArray genArray = new BlockStateGenArray(this.random);
		ArrayList<String> bossUuids = new ArrayList<>();
		DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.world, this.pos.getX(), this.pos.getZ());
		this.roomHelper.generate(this.world, genArray, this.dungeon, this.pos, bossUuids, mobType);

		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, genArray.getMainMap().values(), new PlacementSettings(), mobType));
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.pos, genArray.getPostMap().values(), new PlacementSettings(), mobType));
		this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, this.pos, genArray.getEntityMap().values(), new PlacementSettings(), mobType));
	}

	@Override
	public void postProcess() {
		// Does nothing here
	}

}
