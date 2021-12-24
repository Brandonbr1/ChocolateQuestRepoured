package team.cqr.cqrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.event.EntityRenderManager;
import team.cqr.cqrepoured.client.model.IHideable;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.client.render.MagicBellRenderer;
import team.cqr.cqrepoured.client.render.entity.layer.LayerGlowingAreas;
import team.cqr.cqrepoured.client.render.entity.layer.equipment.LayerCQREntityArmor;
import team.cqr.cqrepoured.client.render.entity.layer.equipment.LayerCQREntityPotion;
import team.cqr.cqrepoured.client.render.entity.layer.equipment.LayerCQRHeldItem;
import team.cqr.cqrepoured.client.render.entity.layer.equipment.LayerShoulderEntity;
import team.cqr.cqrepoured.client.render.entity.layer.special.LayerCQRLeaderFeather;
import team.cqr.cqrepoured.client.render.entity.layer.special.LayerCQRSpeechbubble;
import team.cqr.cqrepoured.client.render.texture.EntityTexture;
import team.cqr.cqrepoured.client.render.texture.InvisibilityTexture;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.gun.ItemMusket;

public class RenderCQREntity<T extends AbstractEntityCQR> extends RenderLiving<T> {

	protected final Int2ObjectMap<ResourceLocation> textureVariantCache = new Int2ObjectArrayMap<>();

	protected ResourceLocation textureLocation;
	protected final String entityName;
	protected double widthScale;
	protected double heightScale;

	public RenderCQREntity(RenderManager rendermanagerIn, String textureName) {
		this(rendermanagerIn, textureName, 1.0D, 1.0D);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, String textureName, double widthScale, double heightScale) {
		this(rendermanagerIn, new ModelCQRBiped(64, 64), 0.5F, textureName, widthScale, heightScale);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, ModelBase model, float shadowSize, String textureName, double widthScale, double heightScale) {
		super(rendermanagerIn, model, shadowSize);
		this.entityName = textureName;
		this.textureLocation = new ResourceLocation(CQRMain.MODID, "textures/entity/" + this.entityName + ".png");
		this.widthScale = widthScale;
		this.heightScale = heightScale;
		this.addLayer(new LayerCQREntityArmor(this));
		this.addLayer(new LayerCQRHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerElytra(this));
		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
		// TODO fix capes
		// this.addLayer(new LayerCQREntityCape<>(this));
		this.addLayer(new LayerCQREntityPotion<>(this));
		this.addLayer(new LayerCQRSpeechbubble<>(this));
		this.addLayer(new LayerShoulderEntity<>(this));

		if (model instanceof ModelBiped) {
			this.addLayer(new LayerCQRLeaderFeather<>(this, ((ModelBiped) model).bipedHead));
			this.addLayer(new LayerCustomHead(((ModelBiped) model).bipedHead));
		}
	}

	protected double getWidthScale(T entity) {
		return this.widthScale * entity.getSizeVariation();
	}

	protected double getHeightScale(T entity) {
		return this.heightScale * entity.getSizeVariation();
	}

