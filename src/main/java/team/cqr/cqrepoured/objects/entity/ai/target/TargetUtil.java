package team.cqr.cqrepoured.objects.entity.ai.target;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.factions.FactionRegistry;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.objects.entity.IMechanical;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.bases.EntityCQRMountBase;

public class TargetUtil {

	public static final Predicate<EntityLivingBase> PREDICATE_ATTACK_TARGET = input -> {
		if (input == null) {
			return false;
		}
		return EntitySelectors.CAN_AI_TARGET.apply(input);
	};
	
	public static final Predicate<EntityLivingBase> PREDICATE_CAN_BE_ELECTROCUTED = input -> {
		if(input == null || input.isDead) {
			return false;
		}
		if(!input.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			return false;
		}
		CapabilityElectricShock icapability = input.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
		if(input instanceof IMechanical || input.getCreatureAttribute() == CQRCreatureAttributes.CREATURE_TYPE_MECHANICAL) {
			return input.isWet(); 
		}
		if(icapability.isElectrocutionActive()) {
			return false;
		}
		if(icapability.getCooldown() > 0) {
			return false;
		}
		return true;
	};
	
	public static final Predicate<EntityLivingBase> PREDICATE_IS_ELECTROCUTED = input -> {
		if(input == null || input.isDead) {
			return false;
		}
		if(!input.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			return false;
		}
		CapabilityElectricShock icapability = input.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
		return icapability.isElectrocutionActive();
	};

