package team.cqr.cqrepoured.entity.mobs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.faction.EDefaultFaction;
import team.cqr.cqrepoured.init.CQRLoottables;

public class EntityCQRTriton extends AbstractEntityCQR {

	public EntityCQRTriton(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Triton;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.TRITONS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_TRITON;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public int getTextureCount() {
		return 2;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.0F;
	}

}
