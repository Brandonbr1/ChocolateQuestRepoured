package team.cqr.cqrepoured.client.models.entities.boss;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.util.Reference;

public class ModelEnderCalamity extends AnimatedGeoModel<EntityCQREnderCalamity> {

	static final ResourceLocation ANIMATION_RESLOC = new ResourceLocation(Reference.MODID, "animations/ender_calamity.animation.json");
	static final ResourceLocation MODEL_RESLOC = new ResourceLocation(Reference.MODID, "geo/ender_calamity.geo.json");

	private ResourceLocation texture;

	public ModelEnderCalamity(ResourceLocation texture) {
		super();
		this.texture = texture;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(EntityCQREnderCalamity animatable) {
		return ANIMATION_RESLOC;
	}

	@Override
	public ResourceLocation getModelLocation(EntityCQREnderCalamity object) {
		return MODEL_RESLOC;
	}

	@Override
	public ResourceLocation getTextureLocation(EntityCQREnderCalamity entity) {
		// Custom texture start
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}
		// Custom texture end
		return entity.getTextureCount() > 1 ? new ResourceLocation(Reference.MODID, "textures/entity/boss/ender_calamity_" + entity.getTextureIndex() + ".png") : this.texture;
	}
	
	private static final String BONE_IDENT_ROOT = "root";
	private static final String BONE_IDENT_BODY = "body";
	//private static final String BONE_IDENT_HEAD = "head";
	/*private static final String BONE_IDENT_LEGJOINT_BR = "legJointBR";
	private static final String BONE_IDENT_LEGJOINT_BL = "legJointBL";
	private static final String BONE_IDENT_LEGJOINT_FR = "legJointFR";
	private static final String BONE_IDENT_LEGJOINT_FL = "legJointFL";*/

	/*
	 * Bones needed for walking:
	 * - legJoint FR
	 * - legJoint FL
	 * - legJoint BR
	 * - legJoint BL
	 * - head
	 */
	
	@Override
	public void setLivingAnimations(EntityCQREnderCalamity entity, Integer uniqueID, AnimationEvent customPredicate) {
		//TODO: Fix buggy rotation
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		//IBone headBone = this.getAnimationProcessor().getBone(BONE_IDENT_HEAD);
		//IBone rootBone = this.getAnimationProcessor().getBone(BONE_IDENT_ROOT);

		//rootBone.setRotationY(-entity.rotationYaw);
		
		//headBone.setRotationX((float) Math.toRadians(-entity.rotationPitch) /*- rootBone.getRotationX()*/);
		//headBone.setRotationY((float) Math.toRadians(-(entity.rotationYawHead - entity.rotationYaw))/* - rootBone.getRotationY()*/);
		if(entity.rotateBodyPitch()) {
			IBone rootBone = this.getAnimationProcessor().getBone(BONE_IDENT_ROOT);
			IBone bodyBone = this.getAnimationProcessor().getBone(BONE_IDENT_BODY);
			float pitch = (float)Math.toRadians(this.getPitch(entity, Minecraft.getMinecraft().getRenderPartialTicks()) -90F);
			pitch -= rootBone.getRotationX();
			//System.out.println("Client pitch: " + pitch);
			//System.out.println("Client prev pitch: " + entity.prevRotationPitchCQR);
			bodyBone.setRotationX(pitch);
		}

	}
	
	protected float getPitch(EntityCQREnderCalamity entity, float partialTicks) {
		return this.interpolateRotation(entity.prevRotationPitchCQR, entity.rotationPitchCQR, partialTicks);
	}

	protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
		return prevRotation + MathHelper.wrapDegrees(rotation - prevRotation) * partialTicks;
	}
	
}
