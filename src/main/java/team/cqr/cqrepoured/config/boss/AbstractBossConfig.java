package team.cqr.cqrepoured.config.boss;

import net.minecraftforge.common.config.Config;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQRBoss;

@Config.RequiresWorldRestart
public abstract class AbstractBossConfig<T extends AbstractEntityCQRBoss<? extends AbstractBossConfig<T>>> {
	
	//Fields
	@Config.RangeDouble(min = 1, max = 1000)
	public float baseHealth = this.getBaseHealthDefaultValue();
	public double standardAttackDamage = this.getStandardAttackDamageDefaultValue();
	public double baseSpeed = this.getBaseSpeedDefaultValue();
	public double armorValue = this.getArmorValueDefaultValue();
	public double armorToughness = this.getArmorToughnessDefaultValue();
	public double knockBackResistance = this.getKnockbackResistanceDefaultValue();
	
	public boolean damageCapEnabled = this.isDamageCapEnabledDefaultValue();
	public float maxUncappedDamage = this.getMaxUncappedDamageDefaultValue();
	public float maxDamageInPercentOfMaxHP = this.getMaxDamageInPercentOfMaxHPDefaultValue();
	public double damageReductionPerPlayer = this.getDamageReductionPerPlayerDefaultValue();
	
	public boolean bossBarEnabled = this.isBossBarEnabledDefaultValue();
	public boolean healthRegenEnabled = this.isHealthRegenEnabledDefaultValue();
	
	//Entity attributes
	protected abstract float getBaseHealthDefaultValue();
	protected abstract double getStandardAttackDamageDefaultValue();
	protected abstract double getBaseSpeedDefaultValue();
	protected abstract double getArmorValueDefaultValue(); 
	protected abstract double getArmorToughnessDefaultValue();
	protected abstract double getKnockbackResistanceDefaultValue();

	//Damge cap stuff
	protected abstract boolean isDamageCapEnabledDefaultValue();
	protected abstract float getMaxUncappedDamageDefaultValue();
	protected abstract float getMaxDamageInPercentOfMaxHPDefaultValue();
	protected abstract double getDamageReductionPerPlayerDefaultValue();
	
	//Boss base values
	protected abstract boolean isBossBarEnabledDefaultValue();
	protected abstract boolean isHealthRegenEnabledDefaultValue();
	
	//Getters for fields
	public float getBaseHealth() {
		return baseHealth;
	}
	public double getStandardAttackDamage() {
		return standardAttackDamage;
	}
	public double getBaseSpeed() {
		return baseSpeed;
	}
	public double getArmorValue() {
		return armorValue;
	}
	public double getArmorToughness() {
		return armorToughness;
	}
	public double getKnockBackResistance() {
		return knockBackResistance;
	}
	public boolean isDamageCapEnabled() {
		return damageCapEnabled;
	}
	public float getMaxUncappedDamage() {
		return maxUncappedDamage;
	}
	public float getMaxDamageInPercentOfMaxHP() {
		return maxDamageInPercentOfMaxHP;
	}
	public double getDamageReductionPerPlayer() {
		return damageReductionPerPlayer;
	}
	public boolean isBossBarEnabled() {
		return bossBarEnabled;
	}
	public boolean isHealthRegenEnabled() {
		return healthRegenEnabled;
	}

}
