package team.cqr.cqrepoured.entity.boss.endercalamity.phases;

import team.cqr.cqrepoured.entity.boss.endercalamity.IEnderCalamityPhase;

public class ECPhaseBuilding implements IEnderCalamityPhase {

	@Override
	public boolean canRandomTeleportDuringPhase() {
		return false;
	}

	@Override
	public boolean canSummonAlliesDuringPhase() {
		return true;
	}

	@Override
	public boolean canPickUpBlocksDuringPhase() {
		return false;
	}

	@Override
	public boolean canThrowBlocksDuringPhase() {
		return false;
	}

	@Override
	public boolean isPhaseTimed() {
		return false;
	}

	@Override
	public IEnderCalamityPhase[] getPossibleSuccessors() {
		return new IEnderCalamityPhase[] { EEnderCalamityPhase.PHASE_IDLE.getPhaseObject() };
	}

}
