package team.cqr.cqrepoured.entity.ai;

import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIMoveToHome extends AbstractCQREntityAI<AbstractEntityCQR> {

	public EntityAIMoveToHome(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean canUse() {
		if (this.entity.hasHomePositionCQR()) {
			BlockPos pos = this.entity.getHomePositionCQR();
			return this.entity.blockPosition().distSqr(pos) > 16.0D;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.isPathFinding();
	}

	@Override
	public void start() {
		BlockPos pos = this.entity.getHomePositionCQR();
		this.entity.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
	}

	@Override
	public void stop() {
		this.entity.getNavigation().stop();
	}

}
