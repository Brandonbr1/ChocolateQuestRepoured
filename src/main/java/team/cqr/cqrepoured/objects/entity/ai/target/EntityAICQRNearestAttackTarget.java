package team.cqr.cqrepoured.objects.entity.ai.target;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.items.IFakeWeapon;
import team.cqr.cqrepoured.objects.items.ISupportWeapon;

public class EntityAICQRNearestAttackTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

	public EntityAICQRNearestAttackTarget(AbstractEntityCQR entity) {
		super(entity);
	}
	
	protected void wrapperSetAttackTarget(EntityLivingBase target) {
		this.entity.setAttackTarget(target);
	}
	
	protected EntityLivingBase wrapperGetAttackTarget() {
		return this.entity.getAttackTarget();
	}

	@Override
	public boolean shouldExecute() {
		if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			this.wrapperSetAttackTarget(null);
			return false;
		}
		if (this.isStillSuitableTarget(this.wrapperGetAttackTarget())) {
			return false;
		}
		this.wrapperSetAttackTarget(null);
		return this.random.nextInt(3) == 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	@Override
	public void startExecuting() {
		AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32.0D);
		List<EntityLivingBase> possibleTargets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		List<EntityLivingBase> possibleTargetsAlly = new ArrayList<>();
		List<EntityLivingBase> possibleTargetsEnemy = new ArrayList<>();
		this.fillLists(possibleTargets, possibleTargetsAlly, possibleTargetsEnemy);
		if (!possibleTargetsAlly.isEmpty()) {
			this.wrapperSetAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargetsAlly));
		} else if (!possibleTargetsEnemy.isEmpty()) {
			this.wrapperSetAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargetsEnemy));
		}
	}

	protected void fillLists(List<EntityLivingBase> list, List<EntityLivingBase> allies, List<EntityLivingBase> enemies) {
		boolean canTargetAlly = this.canTargetAlly();
		for (EntityLivingBase possibleTarget : list) {
			if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
				continue;
			}
			if (!EntitySelectors.IS_ALIVE.apply(possibleTarget)) {
				continue;
			}
			if (possibleTarget == this.entity) {
				continue;
			}
			if (canTargetAlly && this.isSuitableTargetAlly(possibleTarget)) {
				allies.add(possibleTarget);
			} else if (this.isSuitableTargetEnemy(possibleTarget)) {
				enemies.add(possibleTarget);
			}
		}
	}

	protected boolean canTargetAlly() {
		Item item = this.entity.getHeldItemMainhand().getItem();
		return item instanceof ISupportWeapon<?> || item instanceof IFakeWeapon<?>;
	}

	protected boolean isSuitableTargetAlly(EntityLivingBase possibleTarget) {
		CQRFaction faction = this.entity.getFaction();
		if (faction == null) {
			return false;
		}
		if (!TargetUtil.isAllyCheckingLeaders(this.entity, possibleTarget)) {
			return false;
		}
		if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
			return false;
		}
		if (!this.entity.isInSightRange(possibleTarget)) {
			return false;
		}
		return this.entity.getEntitySenses().canSee(possibleTarget);
	}

	protected boolean isSuitableTargetEnemy(EntityLivingBase possibleTarget) {
		if (!TargetUtil.isEnemyCheckingLeaders(this.entity, possibleTarget)) {
			return false;
		}
		if (!this.entity.getEntitySenses().canSee(possibleTarget)) {
			return false;
		}
		if (this.entity.isInAttackReach(possibleTarget)) {
			return true;
		}
		if (this.entity.isEntityInFieldOfView(possibleTarget)) {
			return this.entity.isInSightRange(possibleTarget);
		}
		return !possibleTarget.isSneaking() && this.entity.getDistanceSq(possibleTarget) < 12.0D * 12.0D;
	}

	protected boolean isStillSuitableTarget(EntityLivingBase possibleTarget) {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		if (this.entity.getDistanceSq(possibleTarget) > 64.0D * 64.0D) {
			return false;
		}
		if (TargetUtil.isAllyCheckingLeaders(this.entity, possibleTarget)) {
			if (!this.canTargetAlly()) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
		} else if (this.canTargetAlly()) {
			AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32.0D);
			List<EntityLivingBase> possibleTargets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
			for (EntityLivingBase possibleTargetAlly : possibleTargets) {
				if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTargetAlly)) {
					continue;
				}
				if (!EntitySelectors.IS_ALIVE.apply(possibleTargetAlly)) {
					continue;
				}
				if (possibleTargetAlly == this.entity) {
					continue;
				}
				if (this.isSuitableTargetAlly(possibleTargetAlly)) {
					return false;
				}
			}
		}
		return true;
	}

}
