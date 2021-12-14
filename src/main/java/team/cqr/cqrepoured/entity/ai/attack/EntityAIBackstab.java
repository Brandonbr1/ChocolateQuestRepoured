package team.cqr.cqrepoured.entity.ai.attack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.sword.ItemDagger;

public class EntityAIBackstab extends EntityAIAttack {

	public EntityAIBackstab(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getHeldItemMainhand().getItem() instanceof ItemDagger && super.shouldExecute();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.getHeldItemMainhand().getItem() instanceof ItemDagger && super.shouldContinueExecuting();
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.setSneaking(false);
	}

	@Override
	public void updateTask() {
		super.updateTask();

		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (attackTarget instanceof AbstractEntityCQR) {
			AbstractEntityCQR target = (AbstractEntityCQR) attackTarget;
			boolean flag = this.entity.getDistanceSq(target) < 20.0D * 20.0D && target.getEntitySenses().canSee(this.entity) && !target.isEntityInFieldOfView(this.entity);
			this.entity.setSneaking(flag);
		}
	}

	@Override
	protected void updatePath(EntityLivingBase target) {
		double distance = Math.min(4.0D, this.entity.getDistance(target.posX, target.posY, target.posZ) * 0.5D);
		double rad = Math.toRadians(target.rotationYaw);
		double sin = MathHelper.sin((float) rad);
		double cos = MathHelper.cos((float) rad);
		PathNavigate navigator = this.entity.getNavigator();
		Path path = null;
		for (int i = 4; path == null && i >= 0; i--) {
			double d = distance * i / 4.0D;
			path = navigator.getPathToXYZ(target.posX + sin * d, target.posY, target.posZ - cos * d);
		}
		navigator.setPath(path, 1.0D);
	}

	@Override
	protected void checkAndPerformBlock() {

	}

}
