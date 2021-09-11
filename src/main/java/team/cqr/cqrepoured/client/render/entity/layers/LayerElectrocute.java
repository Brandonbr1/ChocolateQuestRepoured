package team.cqr.cqrepoured.client.render.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShock;
import team.cqr.cqrepoured.capability.electric.CapabilityElectricShockProvider;
import team.cqr.cqrepoured.client.util.ElectricFieldRenderUtil;

public class LayerElectrocute implements LayerRenderer<EntityLivingBase> {

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(entity.hasCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null)) {
			CapabilityElectricShock cap = entity.getCapability(CapabilityElectricShockProvider.ELECTROCUTE_HANDLER_CQR, null);
			if(cap.getRemainingTicks() < 0) {
				return;
			}
			ElectricFieldRenderUtil.renderElectricFieldWithSizeOfEntityAt(entity, 0,0,0);
			if(cap.getTarget() != null) {
				Entity target = cap.getTarget();
				
				/*final Vec3d dir = target.getPositionVector().subtract(entity.getPositionVector());
				
				final Vec3d start = new Vec3d(x,y,z);
				final Vec3d end = dir;*/
				
				double x1 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
				double y1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
				y1 += entity.getEyeHeight() / 2;
				double z1 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
				
				double x2 = target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks;
				double y2 = target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks;
				y2 += target.getEyeHeight() / 2;
				double z2 = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks;
				
				Entity entity1 = Minecraft.getMinecraft().player;
				double x3 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * partialTicks;
				double y3 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * partialTicks;
				y3 += entity1.getEyeHeight() / 2;
				double z3 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * partialTicks;
				x1 -= x3;
				y1 -= y3;
				z1 -= z3;
				x2 -= x3;
				y2 -= y3;
				z2 -= z3;
				
				final Vec3d start = new Vec3d(x1,y1,z1);
				final Vec3d end = new Vec3d(x2,y2,z2);
				
				GlStateManager.pushMatrix();
				
				GlStateManager.loadIdentity();
				
				GlStateManager.translate(x1, y1, z1);
				
				ElectricFieldRenderUtil.renderElectricLineBetween(start, end, entity.getRNG(), 0.5, 0,0,0, 5);
				
				GlStateManager.loadIdentity();
				
				GlStateManager.popMatrix();
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
