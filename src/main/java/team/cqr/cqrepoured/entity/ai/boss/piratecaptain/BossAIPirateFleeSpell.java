package team.cqr.cqrepoured.entity.ai.boss.piratecaptain;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;

public class BossAIPirateFleeSpell extends AbstractEntityAISpell<EntityCQRPirateCaptain> implements IEntityAISpellAnimatedVanilla {

	protected final Predicate<EntityLiving> predicateAlly = input -> {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		return BossAIPirateFleeSpell.this.isSuitableAlly(input);
	};

	public BossAIPirateFleeSpell(EntityCQRPirateCaptain entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
		this.setup(true, true, true, false);
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute() && this.entity.getHealingPotions() <= 1 && this.entity.getHealth() / this.entity.getMaxHealth() <= 0.2) {
			return this.hasNearbyAllies();
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.hasNearbyAllies();
	}

	private boolean hasNearbyAllies() {
		Vec3d vec = new Vec3d(CQRConfig.bosses.pirateCaptainFleeCheckRadius, 0.5 * CQRConfig.bosses.pirateCaptainFleeCheckRadius, CQRConfig.bosses.pirateCaptainFleeCheckRadius);
		Vec3d v1 = this.entity.getPositionVector().add(vec);
		Vec3d v2 = this.entity.getPositionVector().subtract(vec);
		AxisAlignedBB aabb = new AxisAlignedBB(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);

		List<EntityLiving> allies = this.entity.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this.predicateAlly);
		return !allies.isEmpty();
	}

	private int getNearbyAllies(EntityLiving o1) {
		Vec3d vec = new Vec3d(4, 2, 4);
		Vec3d v1 = o1.getPositionVector().add(vec);
		Vec3d v2 = o1.getPositionVector().subtract(vec);
		AxisAlignedBB aabb = new AxisAlignedBB(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
		return o1.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this.predicateAlly).size();
	}

	@Override
	public void castSpell() {
		super.castSpell();

		Vec3d vec = new Vec3d(CQRConfig.bosses.pirateCaptainFleeCheckRadius, 0.5 * CQRConfig.bosses.pirateCaptainFleeCheckRadius, CQRConfig.bosses.pirateCaptainFleeCheckRadius);
		Vec3d v1 = this.entity.getPositionVector().add(vec);
		Vec3d v2 = this.entity.getPositionVector().subtract(vec);
		AxisAlignedBB aabb = new AxisAlignedBB(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);

		List<EntityLiving> allies = this.entity.world.getEntitiesWithinAABB(EntityLiving.class, aabb, this.predicateAlly);
		allies.sort((o1, o2) -> {
			int entCount1 = BossAIPirateFleeSpell.this.getNearbyAllies(o1);
			int entCount2 = BossAIPirateFleeSpell.this.getNearbyAllies(o2);
			return entCount2 - entCount1;
		});
		Vec3d p = allies.get(0).getPositionVector();
		this.entity.attemptTeleport(p.x, p.y, p.z);
	}

	@Override
	public int getWeight() {
		return 2;
	}

	@Override
	public boolean ignoreWeight() {
		return true;
	}

	@Override
	public float getRed() {
		return 0;
	}

	@Override
	public float getGreen() {
		return 1;
	}

	@Override
	public float getBlue() {
		return 0;
	}

	protected boolean isSuitableAlly(EntityLiving possibleAlly) {
		if (possibleAlly == this.entity) {
			return false;
		}
		if (!this.entity.getFaction().isAlly(possibleAlly)) {
			return false;
		}
		Path path = possibleAlly.getNavigator().getPathToEntityLiving(this.entity);
		return path != null && path.getCurrentPathLength() <= 20;
	}

}
