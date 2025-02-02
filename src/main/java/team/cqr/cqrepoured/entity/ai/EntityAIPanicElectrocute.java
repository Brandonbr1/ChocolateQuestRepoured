package team.cqr.cqrepoured.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.PanicGoal;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class EntityAIPanicElectrocute extends PanicGoal {

	public EntityAIPanicElectrocute(CreatureEntity creature, double speedIn) {
		super(creature, speedIn);
	}

	@Override
	public boolean shouldExecute() {
		if (this.creature instanceof IMechanical || this.creature.getCreatureAttribute() == CQRCreatureAttributes.MECHANICAL) {
			return false;
		}

		if (this.creature.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			return this.creature.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null).isElectrocutionActive() && this.findRandomPosition();
		}
		return false;
	}

}
