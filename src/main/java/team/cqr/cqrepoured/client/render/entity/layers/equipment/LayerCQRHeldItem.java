package team.cqr.cqrepoured.client.render.entity.layers.equipment;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;

public class LayerCQRHeldItem extends LayerHeldItem {

	public LayerCQRHeldItem(RenderLivingBase<?> livingEntityRendererIn) {
		super(livingEntityRendererIn);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

		// when rendering a skull it messes up the gl state
		GlStateManager.disableCull();
	}

	@Override
	protected void translateToHand(EnumHandSide handSide) {
		super.translateToHand(handSide);
		if (this.livingEntityRenderer.getMainModel() instanceof ModelBiped) {
			ModelBiped model = (ModelBiped) this.livingEntityRenderer.getMainModel();
			ModelRenderer armRenderer;
			if (handSide == EnumHandSide.RIGHT) {
				armRenderer = model.bipedRightArm;
			} else {
				armRenderer = model.bipedLeftArm;
			}
			if (!armRenderer.cubeList.isEmpty()) {
				ModelBox armBox = armRenderer.cubeList.get(0);
				float x = 0.125F - 0.03125F * (armBox.posX2 - armBox.posX1);
				if (handSide == EnumHandSide.LEFT) {
					x *= -1.0F;
				}
				float y = 0.0625F * (armBox.posY2 - armBox.posY1 - 12.0F);
				GlStateManager.translate(x, y, 0.0F);
			}
		}
	}

}
