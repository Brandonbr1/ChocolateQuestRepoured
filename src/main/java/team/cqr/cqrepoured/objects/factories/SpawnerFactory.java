package team.cqr.cqrepoured.objects.factories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

/**
 * A static utility class for generating CQR/vanilla spawners and converting them to/from the other
 * 
 * @author DerToaster, Meldexun, jdawg3636
 * @version 11 October 2019
 */
public abstract class SpawnerFactory {

	/*
	 * Creation/Modification
	 */

	/**
	 * Places a spawner in the provided world at the provided position. Spawner type (CQR/vanilla) is determined dynamically
	 * based upon the requested capabilities.
	 * 
	 * @param entities                 Entities for spawner to spawn
	 * @param multiUseSpawner          Determines spawner type. Vanilla = true; CQR = false.
	 * @param spawnerSettingsOverrides Settings to be applied if generating vanilla spawner (can be null if CQR spawner)
	 * @param world                    World in which to place spawner
	 * @param pos                      Position at which to place spawner
	 */
	public static void placeSpawner(Entity[] entities, boolean multiUseSpawner, @Nullable NBTTagCompound spawnerSettingsOverrides, World world, BlockPos pos) {
		NBTTagCompound[] entCompounds = new NBTTagCompound[entities.length];
		for (int i = 0; i < entities.length; i++) {
			Entity ent = entities[i];
			if (ent == null) {
				continue;
			}
			entCompounds[i] = createSpawnerNBTFromEntity(ent);
		}
		placeSpawner(entCompounds, multiUseSpawner, spawnerSettingsOverrides, world, pos);
	}

