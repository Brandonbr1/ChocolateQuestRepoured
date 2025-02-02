package team.cqr.cqrepoured.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Made with Blockbench 4.0.0
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports

public class ModelCannonBall extends ModelBase {
	private final ModelRenderer bb_main;

	public ModelCannonBall() {
		this.textureWidth = 16;
		this.textureHeight = 16;

		this.bb_main = new ModelRenderer(this);
		this.bb_main.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -2.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F, false));
		this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -2.0F, -5.0F, 2.0F, 4, 4, 1, 0.0F, false));
		this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -2.0F, -5.0F, -3.0F, 4, 4, 1, 0.0F, false));
		this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -2.0F, -6.0F, -2.0F, 4, 1, 4, 0.0F, false));
		this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, 2.0F, -5.0F, -2.0F, 1, 4, 4, 0.0F, false));
		this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, -3.0F, -5.0F, -2.0F, 1, 4, 4, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.bb_main.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
