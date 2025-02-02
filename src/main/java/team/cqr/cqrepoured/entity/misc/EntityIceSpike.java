package team.cqr.cqrepoured.entity.misc;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.IDontRenderFire;

public class EntityIceSpike extends EvokerFangsEntity implements IDontRenderFire {

	public EntityIceSpike(EntityType<? extends EntityIceSpike> type, World p_i47275_1_) {
		super(type, p_i47275_1_);
	}

	public EntityIceSpike(World p_i47276_1_, double p_i47276_2_, double p_i47276_4_, double p_i47276_6_, float p_i47276_8_, int p_i47276_9_, LivingEntity p_i47276_10_) {
		super(p_i47276_1_, p_i47276_2_, p_i47276_4_, p_i47276_6_, p_i47276_8_, p_i47276_9_, p_i47276_10_);
	}

}
