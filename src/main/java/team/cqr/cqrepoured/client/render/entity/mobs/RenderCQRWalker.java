package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.entity.mobs.EntityCQRWalker;

public class RenderCQRWalker extends RenderCQREntity<EntityCQRWalker> {

	public RenderCQRWalker(EntityRendererManager rendermanagerIn) {
		super(rendermanagerIn, "mob/walker", true);
		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
	}

}