	public static final Predicate<EntityLiving> PREDICATE_MOUNTS = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		if (input.isBeingRidden()) {
			return false;
		}
		/*
		 * if (input instanceof AbstractHorse && ((AbstractHorse) input).isTame()) {
		 * return false;
		 * }
		 */
		return input.canBeSteered() || input instanceof EntityCQRMountBase || input instanceof AbstractHorse /* || input instanceof EntityPig */;
	};

	public static final Predicate<EntityTameable> PREDICATE_PETS = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		if (input.getOwnerId() != null) {
			return false;
		}
		return input instanceof EntityOcelot || input instanceof EntityWolf;
	};

	public static final Predicate<Entity> PREDICATE_LIVING = input -> {
		if (input == null) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		return input instanceof EntityLivingBase;
	};

	public static final Predicate<Entity> createPredicateAlly(CQRFaction faction) {
		return input -> faction.isAlly(input);
	}

	public static final Predicate<Entity> createPredicateNonAlly(CQRFaction faction) {
		return input -> !faction.isAlly(input);
	}

	public static final <T extends Entity> T getNearestEntity(EntityLiving entity, List<T> list) {
		T nearestEntity = null;
		double min = Double.MAX_VALUE;
		for (T otherEntity : list) {
			double distance = entity.getDistanceSq(otherEntity);
			if (distance < min) {
				nearestEntity = otherEntity;
				min = distance;
			}
		}
		return nearestEntity;
	}

	@Nullable
	public static final Vec3d getPositionNearTarget(World world, EntityLiving entity, BlockPos target, double minDist, double dxz, double dy) {
		return getPositionNearTarget(world, entity, new Vec3d(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D), minDist, dxz, dy);
	}

	@Nullable
	public static final Vec3d getPositionNearTarget(World world, EntityLiving entity, Entity target, double minDist, double dxz, double dy) {
		return getPositionNearTarget(world, entity, target.getPositionVector(), minDist, dxz, dy);
	}

	@Nullable
	public static final Vec3d getPositionNearTarget(World world, EntityLiving entity, Vec3d target, double minDist, double dxz, double dy) {
		return getPositionNearTarget(world, entity, target, target, minDist, dxz, dy);
	}

	@Nullable
	public static final Vec3d getPositionNearTarget(World world, EntityLiving entity, Vec3d target, Vec3d vec, double minDist, double dxz, double dy) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		int tries = 200;
		for (int i = 0; i < tries; i++) {
			double x = target.x + world.rand.nextDouble() * dxz * 2.0D - dxz;
			double y = target.y + 1.0D + world.rand.nextDouble() * dy * 2.0D - dy;
			double z = target.z + world.rand.nextDouble() * dxz * 2.0D - dxz;
			if (i < tries * 3 / 5 && (x - vec.x) * (x - vec.x) + (z - vec.z) * (z - vec.z) < minDist * minDist) {
				continue;
			}
			boolean flag = false;
			mutablePos.setPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
			for (int k = 0; k < 4; k++) {
				IBlockState state = world.getBlockState(mutablePos);
				if (state.getMaterial().blocksMovement()) {
					AxisAlignedBB aabb = state.getBoundingBox(world, mutablePos);
					if (y >= mutablePos.getY() + aabb.maxY) {
						y = mutablePos.getY() + aabb.maxY;
						flag = true;
						break;
					}
				}
				mutablePos.setY(mutablePos.getY() - 1);
			}
			if (!flag) {
				continue;
			}
			if (world.collidesWithAnyBlock(entity.getEntityBoundingBox().offset(x - entity.posX, y - entity.posY, z - entity.posZ))) {
				continue;
			}
			if (i < tries * 3 / 5) {
				double oldX = entity.posX;
				double oldY = entity.posY;
				double oldZ = entity.posZ;
				entity.setPosition(x, y, z);
				entity.onGround = true;
				Path path = entity.getNavigator().getPathToXYZ(vec.x, vec.y, vec.z);
				int l = path != null ? path.getCurrentPathLength() : 100;
				entity.setPosition(oldX, oldY, oldZ);
				if (l > dxz * 2) {
					continue;
				}
			}
			return new Vec3d(x, y, z);
		}
		return null;
	}

	public static class Sorter implements Comparator<Entity> {

		private final Entity entity;

		public Sorter(Entity entityIn) {
			this.entity = entityIn;
		}

		@Override
		public int compare(Entity entity1, Entity entity2) {
			double d1 = this.entity.getDistanceSq(entity1);
			double d2 = this.entity.getDistanceSq(entity2);

			if (d1 < d2) {
				return -1;
			} else if (d1 > d2) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	public static boolean areInSameParty(Entity ent1, Entity ent2) {
		if (ent1 instanceof AbstractEntityCQR) {
			return (ent2 instanceof AbstractEntityCQR && ((AbstractEntityCQR) ent2).getLeader() == ((AbstractEntityCQR) ent1).getLeader());
		}
		return false;
	}

	public static boolean isAllyCheckingLeaders(EntityLivingBase entity, EntityLivingBase target) {
		EntityLivingBase leader = getLeaderOrOwnerRecursive(entity);
		if (leader instanceof EntityPlayer) {
			entity = leader;
		}
		EntityLivingBase targetLeader = getLeaderOrOwnerRecursive(target);
		if (targetLeader instanceof EntityPlayer) {
			target = targetLeader;
		}

		if (entity == target) {
			return true;
		}

		if (entity instanceof EntityPlayer) {
			if (target instanceof EntityPlayer) {
				// TODO add coop/pvp mode?
				return false;
			} else {
				EntityLivingBase temp = entity;
				entity = target;
				target = temp;
			}
		}

		CQRFaction faction = FactionRegistry.instance().getFactionOf(entity);
		if (faction.isAlly(target)) {
			return true;
		}

		return false;
	}

	public static boolean isEnemyCheckingLeaders(EntityLivingBase entity, EntityLivingBase target) {
		EntityLivingBase leader = getLeaderOrOwnerRecursive(entity);
		if (leader instanceof EntityPlayer) {
			entity = leader;
		}
		EntityLivingBase targetLeader = getLeaderOrOwnerRecursive(target);
		if (targetLeader instanceof EntityPlayer) {
			target = targetLeader;
		}

		if (entity == target) {
			return false;
		}

		if (entity instanceof EntityPlayer) {
			if (target instanceof EntityPlayer) {
				// TODO add coop/pvp mode?
				return false;
			} else {
				EntityLivingBase temp = entity;
				entity = target;
				target = temp;
			}
		}

		CQRFaction faction = FactionRegistry.instance().getFactionOf(entity);
		if (target instanceof EntityPlayer && faction == FactionRegistry.DUMMY_FACTION) {
			if (!(entity instanceof EntityMob)) {
				return false;
			}
		} else {
			if (!faction.isEnemy(target)) {
				return false;
			}
		}

		return true;
	}

	public static EntityLivingBase getLeaderOrOwnerRecursive(EntityLivingBase entity) {
		int i = 10;
		while (i-- > 0) {
			if (entity instanceof AbstractEntityCQR && ((AbstractEntityCQR) entity).hasLeader()) {
				entity = ((AbstractEntityCQR) entity).getLeader();
				continue;
			}
			if (entity instanceof IEntityOwnable && ((IEntityOwnable) entity).getOwner() instanceof EntityLivingBase) {
				entity = (EntityLivingBase) ((IEntityOwnable) entity).getOwner();
				continue;
			}
			break;
		}
		return entity;
	}

}
