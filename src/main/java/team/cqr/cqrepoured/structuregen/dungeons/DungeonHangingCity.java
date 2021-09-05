package team.cqr.cqrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.generators.SuspensionBridgeHelper.IBridgeDataSupplier;
import team.cqr.cqrepoured.structuregen.generators.hangingcity.GeneratorHangingCity;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonHangingCity extends DungeonBase implements IBridgeDataSupplier {

	private int minBuildings = 6;
	private int maxBuildings = 12;
	private int minIslandDistance = 15;
	private int maxIslandDistance = 30;
	private int yFactorHeight = 20;
	private int heightVariation = 10;
	private int bridgeWidth = 5;
	private boolean digAirCave = true;
	private boolean constructBridges = true;
	private float bridgeTension = 3.0F;

	private IBlockState islandMaterial = Blocks.NETHERRACK.getDefaultState();
	private IBlockState chainBlock = Blocks.OBSIDIAN.getDefaultState();
	private IBlockState bridgeBlock = Blocks.PLANKS.getDefaultState();
	private IBlockState bridgeFenceBlock = Blocks.OAK_FENCE.getDefaultState();
	private IBlockState bridgeRailingBlock = Blocks.AIR.getDefaultState();
	private IBlockState bridgeAnchorBlock = Blocks.OBSERVER.getDefaultState();
	// private Block bridgeBlock = Blocks.NETHER_BRICK;
	// private int bridgeChance = 20;
	private boolean buildChains = true;
	// private boolean buildBridges = false;
	private File structureFolder;
	private File centralStructureFolder;

	public DungeonHangingCity(String name, Properties prop) {
		super(name, prop);

		this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minBuildings", 6);
		this.maxBuildings = PropertyFileHelper.getIntProperty(prop, "maxBuildings", 12);
		this.minIslandDistance = PropertyFileHelper.getIntProperty(prop, "minIslandDistance", 15);
		this.maxIslandDistance = PropertyFileHelper.getIntProperty(prop, "maxIslandDistance", 30);
		this.yFactorHeight = PropertyFileHelper.getIntProperty(prop, "islandFloorCeilingsDistance", 20);
		this.heightVariation = PropertyFileHelper.getIntProperty(prop, "islandHeightVariation", 10);
		this.bridgeWidth = PropertyFileHelper.getIntProperty(prop, "bridgeWidth", 5);

		this.bridgeTension = PropertyFileHelper.getFloatProperty(prop, "bridgeTension", 3.0F);

		this.digAirCave = PropertyFileHelper.getBooleanProperty(prop, "digAirCave", true);
		this.buildChains = PropertyFileHelper.getBooleanProperty(prop, "buildChains", true);
		this.constructBridges = PropertyFileHelper.getBooleanProperty(prop, "constructBridges", true);

		this.structureFolder = PropertyFileHelper.getStructureFolderProperty(prop, "structureFolder", "floatingCity/islands");
		this.centralStructureFolder = PropertyFileHelper.getStructureFolderProperty(prop, "centralStructureFolder", "floatingCity/centers");

		this.islandMaterial = PropertyFileHelper.getBlockStateProperty(prop, "islandBlock", Blocks.NETHERRACK.getDefaultState());
		this.chainBlock = PropertyFileHelper.getBlockStateProperty(prop, "chainBlock", Blocks.OBSIDIAN.getDefaultState());
		this.bridgeAnchorBlock = PropertyFileHelper.getBlockStateProperty(prop, "bridgeAnchorBlock", Blocks.OBSIDIAN.getDefaultState());
		this.bridgeBlock = PropertyFileHelper.getBlockStateProperty(prop, "bridgeBlock", Blocks.PLANKS.getDefaultState());
		this.bridgeFenceBlock = PropertyFileHelper.getBlockStateProperty(prop, "bridgeFenceBlock", Blocks.OAK_FENCE.getDefaultState());
		this.bridgeRailingBlock = PropertyFileHelper.getBlockStateProperty(prop, "bridgeRailingBlock", Blocks.AIR.getDefaultState());
	}

	@Override
	public AbstractDungeonGenerator<DungeonHangingCity> createDungeonGenerator(World world, int x, int y, int z, Random rand,
			DungeonDataManager.DungeonSpawnType spawnType) {
		return new GeneratorHangingCity(world, new BlockPos(x, y, z), this, rand);
	}

	// Generator: Radius of the island circle is the longer side (x or z) -1 of the structure to spawn!!
	// IMPORTANT: Calculate the "center" of the structure and begin to draw the circle from there

	// Code i used in a plugin to generate a round platform.... i may recycle parts of it....
	// On each step downwards, the radius gets smaller by decrement, decrement increases per step down and it starts by 1
	// It stops going down, when decremt *2 is greater than the current decremented radius
	/*
	 * private int radius;
	 * 
	 * @SuppressWarnings("unused") private int platformThickness;
	 * 
	 * public WorldGenEndIsland(int r, int pt) { this.radius = r; this.platformThickness = pt; }
	 * 
	 * @Override public boolean generate(World world, Random random, BlockPosition startPos) { float rdmFloat = (float)
	 * (random.nextInt(3) + 4);
	 * 
	 * for (int y = 0; rdmFloat > 0.5F; --y) { for (int x = MathHelper.d(-rdmFloat); x <= MathHelper.f(rdmFloat); ++x) { for
	 * (int z = MathHelper.d(-rdmFloat); z <=
	 * MathHelper.f(rdmFloat); ++z) { if ((float) (x * x + z * z) <= (rdmFloat + 1.0F) *
	 * (rdmFloat + 1.0F)) { if(isAllowed(x) && isAllowed(z)) { this.a(world, startPos.a(x, y, z),
	 * Blocks.END_STONE.getBlockData()); } } } }
	 * 
	 * rdmFloat = (float) ((double) rdmFloat - ((double) random.nextInt(2) + 0.5D)); }
	 * 
	 * return true; }
	 * 
	 * private boolean isAllowed(int distanceToCenter) { double divisionResult = ((double)distanceToCenter) /
	 * ((double)this.radius); if(divisionResult < 1.0D) {
	 * return true; } return false; }
	 */

	public File pickStructure(Random rand) {
		if (this.structureFolder == null) {
			return null;
		}
		return this.getStructureFileFromDirectory(this.structureFolder, rand);
	}

	public File pickCentralStructure(Random rand) {
		if (this.centralStructureFolder == null) {
			return null;
		}
		return this.getStructureFileFromDirectory(this.centralStructureFolder, rand);
	}

	public boolean doBuildChains() {
		return this.buildChains;
	}

	/*
	 * public boolean doBuildBridges() { return this.buildBridges; } public int getBridgeChance() { return
	 * this.bridgeChance; }
	 */

	public int getMinBuildings() {
		return this.minBuildings;
	}

	public int getMaxBuildings() {
		return this.maxBuildings;
	}

	public IBlockState getChainBlock() {
		return this.chainBlock;
	}

	public IBlockState getIslandBlock() {
		return this.islandMaterial;
	}

	/*
	 * public Block getBridgeBlock() { return this.bridgeBlock; }
	 */

	public int getMinIslandDistance() {
		return this.minIslandDistance;
	}

	public int getMaxIslandDistance() {
		return this.maxIslandDistance;
	}

	public int getYFactorHeight() {
		return this.yFactorHeight;
	}

	public boolean digAirCave() {
		return this.digAirCave;
	}

	public int getRandomHeightVariation(Random rand) {
		if (this.heightVariation != 0) {
			int var = Math.abs(this.heightVariation);
			int rvar = DungeonGenUtils.randomBetween(0, var, rand);
			return var / 2 - rvar;
		}
		return 0;
	}

	public boolean isConstructBridges() {
		return constructBridges;
	}

	@Override
	public float getBridgeTension() {
		return this.bridgeTension;
	}

	@Override
	public int getBridgeWidth() {
		return this.bridgeWidth;
	}

	@Override
	public IBlockState getBridgePathBlock() {
		return this.bridgeBlock;
	}

	@Override
	public IBlockState getBridgeFenceBlock() {
		return this.bridgeFenceBlock;
	}

	@Override
	public IBlockState getBridgeRailingBlock() {
		return this.bridgeRailingBlock;
	}

	@Override
	public IBlockState getBridgeAnchorBlock() {
		return this.bridgeAnchorBlock;
	}

}
