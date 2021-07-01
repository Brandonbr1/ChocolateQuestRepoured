package team.cqr.cqrepoured.client.render.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;
import team.cqr.cqrepoured.util.Reference;

/*
 * 06.01.2020
 * Author: DerToaster98
 * Github: https://github.com/DerToaster98
 */
@SideOnly(Side.CLIENT)
public class TileEntityForceFieldNexusRenderer extends TileEntitySpecialRenderer<TileEntityForceFieldNexus> {

	private final ModelBase crystal = new ModelNexusCrystal();
	private static final ResourceLocation CRYSTAL_TEXTURES = new ResourceLocation(Reference.MODID, "textures/entity/nexus_crystal.png");

	@Override
	public void render(TileEntityForceFieldNexus te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		// Crystal code
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.7F, (float) z + 0.5F);
		this.bindTexture(CRYSTAL_TEXTURES);

		this.crystal.render(null, 0.0F, 0.0F, partialTicks, 0.0F, 0.0F, 0.025F);
		GlStateManager.enableLighting();
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
	}

}
