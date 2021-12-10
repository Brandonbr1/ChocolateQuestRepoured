package team.cqr.cqrepoured.entity.ai.boss.gianttortoise;

import team.cqr.cqrepoured.entity.ai.EntityAIMoveToHome;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class BossAITortoiseMoveToHome extends EntityAIMoveToHome {

	private static final int PREPARE_TIME = 60;
	private int prepareTime = PREPARE_TIME;

	public BossAITortoiseMoveToHome(EntityCQRGiantTortoise entity) {
		super(entity);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public boolean shouldExecute() {
		if (this.prepareTime > 0) {
			this.prepareTime--;
			return false;
		}
		if (super.shouldExecute()) {
			return this.checkTurtleSpecific();
		}
		return false;
	}

	private boolean checkTurtleSpecific() {
		if (this.getBoss().hasAttackTarget()) {
			return false;
		}
		if ((!this.getBoss().hasHomePositionCQR() && !this.getBoss().hasHome())) {
			return false;
		}
		if (!this.getBoss().hasAttackTarget() && (!this.getBoss().isStunned() && !this.getBoss().isSpinning() && !this.getBoss().isHealing())) {
			/*
			 * if (this.getBoss().isInShell()) {
			 * this.getBoss().targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_OUT);
			 * }
			 */
			return true;
		}
		return false;
	}

	private boolean isTortoiseReadyToWalk() {
		return this.getBoss().getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_WALK;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (super.shouldContinueExecuting()) {
			return this.checkTurtleSpecific();
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.prepareTime = PREPARE_TIME;
		this.getBoss().setInShell(false);
		super.startExecuting();
	}

	@Override
	public void updateTask() {
		if (!this.isTortoiseReadyToWalk() && this.getBoss().getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_IN_SHELL) {
			this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_EXIT_SHELL);
			return;
		}
		if (!this.getBoss().isInShell()) {
			super.updateTask();
		}
	}

	@Override
	public void resetTask() {
		this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_ENTER_SHELL);
		this.getBoss().setInShell(true);
		this.prepareTime = PREPARE_TIME;
		super.resetTask();
	}

}
