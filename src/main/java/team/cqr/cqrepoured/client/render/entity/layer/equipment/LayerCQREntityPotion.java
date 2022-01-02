package team.cqr.cqrepoured.client.render.entity.layer.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractLayerCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.init.CQRItems;

@Dist(OnlyIn.CLIENT)
public class LayerCQREntityPotion extends AbstractLayerCQR {

	public LayerCQREntityPotion(RenderCQREntity<?> renderCQREntity) {
		super(renderCQREntity);
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity.getHealingPotions() > 0 && this.entityRenderer.getMainModel() instanceof ModelBiped) {
			ModelBiped model = (ModelBiped) this.entityRenderer.getMainModel();
			ModelRenderer body = model.bipedBody;

			if (!body.cubeList.isEmpty()) {
				ModelBox box = body.cubeList.get(0);

				if (box != null) {
					ItemStack stack = new ItemStack(CQRItems.POTION_HEALING);

					GlStateManager.pushMatrix();
					if (entity.isSneaking()) {
						GlStateManager.translate(0.0F, 0.2F, 0.0F);
					}
					GlStateManager.translate(body.offsetX + body.rotationPointX * 0.0625F, body.offsetY + body.rotationPointY * 0.0625F, body.offsetZ + body.rotationPointZ * 0.0625F);
					GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleX), 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleY), 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleZ), 0.0F, 0.0F, 1.0F);
					GlStateManager.translate(box.posX1 * 0.0625F, box.posY1 * 0.0625F, box.posZ1 * 0.0625F);
					float f = 0.0F;
					if (!entity.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty()) {
						f = -1.0F;
					} else if (!entity.getItemStackFromSlot(EquipmentSlotType.LEGS).isEmpty()) {
						f = -0.5F;
					}
					GlStateManager.translate(f * 0.0625F - 0.0125F, (box.posY2 - box.posY1) * 0.0625F, (box.posZ2 - box.posZ1) * 0.0625F * 0.5F);
					this.entityRenderer.setupPotionOffsets(null);
					GlStateManager.scale(0.4F, 0.4F, 0.4F);
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
					Minecraft.getMinecraft().getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.NONE);

					GlStateManager.popMatrix();
				}
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
