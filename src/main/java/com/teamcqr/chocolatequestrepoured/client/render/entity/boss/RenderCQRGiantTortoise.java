package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

public class RenderCQRGiantTortoise extends RenderLiving<EntityCQRGiantTortoise> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise.png");

	public RenderCQRGiantTortoise(RenderManager rendermanagerIn, ModelGiantTortoise modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantTortoise entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(EntityCQRGiantTortoise entity, double x, double y, double z, float entityYaw, float partialTicks) {
		// DONE: Rotate move around z axis when the mouth is open
		switch (entity.getCurrentAnimation()) {
		case HEALING:
			break;
		case MOVE_PARTS_IN:
			break;
		case MOVE_PARTS_OUT:
			break;
		case NONE:
			break;
		case SPIN:
			break;
		case SPIN_DOWN:
			break;
		case SPIN_UP:
			break;
		case WALKING:
			break;
		default:
			break;

		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
