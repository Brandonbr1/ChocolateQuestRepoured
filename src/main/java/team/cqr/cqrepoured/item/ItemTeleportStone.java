package team.cqr.cqrepoured.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ItemTeleportStone extends Item {

	private String X = "x";
	private String Y = "y";
	private String Z = "z";
	private String Dimension = "dimension";

	public ItemTeleportStone() {
		this.setMaxDamage(100);

		this.setMaxStackSize(1);
	}

	@Override
	public UseAction getItemUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 40;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	/**
	 * Taken from CoFHCore's EntityHelper
	 * (https://github.com/CoFH/CoFHCore/blob/1.12/src/main/java/cofh/core/util/helpers/EntityHelper.java)
	 */
	private static void transferPlayerToDimension(ServerPlayerEntity player, int dimension, PlayerList manager) {
		int oldDim = player.dimension;
		ServerWorld oldWorld = manager.getServerInstance().getWorld(player.dimension);
		player.dimension = dimension;
		ServerWorld newWorld = manager.getServerInstance().getWorld(player.dimension);
		player.connection.sendPacket(new SRespawnPacket(player.dimension, newWorld.getDifficulty(), newWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
		oldWorld.removeEntityDangerously(player);
		if (player.isBeingRidden()) {
			player.removePassengers();
		}
		if (player.isRiding()) {
			player.dismountRidingEntity();
		}
		player.isDead = false;
		transferEntityToWorld(player, oldWorld, newWorld);
		manager.preparePlayer(player, oldWorld);
		player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.interactionManager.setWorld(newWorld);
		manager.updateTimeAndWeatherForPlayer(player, newWorld);
		manager.syncPlayerInventory(player);

		for (EffectInstance potioneffect : player.getActivePotionEffects()) {
			player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), potioneffect));
		}
		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
	}

	/**
	 * Taken from CoFHCore's EntityHelper
	 * (https://github.com/CoFH/CoFHCore/blob/1.12/src/main/java/cofh/core/util/helpers/EntityHelper.java)
	 */
	private static void transferEntityToWorld(Entity entity, ServerWorld oldWorld, ServerWorld newWorld) {
		net.minecraft.world.dimension.Dimension oldWorldProvider = oldWorld.provider;
		net.minecraft.world.dimension.Dimension newWorldProvider = newWorld.provider;
		double moveFactor = oldWorldProvider.getMovementFactor() / newWorldProvider.getMovementFactor();
		double x = entity.posX * moveFactor;
		double z = entity.posZ * moveFactor;

		oldWorld.profiler.startSection("placing");
		x = MathHelper.clamp(x, -29_999_872, 29_999_872);
		z = MathHelper.clamp(z, -29_999_872, 29_999_872);
		if (entity.isEntityAlive()) {
			entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
			newWorld.spawnEntity(entity);
			newWorld.updateEntityWithOptionalForce(entity, false);
		}
		oldWorld.profiler.endSection();

		entity.setWorld(newWorld);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
			player.getCooldownTracker().setCooldown(stack.getItem(), 60);

			if (player.isSneaking() && stack.hasTagCompound()) {
				stack.getTagCompound().removeTag(this.X);
				stack.getTagCompound().removeTag(this.Y);
				stack.getTagCompound().removeTag(this.Z);
				worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
				for (int i = 0; i < 10; i++) {
					worldIn.spawnParticle(ParticleTypes.SMOKE_LARGE, player.posX + worldIn.rand.nextDouble() - 0.5D, player.posY + 0.5D, player.posZ + worldIn.rand.nextDouble() - 0.5D, 0D, 0D, 0D);
				}
			}

			else if (this.getPoint(stack) == null || !stack.hasTagCompound()) {
				this.setPoint(stack, player);
				for (int i = 0; i < 10; i++) {
					worldIn.spawnParticle(ParticleTypes.FLAME, player.posX + worldIn.rand.nextDouble() - 0.5D, player.posY + 0.5D, player.posZ + worldIn.rand.nextDouble() - 0.5D, 0D, 0D, 0D);
				}
				worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

				return super.onItemUseFinish(stack, worldIn, entityLiving);
			}

			else if (stack.hasTagCompound() && !player.isSneaking()) {
				if (stack.getTagCompound().hasKey(this.X) && stack.getTagCompound().hasKey(this.Y) && stack.getTagCompound().hasKey(this.Z)) {
					int dimension = stack.getTagCompound().hasKey(this.Dimension, Constants.NBT.TAG_INT) ? stack.getTagCompound().getInteger(this.Dimension) : 0;
					BlockPos pos = this.getPoint(stack);

					if (player.isBeingRidden()) {
						player.removePassengers();
					}
					if (player.isRiding()) {
						player.dismountRidingEntity();
					}

					if (dimension != player.getEntityWorld().provider.getDimension()) {
						MinecraftServer server = player.world.getMinecraftServer();
						if (server != null) {
							transferPlayerToDimension(player, dimension, server.getPlayerList());
						}
					}
					player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
					// player.attemptTeleport(stack.getTagCompound().getDouble(this.X), stack.getTagCompound().getDouble(this.Y),
					// stack.getTagCompound().getDouble(this.Z));
					/*
					 * if(worldIn.provider.getDimension() != dimension) {
					 * 
					 * WorldServer worldServer = player.getServer().getWorld(dimension);
					 * WorldServer worldServerOld = player.getServerWorld();
					 * player.moveToBlockPosAndAngles(new BlockPos(stack.getTagCompound().getDouble(this.X),
					 * stack.getTagCompound().getDouble(this.Y),
					 * stack.getTagCompound().getDouble(this.Z)), player.rotationYaw, player.rotationPitch);
					 * worldServerOld.removeEntity(player);
					 * boolean flag = player.forceSpawn;
					 * player.forceSpawn = true;
					 * worldServer.spawnEntity(player);
					 * player.forceSpawn = flag;
					 * worldServer.updateEntityWithOptionalForce(player, false);
					 * 
					 * }
					 */
					// player.connection.setPlayerLocation(stack.getTagCompound().getDouble(this.X),
					// stack.getTagCompound().getDouble(this.Y),
					// stack.getTagCompound().getDouble(this.Z), player.rotationYaw, player.rotationPitch);
					for (int i = 0; i < 30; i++) {
						worldIn.spawnParticle(ParticleTypes.PORTAL, player.posX + worldIn.rand.nextDouble() - 0.5D, player.posY + 0.5D, player.posZ + worldIn.rand.nextDouble() - 0.5D, 0D, 0D, 0D);
					}
					worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);

					if (!player.capabilities.isCreativeMode) {
						stack.damageItem(1, entityLiving);
					}

					return super.onItemUseFinish(stack, worldIn, entityLiving);
				}
			}

		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.teleport_stone.name"));

			if (stack.hasTagCompound()) {
				if (stack.getTagCompound().hasKey(this.X) && stack.getTagCompound().hasKey(this.Y) && stack.getTagCompound().hasKey(this.Z)) {
					tooltip.add(TextFormatting.BLUE + I18n.format("description.teleport_stone_position.name"));
					tooltip.add(TextFormatting.BLUE + I18n.format("X: " + (int) stack.getTagCompound().getDouble(this.X)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Y: " + (int) stack.getTagCompound().getDouble(this.Y)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Z: " + (int) stack.getTagCompound().getDouble(this.Z)));
					tooltip.add(TextFormatting.BLUE + I18n.format("Dimension: " + (stack.getTagCompound().hasKey(this.Dimension, Constants.NBT.TAG_INT) ? stack.getTagCompound().getInteger(this.Dimension) : 0)));
				}
			}
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	private void setPoint(ItemStack stack, ServerPlayerEntity player) {
		CompoundNBT stone = stack.getTagCompound();

		if (stone == null) {
			stone = new CompoundNBT();
			stack.setTagCompound(stone);
		}

		if (!stone.hasKey(this.X)) {
			stone.setDouble(this.X, player.posX);
		}

		if (!stone.hasKey(this.Y)) {
			stone.setDouble(this.Y, player.posY);
		}

		if (!stone.hasKey(this.Z)) {
			stone.setDouble(this.Z, player.posZ);
		}

		// Don't re-enable this check, if it is enabled it will never trigger correctly for whatever reason
		// if (!stone.hasKey(this.Dimension)) {
		stone.setInteger(this.Dimension, player.dimension);
		// }
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return this.getPoint(stack) != null;
	}

	@Nullable
	private BlockPos getPoint(ItemStack stack) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey(this.X) && stack.getTagCompound().hasKey(this.Y) && stack.getTagCompound().hasKey(this.Z)) {
				CompoundNBT stone = stack.getTagCompound();

				double x = stone.getDouble(this.X);
				double y = stone.getDouble(this.Y);
				double z = stone.getDouble(this.Z);

				return new BlockPos(x, y, z);
			}
		}
		return null;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

}
