package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class LayerMagicalArmor extends AbstractLayerCQR {

	protected final ResourceLocation ARMOR_TEXTURE;
	protected final RenderCQREntity<? extends AbstractEntityCQR> RENDERER;
	protected final ModelBase MODEL;

	public LayerMagicalArmor(RenderCQREntity<? extends AbstractEntityCQR> renderer, ResourceLocation texture, ModelBase model) {
		super(renderer);
		this.RENDERER = renderer;
		this.ARMOR_TEXTURE = texture;
		this.MODEL = model;
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
			float netHeadYaw, float headPitch, float scale) {

		if (entitylivingbaseIn.isMagicArmorActive()) {
			GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
			this.RENDERER.bindTexture(this.ARMOR_TEXTURE);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f = entitylivingbaseIn.ticksExisted + partialTicks;
			float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
			float f2 = f * 0.01F;
			GlStateManager.translate(f1, f2, 0.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.enableBlend();
			GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			this.MODEL.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
			this.MODEL.setModelAttributes(this.RENDERER.getMainModel());
			Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			this.MODEL.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
