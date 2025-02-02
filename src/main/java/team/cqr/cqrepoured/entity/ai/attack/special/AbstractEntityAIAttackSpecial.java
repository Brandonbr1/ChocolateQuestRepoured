package team.cqr.cqrepoured.entity.ai.attack.special;

import java.util.Random;

import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public abstract class AbstractEntityAIAttackSpecial {

	protected final Random random = new Random();
	protected boolean needsTargetToContinue;
	protected boolean needsSightToContinue;
	protected final int maxUseTime;
	protected final int cooldown;

	protected AbstractEntityAIAttackSpecial(boolean needsTargetToContinue, boolean needsSightToContinue, int maxUseTime, int cooldown) {
		this.needsTargetToContinue = needsTargetToContinue;
		this.needsSightToContinue = needsSightToContinue;
		this.maxUseTime = maxUseTime;
		this.cooldown = cooldown;
	}

	public abstract boolean shouldStartAttack(AbstractEntityCQR attacker, EntityLivingBase target);

	public abstract boolean shouldContinueAttack(AbstractEntityCQR attacker, EntityLivingBase target);

	public abstract boolean isInterruptible(AbstractEntityCQR entity);

	public abstract void startAttack(AbstractEntityCQR attacker, EntityLivingBase target);

	public abstract void continueAttack(AbstractEntityCQR attacker, EntityLivingBase target, int tick);

	/**
	 * Gets called when this attack is finished.
	 */
	public abstract void stopAttack(AbstractEntityCQR attacker, EntityLivingBase target);

	/**
	 * Gets called when this attack is finished or interrupted.
	 */
	public abstract void resetAttack(AbstractEntityCQR attacker);

	public double getAttackRange(AbstractEntityCQR attacker, EntityLivingBase target) {
		return attacker.getAttackReach(target);
	}

	public double getAttackChance(AbstractEntityCQR attacker, EntityLivingBase target) {
		return 0.1D;
	}

	public boolean needsTargetToContinue() {
		return this.needsTargetToContinue;
	}

	public boolean needsSightToContinue() {
		return this.needsSightToContinue;
	}

	public int getMaxUseTime() {
		return this.maxUseTime;
	}

	public int getCooldown(AbstractEntityCQR attacker) {
		return this.cooldown;
	}

}
