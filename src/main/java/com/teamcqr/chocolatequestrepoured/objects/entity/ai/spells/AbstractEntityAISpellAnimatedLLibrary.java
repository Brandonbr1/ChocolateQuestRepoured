package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.util.SoundEvent;

public abstract class AbstractEntityAISpellAnimatedLLibrary<T extends AbstractEntityCQR & IAnimatedEntity> extends AnimationAI<T> implements IEntityAISpell {

	protected final Random random = new Random();
	protected final boolean needsTargetToStart;
	protected final boolean needsTargetToContinue;
	protected final int cooldown;
	protected final int chargingTicks;
	protected final int castingTicks;
	protected int prevTimeCasted;
	protected int tick;

	public AbstractEntityAISpellAnimatedLLibrary(T entity, boolean needsTargetToStart, boolean needsTargetToContinue, int cooldown, int chargingTicks, int castingTicks) {
		super(entity);
		this.needsTargetToStart = needsTargetToStart;
		this.needsTargetToContinue = needsTargetToContinue;
		this.cooldown = cooldown;
		this.chargingTicks = Math.max(chargingTicks, 0);
		this.castingTicks = Math.max(castingTicks, 1);
		this.prevTimeCasted = -this.random.nextInt(cooldown + 1) - 1;
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.isEntityAlive()) {
			return false;
		}
		if (this.needsTargetToStart && this.entity.getAttackTarget() == null) {
			return false;
		}
		return this.entity.ticksExisted > this.prevTimeCasted + this.cooldown;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.entity.isEntityAlive()) {
			return false;
		}
		if (this.needsTargetToContinue && this.entity.getAttackTarget() == null) {
			return false;
		}
		return this.tick < this.chargingTicks + this.castingTicks;
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void startExecuting() {
		this.tick = 0;
	}

	@Override
	public void resetTask() {
		this.prevTimeCasted = this.entity.ticksExisted;
		this.tick = -1;
	}

	@Override
	public void updateTask() {
		if (this.tick < this.chargingTicks) {
			if (this.tick == 0) {
				this.startChargingSpell();
			}
			this.chargeSpell();
		} else {
			if (this.tick == this.chargingTicks) {
				this.startCastingSpell();
			}
			this.castSpell();
		}
		this.tick++;
	}

	@Override
	public boolean isCharging() {
		return this.tick != -1 && this.tick < this.chargingTicks;
	}

	@Override
	public boolean isCasting() {
		return this.tick != -1 && this.tick < this.chargingTicks + this.castingTicks;
	}

	protected void startChargingSpell() {
		if (this.getStartChargingSound() != null) {
			this.entity.playSound(this.getStartChargingSound(), 1.0F, 0.9F + 0.2F * this.random.nextFloat());
		}
	}

	protected void startCastingSpell() {
		if (this.getStartCastingSound() != null) {
			this.entity.playSound(this.getStartCastingSound(), 1.0F, 0.9F + 0.2F * this.random.nextFloat());
		}
	}

	protected void chargeSpell() {

	}

	protected void castSpell() {

	}

	@Nullable
	protected SoundEvent getStartChargingSound() {
		return null;
	}

	@Nullable
	protected SoundEvent getStartCastingSound() {
		return null;
	}

}
