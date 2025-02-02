package team.cqr.cqrepoured.entity.ai;

import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.BlockPosUtil;

public class EntityAIFireFighter extends AbstractCQREntityAI<AbstractEntityCQR> {

	private static final int SEARCH_RADIUS_HORIZONTAL = 16;
	private static final int SEARCH_RADIUS_VERTICAL = 2;
	private static final double REACH_DISTANCE_SQ = 3.0D * 3.0D;
	private BlockPos nearestFire = null;
	private int lastTickStarted = Integer.MIN_VALUE;

	public EntityAIFireFighter(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean canUse() {
		if (!this.entity.canPutOutFire()) {
			return false;
		}

		if (this.random.nextInt(this.lastTickStarted + 60 >= this.entity.ticksExisted ? 5 : 20) == 0) {
			BlockPos pos = new BlockPos(this.entity);
			Vector3d vec = this.entity.getPositionEyes(1.0F);
			this.nearestFire = BlockPosUtil.getNearest(this.world, pos.getX(), pos.getY() + (MathHelper.ceil(this.entity.height) >> 1), pos.getZ(), SEARCH_RADIUS_HORIZONTAL, SEARCH_RADIUS_VERTICAL, true, true, Blocks.FIRE, (mutablePos, state) -> {
				mutablePos.setY(mutablePos.getY() - 1);
				if (this.world.getBlockState(mutablePos).getBlock().isFireSource(this.world, mutablePos, Direction.UP)) {
					return false;
				}
				mutablePos.setY(mutablePos.getY() + 1);
				RayTraceResult result = this.world.rayTraceBlocks(vec, new Vector3d(mutablePos.getX() + 0.5D, mutablePos.getY() + 0.5D, mutablePos.getZ() + 0.5D), false, true, false);
				return result == null || result.getBlockPos().equals(mutablePos);
			});
		}

		return this.nearestFire != null;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.nearestFire == null) {
			return false;
		}
		if (this.entity.ticksExisted % 10 == 0 && this.entity.world.getBlockState(this.nearestFire).getBlock() != Blocks.FIRE) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void start() {
		if (this.entity.getDistanceSqToCenter(this.nearestFire) > REACH_DISTANCE_SQ) {
			this.entity.getNavigator().tryMoveToXYZ(this.nearestFire.getX(), this.nearestFire.getY(), this.nearestFire.getZ(), 1.0D);
		}
		this.lastTickStarted = this.entity.ticksExisted;
	}

	@Override
	public void stop() {
		this.nearestFire = null;
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void tick() {
		if (this.entity.getDistanceSqToCenter(this.nearestFire) <= REACH_DISTANCE_SQ) {
			if (this.entity.world.getBlockState(this.nearestFire).getBlock() == Blocks.FIRE) {
				this.entity.world.setBlockToAir(this.nearestFire);
				((ServerWorld) this.entity.world).spawnParticle(ParticleTypes.SMOKE_NORMAL, this.nearestFire.getX() + 0.5D, this.nearestFire.getY() + 0.5D, this.nearestFire.getZ() + 0.5D, 4, 0.25D, 0.25D, 0.25D, 0.0D);
				this.entity.world.playSound(null, this.nearestFire.getX() + 0.5D, this.nearestFire.getY() + 0.5D, this.nearestFire.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, this.entity.getSoundSource(), 1.0F, 0.9F + this.entity.getRNG().nextFloat() * 0.2F);
			}
			this.nearestFire = null;
		}
	}

}