	/**
	 * Places a spawner in the provided world at the provided position. Spawner type (CQR/vanilla) is determined dynamically
	 * based upon the requested capabilities.
	 * 
	 * @param entities                 Entities as NBT Tag (From Entity.writeToNBTOptional(COMPOUND) for spawner to spawn
	 * @param multiUseSpawner          Determines spawner type. Vanilla = true; CQR = false.
	 * @param spawnerSettingsOverrides Settings to be applied if generating vanilla spawner (can be null if CQR spawner)
	 * @param world                    World in which to place spawner
	 * @param pos                      Position at which to place spawner
	 */
	public static void placeSpawner(NBTTagCompound[] entities, boolean multiUseSpawner, @Nullable NBTTagCompound spawnerSettingsOverrides, World world,
			BlockPos pos) {
		world.setBlockState(pos, multiUseSpawner ? Blocks.MOB_SPAWNER.getDefaultState() : CQRBlocks.SPAWNER.getDefaultState());

		TileEntity tileEntity = world.getTileEntity(pos);
		if (multiUseSpawner) {
			TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) tileEntity;
			NBTTagCompound compound = tileEntityMobSpawner.writeToNBT(new NBTTagCompound());
			NBTTagList spawnPotentials = new NBTTagList();

			// Store entity ids into NBT tag
			for (int i = 0; i < entities.length; i++) {
				if (entities[i] != null) {
					{
						// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an
						// entity
						entities[i].removeTag("UUIDLeast");
						entities[i].removeTag("UUIDMost");

						entities[i].removeTag("Pos");
						NBTTagList passengers = entities[i].getTagList("Passengers", 10);
						for (NBTBase passenger : passengers) {
							((NBTTagCompound) passenger).removeTag("UUIDLeast");
							((NBTTagCompound) passenger).removeTag("UUIDMost");
							((NBTTagCompound) passenger).removeTag("Pos");
						}
					}
					NBTTagCompound spawnPotential = new NBTTagCompound();
					spawnPotential.setInteger("Weight", 1);
					spawnPotential.setTag("Entity", entities[i]);
					spawnPotentials.appendTag(spawnPotential);
				}
			}
			compound.setTag("SpawnPotentials", spawnPotentials);
			compound.removeTag("SpawnData");

			// Store default settings into NBT
			if (spawnerSettingsOverrides != null) {
				compound.setInteger("MinSpawnDelay", spawnerSettingsOverrides.getInteger("MinSpawnDelay"));
				compound.setInteger("MaxSpawnDelay", spawnerSettingsOverrides.getInteger("MaxSpawnDelay"));
				compound.setInteger("SpawnCount", spawnerSettingsOverrides.getInteger("SpawnCount"));
				compound.setInteger("MaxNearbyEntities", spawnerSettingsOverrides.getInteger("MaxNearbyEntities"));
				compound.setInteger("SpawnRange", spawnerSettingsOverrides.getInteger("SpawnRange"));
				compound.setInteger("RequiredPlayerRange", spawnerSettingsOverrides.getInteger("RequiredPlayerRange"));
			}

			// Read data from modified nbt
			tileEntityMobSpawner.readFromNBT(compound);

			tileEntityMobSpawner.markDirty();
		} else {
			TileEntitySpawner tileEntitySpawner = (TileEntitySpawner) tileEntity;

			for (int i = 0; i < entities.length && i < 9; i++) {
				if (entities[i] != null) {
					tileEntitySpawner.inventory.setStackInSlot(i, getSoulBottleItemStackForEntity(entities[i]));
				}
			}

			tileEntitySpawner.markDirty();
		}
	}

	/**
	 * Places a vanilla spawner in the provided world at the provided position using the provided ResourceLocation for the
	 * entity that it should spawn.
	 */
	public static void createSimpleMultiUseSpawner(World world, BlockPos pos, ResourceLocation entityResLoc) {
		world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState());
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(pos);

		spawner.getSpawnerBaseLogic().setEntityId(entityResLoc);

		spawner.updateContainingBlockInfo();
		spawner.update();
	}

	public static TileEntityMobSpawner getSpawnerTile(World world, ResourceLocation entity, BlockPos pos) {
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(pos);
		spawner.getSpawnerBaseLogic().setEntityId(entity);
		return spawner;
	}

	/**
	 * Overloaded variant of normal createSimpleMultiUseSpawner method that accepts an Entity object rather than a resource
	 * location in its parameter
	 */
	public static void createSimpleMultiUseSpawner(World world, BlockPos pos, Entity entity) {
		createSimpleMultiUseSpawner(world, pos, EntityList.getKey(entity));
	}

	/*
	 * CQR/Vanilla Conversion
	 */

	/**
	 * Converts the CQR spawner at the provided World/BlockPos to a vanilla spawner
	 * 
	 * @param spawnerSettings
	 */
	public static void convertCQSpawnerToVanillaSpawner(World world, BlockPos pos, @Nullable NBTTagCompound spawnerSettings) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntitySpawner) {
			TileEntitySpawner spawner = (TileEntitySpawner) tile;

			// Entity[] entities = new Entity[spawner.inventory.getSlots()];
			NBTTagCompound[] entities = new NBTTagCompound[spawner.inventory.getSlots()];
			// Random rand = new Random();

			for (int i = 0; i < entities.length; i++) {
				ItemStack stack = spawner.inventory.extractItem(i, spawner.inventory.getStackInSlot(i).getCount(), false);// getStackInSlot(i);
				if (stack != null && !stack.isEmpty() && stack.getCount() >= 1) {
					try {
						NBTTagCompound tag = stack.getTagCompound();

						// NBTTagCompound entityTag = (NBTTagCompound)tag.getTag("EntityIn");
						entities[i] = tag.getCompoundTag("EntityIn");
						/*
						 * entities[i] = createEntityFromNBTWithoutSpawningIt(entityTag, world);
						 * 
						 * entities[i].setUniqueId(MathHelper.getRandomUUID(rand));
						 */
					} catch (NullPointerException ignored) {
					}
				} else {
					entities[i] = null;
				}
			}
			world.setBlockToAir(pos);

			placeSpawner(entities, true, spawnerSettings, world, pos);
		}
	}

	/**
	 * Converts the vanilla spawner at the provided World/BlockPos to a CQR spawner
	 */
	public static void convertVanillaSpawnerToCQSpawner(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner spawnerMultiUseTile = (TileEntityMobSpawner) tile;

			List<WeightedSpawnerEntity> spawnerEntries = new ArrayList<>();
			spawnerEntries = ObfuscationReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, spawnerMultiUseTile.getSpawnerBaseLogic(),
					1 /* It is an array index of getDeclaredFields() */);
			if (spawnerEntries != null && !spawnerEntries.isEmpty()) {
				Iterator<WeightedSpawnerEntity> iterator = spawnerEntries.iterator();

				// Entity[] entities = new Entity[9];
				NBTTagCompound[] entityCompound = new NBTTagCompound[9];

				int entriesRead = 0;
				while (entriesRead < 9 && iterator.hasNext()) {
					/*
					 * Entity entity = createEntityFromNBTWithoutSpawningIt(iterator.next().getNbt(), world); entities[entriesRead] =
					 * entity;
					 */
					entityCompound[entriesRead] = iterator.next().getNbt();
					entriesRead++;
				}
				// placeSpawner(entities, false, null, world, pos);
				placeSpawner(entityCompound, false, null, world, pos);
			}
		}
	}

	/*
	 * Miscellaneous
	 */

	/**
	 * Converts provided NBT data into an entity in the provided world without actually spawning it
	 * 
	 * @return Generated entity object
	 */
	public static Entity createEntityFromNBTWithoutSpawningIt(NBTTagCompound tag, World worldIn) {
		Entity entity = EntityList.createEntityFromNBT(tag, worldIn);
		entity.readFromNBT(tag);

		return entity;
	}

	public static NBTTagCompound createSpawnerNBTFromEntity(Entity entity) {
		return createSpawnerNBTFromEntity(entity, (!(entity instanceof AbstractEntityCQRBoss) && entity.isNonBoss()));
	}

	public static NBTTagCompound createSpawnerNBTFromEntity(Entity entity, boolean removeUUID) {
		NBTTagCompound entityCompound = new NBTTagCompound();
		if (entity instanceof AbstractEntityCQR) {
			// ((AbstractEntityCQR) entity).onPutInSpawner();
		}
		entity.writeToNBTOptional(entityCompound);
		if (removeUUID) {
			entityCompound.removeTag("UUIDLeast");
			entityCompound.removeTag("UUIDMost");
		}
		entityCompound.removeTag("Pos");
		NBTTagList passengerList = entityCompound.getTagList("Passengers", 10);
		for (NBTBase passengerTag : passengerList) {
			if (removeUUID) {
				((NBTTagCompound) passengerTag).removeTag("UUIDLeast");
				((NBTTagCompound) passengerTag).removeTag("UUIDMost");
			}
			((NBTTagCompound) passengerTag).removeTag("Pos");
		}

		return entityCompound;
	}

	/**
	 * Used internally for the placeSpawner method
	 */
	public static ItemStack getSoulBottleItemStackForEntity(Entity entity) {
		if (entity == null) {
			return null;
		}
		NBTTagCompound entityTag = new NBTTagCompound();
		if (entity.writeToNBTOptional(entityTag)) {
			return getSoulBottleItemStackForEntity(entityTag);
		}
		return null;

	}

	public static ItemStack getSoulBottleItemStackForEntity(NBTTagCompound entityTag) {
		ItemStack bottle = new ItemStack(CQRItems.SOUL_BOTTLE);
		bottle.setCount(1);
		NBTTagCompound mobToSpawnerItem = new NBTTagCompound();

		mobToSpawnerItem.setTag("EntityIn", entityTag);
		bottle.setTagCompound(mobToSpawnerItem);
		return bottle;
	}

}
