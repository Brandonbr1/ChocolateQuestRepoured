package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRHuman;

public class RenderCQRHuman extends RenderCQREntity<EntityCQRHuman> {

	public RenderCQRHuman(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, "mob/human", true);
	}

}