	@Override
	protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);

		double width = this.getWidthScale(entitylivingbaseIn);
		double height = this.getHeightScale(entitylivingbaseIn);
		GL11.glScaled(width, height, width);

		if (this.mainModel.isRiding) {
			GL11.glTranslatef(0, 0.6F, 0);
		}
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (!EntityRenderManager.shouldEntityBeRendered(entity)) {
			return;
		}

		if (this.mainModel instanceof ModelBiped) {
			ModelBiped model = (ModelBiped) this.mainModel;
			EnumHand rightHand;
			EnumHand leftHand;

			if (entity.isLeftHanded()) {
				rightHand = EnumHand.OFF_HAND;
				leftHand = EnumHand.MAIN_HAND;
			} else {
				rightHand = EnumHand.MAIN_HAND;
				leftHand = EnumHand.OFF_HAND;
			}

			model.rightArmPose = this.getArmPose(entity, rightHand);
			model.rightArmPose = this.getArmPose(entity, leftHand);
		}

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		GlStateManager.disableBlend();
	}

	private ArmPose getArmPose(T entity, EnumHand hand) {
		ItemStack stack = entity.getHeldItem(hand);

		if (stack.isEmpty()) {
			return ArmPose.EMPTY;
		}

		Item item = stack.getItem();

		if (item instanceof ItemMusket) {
			return ArmPose.BOW_AND_ARROW;
		}

		if (entity.getItemInUseCount() > 0) {
			EnumAction action = item.getItemUseAction(stack);
			switch (action) {
			case DRINK:
			case EAT:
				return ArmPose.ITEM;
			case BOW:
				return ArmPose.BOW_AND_ARROW;
			case BLOCK:
				return ArmPose.BLOCK;
			default:
				break;
			}
		}

		return ArmPose.ITEM;
	}

	@Override
	protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch,
			float scaleFactor) {
		EntityTexture texture = EntityTexture.get(this.getEntityTexture(entitylivingbaseIn));
		if (this.mainModel instanceof IHideable) {
			((IHideable) this.mainModel).setupVisibility(texture.getPartsToRender());
		}

		boolean isInvisible = entitylivingbaseIn.getInvisibility() > 0.0F;
		if (isInvisible) {
			GlStateManager.alphaFunc(GL11.GL_GREATER, entitylivingbaseIn.getInvisibility());
			this.bindTexture(InvisibilityTexture.get(texture.getTextureLocation()));
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.depthFunc(GL11.GL_EQUAL);
		}

		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

		if (isInvisible) {
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
		}

		if (this.mainModel instanceof IHideable) {
			((IHideable) this.mainModel).resetVisibility();
		}
	}

	@Override
	protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
		if (this.mainModel instanceof ModelBiped) {
			((ModelBiped) this.mainModel).isRiding = entityLivingBaseIn.isRiding() || entityLivingBaseIn.isSitting();
			((ModelBiped) this.mainModel).isSneak = entityLivingBaseIn.isSneaking();
		}

		super.renderLivingAt(entityLivingBaseIn, x, y, z);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		if (entity.hasTextureOverride()) {
			return entity.getTextureOverride();
		}

		if (entity.getTextureCount() > 1) {
			return this.textureVariantCache.computeIfAbsent(entity.getTextureIndex(), k -> {
				String s = String.format("textures/entity/%s_%d.png", this.entityName, k);
				return new ResourceLocation(CQRMain.MODID, s);
			});
		}

		return this.textureLocation;
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		this.shadowSize *= ((AbstractEntityCQR) entityIn).getSizeVariation();
		this.shadowOpaque = MathHelper.clamp(1.0F - ((AbstractEntityCQR) entityIn).getInvisibility(), 0.0F, 1.0F);
		super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
		this.shadowSize /= ((AbstractEntityCQR) entityIn).getSizeVariation();
	}

	@Override
	protected int getTeamColor(T entityIn) {
		if (MagicBellRenderer.outlineColor != -1) {
			return MagicBellRenderer.outlineColor;
		}
		return super.getTeamColor(entityIn);
	}

	public void setupHeadOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {

	}

	public void setupBodyOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {

	}

	public void setupRightArmOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {

	}

	public void setupLeftArmOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {

	}

	public void setupRightLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {

	}

	public void setupLeftLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {

	}

	public void setupHeadwearOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {

	}

	public void setupPotionOffsets(ModelRenderer modelRenderer) {

	}

	protected void applyTranslations(ModelRenderer modelRenderer) {
		GlStateManager.translate(modelRenderer.offsetX, modelRenderer.offsetY, modelRenderer.offsetZ);
		GlStateManager.translate(modelRenderer.rotationPointX * 0.0625F, modelRenderer.rotationPointY * 0.0625F, modelRenderer.rotationPointZ * 0.0625F);
	}

	protected void resetTranslations(ModelRenderer modelRenderer) {
		GlStateManager.translate(-modelRenderer.rotationPointX * 0.0625F, -modelRenderer.rotationPointY * 0.0625F, -modelRenderer.rotationPointZ * 0.0625F);
		GlStateManager.translate(-modelRenderer.offsetX, -modelRenderer.offsetY, -modelRenderer.offsetZ);
	}

	protected void applyRotations(ModelRenderer modelRenderer) {
		GlStateManager.rotate((float) Math.toDegrees(modelRenderer.rotateAngleZ), 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate((float) Math.toDegrees(modelRenderer.rotateAngleY), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) Math.toDegrees(modelRenderer.rotateAngleX), 1.0F, 0.0F, 0.0F);
	}

	protected void resetRotations(ModelRenderer modelRenderer) {
		GlStateManager.rotate((float) Math.toDegrees(modelRenderer.rotateAngleX), -1.0F, 0.0F, 0.0F);
		GlStateManager.rotate((float) Math.toDegrees(modelRenderer.rotateAngleY), 0.0F, -1.0F, 0.0F);
		GlStateManager.rotate((float) Math.toDegrees(modelRenderer.rotateAngleZ), 0.0F, 0.0F, -1.0F);
	}

	public double getHeightScale() {
		return heightScale;
	}

	public double getWidthScale() {
		return widthScale;
	}

}
