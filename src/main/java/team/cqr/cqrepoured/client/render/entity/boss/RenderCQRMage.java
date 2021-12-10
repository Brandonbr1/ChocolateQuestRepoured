package team.cqr.cqrepoured.client.render.entity.boss;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.model.entity.boss.ModelMageHidden;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.layers.LayerGlowingAreas;
import team.cqr.cqrepoured.client.render.entity.layers.LayerMagicalArmor;
import team.cqr.cqrepoured.entity.boss.AbstractEntityCQRMageBase;

public class RenderCQRMage<T extends AbstractEntityCQRMageBase> extends RenderCQREntity<T> {

	public static final ResourceLocation TEXTURES_HIDDEN = new ResourceLocation(CQRMain.MODID, "textures/entity/boss/mage_hidden.png");
	public static final ResourceLocation TEXTURES_ARMOR = new ResourceLocation(CQRMain.MODID, "textures/entity/magic_armor/mages.png");

	private final ModelBiped modelHidden;
	private final ModelBiped modelRevealed;

	public RenderCQRMage(RenderManager rendermanagerIn, ModelBiped model, String entityName) {
		super(rendermanagerIn, model, 0.5F, entityName, 1D, 1D);
		this.modelHidden = new ModelMageHidden();
		this.modelRevealed = model;

		for (int i = 0; i < this.layerRenderers.size(); i++) {
			LayerRenderer<?> layer = this.layerRenderers.get(i);
			if (layer instanceof LayerBipedArmor) {
				this.layerRenderers.remove(i--);
			}
		}
		this.addLayer(new LayerGlowingAreas<>(this, this::getEntityTexture));
		this.addLayer(new LayerMagicalArmor(this, TEXTURES_ARMOR, model));
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.isIdentityHidden()) {
			this.mainModel = this.modelHidden;
		} else {
			this.mainModel = this.modelRevealed;
		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return entity.isIdentityHidden() ? TEXTURES_HIDDEN : super.getEntityTexture(entity);
	}

}
