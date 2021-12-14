package team.cqr.cqrepoured.entity.ai.target;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.EnumDifficulty;
import team.cqr.cqrepoured.entity.ICirclingEntity;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.init.CQRItems;

public class EntityAINearestAttackTargetAtHomeArea<T extends AbstractEntityCQR & ICirclingEntity> extends AbstractCQREntityAI<T> {

	protected final Predicate<EntityLivingBase> predicate = input -> {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		return EntityAINearestAttackTargetAtHomeArea.this.isSuitableTarget(input);
	};

	public EntityAINearestAttackTargetAtHomeArea(T entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			this.entity.setAttackTarget(null);
			return false;
		}
		if (this.isStillSuitableTarget(this.entity.getAttackTarget())) {
			return false;
		}
		this.entity.setAttackTarget(null);
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	private static final Vec3i SIZE_VECTOR = new Vec3i(32, 32, 32);

	@Override
	public void startExecuting() {
		AxisAlignedBB aabb = new AxisAlignedBB(this.entity.getCirclingCenter().add(SIZE_VECTOR), this.entity.getCirclingCenter().subtract(SIZE_VECTOR));
		List<EntityLivingBase> possibleTargets = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, this.predicate);
		if (!possibleTargets.isEmpty()) {
			this.entity.setAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargets));
		}
	}

	private boolean isSuitableTarget(EntityLivingBase possibleTarget) {
		if (possibleTarget == this.entity) {
			return false;
		}
		Faction faction = this.entity.getFaction();
		if (this.entity.getHeldItemMainhand().getItem() == CQRItems.STAFF_HEALING) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			/*
			 * if (!this.entity.isInSightRange(possibleTarget)) { return false; }
			 */
			// return this.entity.getEntitySenses().canSee(possibleTarget);
			return this.isInHomeZone(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		/*
		 * if (!this.entity.getEntitySenses().canSee(possibleTarget)) { return false; }
		 */
		if (this.entity.isInAttackReach(possibleTarget)) {
			return true;
		}
		/*
		 * if (this.entity.isEntityInFieldOfView(possibleTarget)) { return this.entity.isInSightRange(possibleTarget); }
		 */
		return !possibleTarget.isSneaking() && this.entity.getDistance(possibleTarget) < 32.0D;
	}

	private boolean isStillSuitableTarget(EntityLivingBase possibleTarget) {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		Faction faction = this.entity.getFaction();
		if (this.entity.getHeldItemMainhand().getItem() == CQRItems.STAFF_HEALING) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			/*
			 * if (!this.entity.isInSightRange(possibleTarget)) { return false; }
			 */
			// return this.entity.getEntitySenses().canSee(possibleTarget);
			return this.isInHomeZone(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		/*
		 * if (!this.entity.isInSightRange(possibleTarget)) { return false; } return
		 * this.entity.getEntitySenses().canSee(possibleTarget);
		 */
		return this.isInHomeZone(possibleTarget);
	}

	private boolean isInHomeZone(EntityLivingBase possibleTarget) {
		double distance = possibleTarget.getPosition().getDistance(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(),
				this.entity.getCirclingCenter().getZ());
		return Math.abs(distance) <= 48 + 8 * (this.world.getDifficulty().ordinal());
	}

}
