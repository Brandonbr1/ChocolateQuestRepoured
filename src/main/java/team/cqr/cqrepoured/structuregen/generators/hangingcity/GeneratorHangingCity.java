package team.cqr.cqrepoured.structuregen.generators.hangingcity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonHangingCity;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class GeneratorHangingCity extends AbstractDungeonGenerator<DungeonHangingCity> {

	// DONE: Air bubble around the whole thing

	private int islandCount = 1;
	private int islandDistance = 1;
	//private Map<BlockPos, CQStructure> structureMap = new HashMap<>();
	private HangingCityBuilding[][] buildingGrid;
	
	// This needs to calculate async (island blocks, chain blocks, air blocks)

	public GeneratorHangingCity(World world, BlockPos pos, DungeonHangingCity dungeon, Random rand, DungeonDataManager.DungeonSpawnType spawnType) {
		super(world, pos, dungeon, rand, spawnType);
	}

	@Override
	public void preProcess() {
		this.islandCount = DungeonGenUtils.randomBetween(this.dungeon.getMinBuildings(), this.dungeon.getMaxBuildings(), this.random);
		this.islandDistance = DungeonGenUtils.randomBetween(this.dungeon.getMinIslandDistance(), this.dungeon.getMaxIslandDistance(), this.random);

		this.buildingGrid = new HangingCityBuilding[(2* islandCount) + 4][(2* islandCount) + 4];
		
		final int offsetXY = islandCount +2;
		
		// Calculates the positions and creates the island objects
		// positions are the !!CENTERS!! of the platforms, the structures positions are calculated by the platforms themselves
		// Radius = sqrt(((Longer side of building) / 2)^2 *2) +5
		// Chain start pos: diagonal go (radius / 3) * 2 -1 blocks, here start building up the chains...
		// DONE: Carve out cave -> Need to specify the height in the dungeon
		/*for (int i = 0; i < this.islandCount; i++) {
			BlockPos nextIslandPos = this.getNextIslandPos(this.pos, i);
			File file = this.dungeon.pickStructure(this.random);
			CQStructure structure = this.loadStructureFromFile(file);

			this.structureMap.put(nextIslandPos, structure);
			if (this.dungeon.digAirCave()) {
				int radius = structure != null ? 2 * Math.max(structure.getSize().getX(), structure.getSize().getZ()) : 16;
				BlockPos startPos = nextIslandPos.add(-radius, -this.dungeon.getYFactorHeight(), -radius);
				BlockPos endPos = nextIslandPos.add(radius, this.dungeon.getYFactorHeight(), radius);
				this.dungeonGenerator.add(PlateauBuilder.makeRandomBlob2(Blocks.AIR, startPos, endPos, 8, WorldDungeonGenerator.getSeed(this.world, this.pos.getX() >> 4, this.pos.getZ() >> 4), this.world, this.dungeonGenerator));
			}
		}

		CQStructure structure = this.loadStructureFromFile(this.dungeon.pickCentralStructure(this.random));
		this.structureMap.put(this.pos, structure);
		if (this.dungeon.digAirCave()) {
			int radius = structure != null ? 2 * Math.max(structure.getSize().getX(), structure.getSize().getZ()) : 16;
			BlockPos startPos = this.pos.add(-radius, -this.dungeon.getYFactorHeight(), -radius);
			BlockPos endPos = this.pos.add(radius, this.dungeon.getYFactorHeight(), radius);
			this.dungeonGenerator.add(PlateauBuilder.makeRandomBlob2(Blocks.AIR, startPos, endPos, 8, WorldDungeonGenerator.getSeed(this.world, this.pos.getX() >> 4, this.pos.getZ() >> 4), this.world, this.dungeonGenerator));
		}*/
		HangingCityBuilding lastProcessed = null;
		CQStructure structure = this.loadStructureFromFile(this.dungeon.pickCentralStructure(this.random));
		List<HangingCityBuilding> processed = new ArrayList<>();
		//Create grid
		for(int i = 0; i < this.islandCount; i++) {
			Tuple<Integer, Integer> coords = new Tuple<>(0,0);
			if(lastProcessed != null) {
				structure = this.loadStructureFromFile(this.dungeon.pickStructure(this.random));
				boolean continueSearch = true;
				List<HangingCityBuilding> buildings = new ArrayList<>(processed);
				Collections.shuffle(buildings, this.random);
				Queue<HangingCityBuilding> buildingQueue = new LinkedList<>(buildings);
				do {
					HangingCityBuilding chosen = buildingQueue.remove();
					if(chosen.hasFreeNeighbourSpots()) {
						List<Tuple<Integer, Integer>> spots = new ArrayList<>(chosen.getFreeNeighbourSpots());
						Collections.shuffle(spots, this.random);
						coords = spots.get(0);
						continueSearch = false;
					}
					
				} while(continueSearch && !buildingQueue.isEmpty());
			}
			lastProcessed = new HangingCityBuilding(this, coords.getFirst(), coords.getSecond(), structure);
			lastProcessed.preProcess(world, dungeonGenerator, null);
			processed.add(lastProcessed);
			this.buildingGrid[offsetXY + coords.getFirst()][offsetXY + coords.getSecond()] = lastProcessed;
		}
		//Calculate bridge connections
		//Needs to call building.connectTo on the first and markAsConnected on the second
		if(this.dungeon.isConstructBridges()) {
			//TODO
		}
	}

	@Override
	public void buildStructure() {
		DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.world, this.pos.getX(), this.pos.getZ());

		// Builds the platforms
		// Builds the chains
		/*for (Map.Entry<BlockPos, CQStructure> entry : this.structureMap.entrySet()) {
			BlockPos bp = entry.getKey();
			CQStructure structure = entry.getValue();

			this.buildBuilding(bp, structure, mobType);
		}*/
	}

	@Override
	public void postProcess() {
		// Not needed
	}

	// calculates a fitting position for the next island
	/*private BlockPos getNextIslandPos(BlockPos centerPos, int islandIndex) {
		BlockPos retPos = new BlockPos(centerPos);

		Vec3i vector = new Vec3i(0, 0, (this.islandDistance * 3D) * ((islandIndex) / 10 + 1));

		int degreeMultiplier = islandIndex;
		// int degreeMultiplier = (Math.floorDiv(islandIndex, 10) *10);
		if (this.islandCount > 10) {
			degreeMultiplier -= (((islandIndex) / 10) * 10);
		}
		double angle = this.islandCount >= 10 ? 36D : 360D / this.islandCount;
		retPos = retPos.add(VectorUtil.rotateVectorAroundY(vector, degreeMultiplier * angle));
		retPos = retPos.add(0, this.dungeon.getRandomHeightVariation(this.random), 0);
		return retPos;
	}*/
	
	HangingCityBuilding getBuildingFromGridPos(int x, int y) {
		x += this.islandCount;
		y += this.islandCount;
		
		return this.buildingGrid[x][y];
	}

	// Constructs an Island in this shape:
	/*
	 * Dec Rad # # # # # # # # # # # # # # # # # # # # 0 10 # # # # # # # # # # # # # # # # # # 1 9 # # # # # # # # # # # # # # 2 7 # # # # # # # # 3 4
	 * 
	 */
	/*private void buildBuilding(BlockPos centeredPos, CQStructure structure, DungeonInhabitant mobType) {
		int longestSide = structure != null ? Math.max(structure.getSize().getX(), structure.getSize().getZ()) : 16;
		int radius = longestSide / 2;
		radius *= radius;
		radius *= 2;
		radius = (int) (Math.round(Math.sqrt(radius))) + 5;// (int) (0.7071D * (double) longestSide) + 5;

		this.buildPlatform(centeredPos, radius, mobType);
		if (structure != null) {
			PlacementSettings settings = new PlacementSettings();
			BlockPos p = DungeonGenUtils.getCentralizedPosForStructure(centeredPos.up(), structure, settings);
			structure.addAll(this.world, this.dungeonGenerator, p, settings, mobType);
		}
	}

	private void buildPlatform(BlockPos center, int radius, DungeonInhabitant mobType) {
		Map<BlockPos, IBlockState> stateMap = new HashMap<>();
		int decrementor = 0;
		int rad = (int) (1.5D * radius);
		while (decrementor < (rad / 2)) {
			rad -= decrementor;

			for (int iX = -rad; iX <= rad; iX++) {
				for (int iZ = -rad; iZ <= rad; iZ++) {
					if (DungeonGenUtils.isInsideCircle(iX, iZ, rad)) {
						stateMap.put((center.add(iX, -decrementor, iZ)), this.dungeon.getIslandBlock());
					}
				}
			}

			decrementor++;
		}

		if (this.dungeon.doBuildChains()) {
			this.buildChain(center.add(radius * 0.9, -2, radius * 0.9), 0, stateMap);
			this.buildChain(center.add(-radius * 0.9, -2, -radius * 0.9), 0, stateMap);
			this.buildChain(center.add(-radius * 0.9, -2, radius * 0.9), 1, stateMap);
			this.buildChain(center.add(radius * 0.9, -2, -radius * 0.9), 1, stateMap);
		}

		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();
		for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
			blockInfoList.add(new BlockInfo(entry.getKey().subtract(center), entry.getValue(), null));
		}
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, center, blockInfoList, new PlacementSettings(), mobType));
	}

	private void buildChain(BlockPos pos, int iOffset, Map<BlockPos, IBlockState> stateMap) {
		/*
		 * Chain from side: # # # # # # # # # # # # # # # # # # # #
		 */
		/*int deltaYPerChainSegment = 5;*/

		/*
		 * int maxY = DungeonGenUtils.getYForPos(this.world, pos.getX(), pos.getZ(), true);
		 * maxY = maxY >= 255 ? 255 : maxY;
		 */
		// TODO: Move this option to the config of the dungeon, that is cleaner
		// Or: Change this to something like "world.getMaxBuildHeight()", if that exists.
		/*int maxY = 255;
		int chainCount = (maxY - pos.getY()) / deltaYPerChainSegment;
		for (int i = 0; i < chainCount; i++) {
			// Check the direction of the chain
			int yOffset = i * deltaYPerChainSegment;
			BlockPos startPos = pos.add(0, yOffset, 0);
			if ((i + iOffset) % 2 > 0) {
				this.buildChainSegment(startPos, startPos.north(), startPos.south(), startPos.north(2).up(), startPos.south(2).up(), stateMap);
			} else {
				this.buildChainSegment(startPos, startPos.east(), startPos.west(), startPos.east(2).up(), startPos.west(2).up(), stateMap);
			}
		}
	}*/
	
	final BlockPos getCenterPosForIsland(HangingCityBuilding building) {
		BlockPos centerGen = this.pos;
		int offsetX = this.islandDistance * building.getGridPosX();
		int offsetZ = this.islandDistance * building.getGridPosY();
		
		int offsetY = this.dungeon.getRandomHeightVariation(this.random);
		
		final BlockPos pos = centerGen.add(offsetX, offsetY, offsetZ);
		return pos;
	}
/*
	private void buildChainSegment(BlockPos lowerCenter, BlockPos lowerLeft, BlockPos lowerRight, BlockPos lowerBoundL, BlockPos lowerBoundR, Map<BlockPos, IBlockState> stateMap) {
		stateMap.put(lowerCenter, this.dungeon.getChainBlock());
		stateMap.put(lowerCenter.add(0, 6, 0), this.dungeon.getChainBlock());

		stateMap.put(lowerLeft, this.dungeon.getChainBlock());
		stateMap.put(lowerLeft.add(0, 6, 0), this.dungeon.getChainBlock());

		stateMap.put(lowerRight, this.dungeon.getChainBlock());
		stateMap.put(lowerRight.add(0, 6, 0), this.dungeon.getChainBlock());

		for (int i = 0; i < 5; i++) {
			stateMap.put(lowerBoundL.add(0, i, 0), this.dungeon.getChainBlock());
			stateMap.put(lowerBoundR.add(0, i, 0), this.dungeon.getChainBlock());
		}
	}*/

	public BlockPos getPos() {
		return this.pos;
	}

}
