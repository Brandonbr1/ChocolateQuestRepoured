package team.cqr.cqrepoured.client.render.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.render.entity.layers.special.LayerCQRPirateParrotHeldItem;

public class RenderPirateParrot extends RenderParrot {

	public static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/entity/pirate_parrot.png");

	public RenderPirateParrot(RenderManager renderManager) {
		super(renderManager);

		this.addLayer(new LayerCQRPirateParrotHeldItem(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityParrot entity) {
		return TEXTURE;
	}

}
