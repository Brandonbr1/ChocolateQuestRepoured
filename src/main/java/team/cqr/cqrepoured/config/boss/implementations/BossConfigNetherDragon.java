package team.cqr.cqrepoured.config.boss.implementations;

import team.cqr.cqrepoured.config.boss.AbstractBossConfig;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRNetherDragon;

public class BossConfigNetherDragon extends AbstractBossConfig<EntityCQRNetherDragon> {
	//These are all the same accross all bosses
		@Override
		protected double getStandardAttackDamageDefaultValue() {
			return 2.0D;
		}
		
		@Override
		protected double getBaseSpeedDefaultValue() {
			return 0.25D;
		}

		@Override
		protected double getArmorValueDefaultValue() {
			return 0;
		}

		@Override
		protected double getArmorToughnessDefaultValue() {
			return 0;
		}

		@Override
		protected double getKnockbackResistanceDefaultValue() {
			return 0;
		}

		@Override
		protected boolean isDamageCapEnabledDefaultValue() {
			return true;
		}

		@Override
		protected float getMaxUncappedDamageDefaultValue() {
			return 30F;
		}

		@Override
		protected float getMaxDamageInPercentOfMaxHPDefaultValue() {
			return 0.1F;
		}

		@Override
		protected double getDamageReductionPerPlayerDefaultValue() {
			return 0.25D;
		}

		@Override
		protected boolean isBossBarEnabledDefaultValue() {
			return true;
		}

		@Override
		protected boolean isHealthRegenEnabledDefaultValue() {
			return true;
		}
}
