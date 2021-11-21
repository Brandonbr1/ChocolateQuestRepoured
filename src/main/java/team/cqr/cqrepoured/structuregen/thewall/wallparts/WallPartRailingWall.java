package team.cqr.cqrepoured.structuregen.thewall.wallparts;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.factories.SpawnerFactory;
import team.cqr.cqrepoured.objects.items.armor.ItemArmorDyable;
import team.cqr.cqrepoured.structuregen.generation.GeneratableDungeon;
import team.cqr.cqrepoured.structuregen.generation.part.BlockDungeonPart;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparableSpawnerInfo;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WallPartRailingWall implements IWallPart {

	@Override
	public int getTopY() {
		return CQRConfig.wall.topY - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, GeneratableDungeon.Builder dungeonBuilder) {
		int startX = chunkX * 16 + 8;
		int startZ = chunkZ * 16;
		int startY = this.getTopY();

		BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		IBlockState stateBlock = Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE)
				.withProperty(BlockStoneSlab.SEAMLESS, true);

		int[] zValues = new int[] { 2, 3, 12, 13 };
		for (int y = 0; y < 8; y++) {
			for (int z : zValues) {
				for (int x = 0; x < 8; x++) {
					if (this.isBiggerPart(x)) {
						if (y >= 3 || z == 3 || z == 12) {
							partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
							partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
						}
					} else if (y >= 4 && y <= 6 && (z == 3 || z == 12)) {
						partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2, y, z), stateBlock, null));
						partBuilder.add(new PreparableBlockInfo(new BlockPos(x * 2 + 1, y, z), stateBlock, null));
					}
				}
			}
		}

		// Spawner
		this.placeSpawner(new BlockPos(4, 6, 7), world, partBuilder);

		dungeonBuilder.add(partBuilder, dungeonBuilder.getPlacement(new BlockPos(startX, startY, startZ)));
	}

	private boolean isBiggerPart(int xAsChunkRelativeCoord) {
		return (xAsChunkRelativeCoord & 1) == 0;
	}

	private void placeSpawner(BlockPos spawnerPos, World world, BlockDungeonPart.Builder partBuilder) {
		Entity spawnerEnt = EntityList.createEntityByIDFromName(new ResourceLocation(CQRConfig.wall.mob), world);

		if (spawnerEnt instanceof EntityLivingBase) {
			EnumDifficulty difficulty = world.getDifficulty();
			this.equipWeaponBasedOnDifficulty((EntityLivingBase) spawnerEnt, difficulty);
			this.equipArmorBasedOnDifficulty((EntityLivingBase) spawnerEnt, difficulty);

			if (spawnerEnt instanceof AbstractEntityCQR) {
				((AbstractEntityCQR) spawnerEnt).setHealingPotions(1);
			}

			IBlockState state2 = CQRBlocks.SPAWNER.getDefaultState();
			TileEntitySpawner tileSpawner = (TileEntitySpawner) CQRBlocks.SPAWNER.createTileEntity(world, state2);
			tileSpawner.inventory.setStackInSlot(0, SpawnerFactory.getSoulBottleItemStackForEntity(spawnerEnt));

			partBuilder.add(new PreparableSpawnerInfo(spawnerPos, tileSpawner.writeToNBT(new NBTTagCompound())));
		}
	}

	private void equipWeaponBasedOnDifficulty(EntityLivingBase entity, EnumDifficulty difficulty) {
		switch (entity.getRNG().nextInt(5)) {
		case 0:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER, 1));
			break;
		case 1:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE, 1));
			entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER, 1));
			break;
		case 2:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW, 1));
			break;
		case 3:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW, 1));
			entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(CQRItems.SHIELD_SPECTER, 1));
			break;
		case 4:
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.DIAMOND_SWORD, 1));
			break;
		}
	}

	private void equipArmorBasedOnDifficulty(EntityLivingBase entity, EnumDifficulty difficulty) {
		ItemStack helm = new ItemStack(CQRItems.HELMET_IRON_DYABLE);
		ItemStack chest = new ItemStack(CQRItems.CHESTPLATE_IRON_DYABLE);
		ItemStack legs = new ItemStack(CQRItems.LEGGINGS_IRON_DYABLE);
		ItemStack feet = new ItemStack(CQRItems.BOOTS_IRON_DYABLE);

		if (difficulty == EnumDifficulty.HARD) {
			if (entity.getRNG().nextDouble() < 0.35D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE);
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE);
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE);
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE);
			}
		} else if (difficulty == EnumDifficulty.NORMAL) {
			if (entity.getRNG().nextDouble() < 0.25D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE);
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE);
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE);
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE);
			}
		} else {
			if (entity.getRNG().nextDouble() < 0.2D) {
				helm = new ItemStack(CQRItems.HELMET_DIAMOND_DYABLE);
				chest = new ItemStack(CQRItems.CHESTPLATE_DIAMOND_DYABLE);
				legs = new ItemStack(CQRItems.LEGGINGS_DIAMOND_DYABLE);
				feet = new ItemStack(CQRItems.BOOTS_DIAMOND_DYABLE);
			}
		}

		if (entity.getRNG().nextDouble() < 0.005D) {
			((ItemArmorDyable) helm.getItem()).setColor(helm, 0x1008FFFF);
			((ItemArmorDyable) helm.getItem()).setColor(chest, 0x1008FFFF);
			((ItemArmorDyable) helm.getItem()).setColor(legs, 0x1008FFFF);
			((ItemArmorDyable) helm.getItem()).setColor(feet, 0x1008FFFF);
		} else {
			((ItemArmorDyable) helm.getItem()).setColor(helm, 0);
			((ItemArmorDyable) helm.getItem()).setColor(chest, 0);
			((ItemArmorDyable) helm.getItem()).setColor(legs, 0);
			((ItemArmorDyable) helm.getItem()).setColor(feet, 0);
		}

		entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, helm);
		entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, chest);
		entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, legs);
		entity.setItemStackToSlot(EntityEquipmentSlot.FEET, feet);
	}

}
