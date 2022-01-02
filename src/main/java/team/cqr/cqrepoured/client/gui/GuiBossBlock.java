package team.cqr.cqrepoured.client.gui;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.util.GuiHelper;

@Dist(OnlyIn.CLIENT)
public class GuiBossBlock extends ContainerScreen {

	private static final ResourceLocation GUI_BOSS_BLOCK = new ResourceLocation(CQRMain.MODID, "textures/gui/container/gui_boss_block.png");

	public GuiBossBlock(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		this.xSize = 176;
		this.ySize = 132;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GuiHelper.drawString(this.fontRenderer, I18n.format("Boss Block"), this.xSize / 2, 7, 0x404040, true, false);
		GuiHelper.drawString(this.fontRenderer, I18n.format("container.inventory"), 8, 39, 0x404040, false, false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_BOSS_BLOCK);
		GuiHelper.drawTexture(this.guiLeft, this.guiTop, 0.0D, 0.0D, this.xSize, this.ySize, this.xSize / 256.0D, this.ySize / 256.0D);
	}

}
