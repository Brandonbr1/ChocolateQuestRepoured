package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.boss.ModelNetherDragonHead;
import team.cqr.cqrepoured.client.model.entity.boss.ModelNetherDragonHeadSkeletal;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;

public class RenderCQRNetherDragon extends RenderLiving<EntityCQRNetherDragon> {

	public static final ResourceLocation TEXTURES_NORMAL = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/nether_dragon.png");
	public static final ResourceLocation TEXTURES_SKELETAL = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/nether_dragon_skeletal_head.png");

	private final ModelNetherDragonHead modelNormal;
	private final ModelNetherDragonHeadSkeletal modelSkeletal;

	public RenderCQRNetherDragon(RenderManager manager) {
		super(manager, new ModelNetherDragonHead(), 0.5F);
		this.modelNormal = (ModelNetherDragonHead) this.mainModel;
		this.modelSkeletal = new ModelNetherDragonHeadSkeletal();

		this.addLayer(new LayerGlowingAreas<RenderCQRNetherDragon, EntityCQRNetherDragon>(this, this::getEntityTexture) {
			@Override
			public void doRenderLayer(EntityCQRNetherDragon entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
				if (entitylivingbaseIn.getSkeleProgress() >= 0) {
					return;
				}
				super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			}
		});
	}

	@Override
	public void doRender(EntityCQRNetherDragon entity, double x, double y, double z, float entityYaw, float partialTicks) {
		// Determine model
		if (entity.getSkeleProgress() >= 0) {
			this.mainModel = this.modelSkeletal;
		} else {
			this.mainModel = this.modelNormal;
		}

		/*
		 * if(entity.deathTicks > 0 ) {
		 * GlStateManager.pushMatrix();
		 * GlStateManager.color(new Float(0.5F * (0.25 * Math.sin(0.75 * entity.ticksExisted) + 0.5)), 0, 0, 1F);
		 * }
		 */

		// TODO do NOT spawn particles during rendering
		/*
		 * if (entity.deathTime > 0 && entity.deathTime % 5 == 0) {
		 * float x = entity.posX + (entity.getRNG().nextFloat() - 0.5F) * 8.0F;
		 * float y = entity.posY + (entity.getRNG().nextFloat() - 0.5F) * 4.0F + 2.0D;
		 * float z = entity.posZ + (entity.getRNG().nextFloat() - 0.5F) * 8.0F;
		 * World world = Minecraft.getMinecraft().world;
		 * world.spawnParticle(entity.getDeathAnimParticles(), x, y, z, 0.0D, 0.0D, 0.0D);
		 * }
		 */

		super.doRender(entity, x, y, z, entity.rotationYawHead, partialTicks);

		/*
		 * if(entity.deathTicks > 0 ) {
		 * GlStateManager.popMatrix();
		 * }
		 */
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRNetherDragon entity) {
		if (entity.getSkeleProgress() < 0) {
			// Custom texture start
			if (entity.hasTextureOverride()) {
				return entity.getTextureOverride();
			}
			// Custom texture end
			return TEXTURES_NORMAL;
		}
		return TEXTURES_SKELETAL;
	}

	@Override
	protected float getDeathMaxRotation(EntityCQRNetherDragon entityLivingBaseIn) {
		return 0;
	}

}
