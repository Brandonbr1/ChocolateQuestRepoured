package team.cqr.cqrepoured.client.render.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection.Section;

public class AutoGlowingTexture extends AbstractTextureCQR {

	private final Set<String> partsToRender = new HashSet<>();

	public AutoGlowingTexture(ResourceLocation originalTextureLocation, ResourceLocation textureLocation) {
		super(originalTextureLocation, textureLocation);
	}

	public static AutoGlowingTexture get(ResourceLocation originalTextureLocation) {
		ResourceLocation textureLocation = appendBeforeEnding(originalTextureLocation, "_glowing");
		TextureManager textureManager = Minecraft.getMinecraft().renderEngine;
		ITextureObject texture = textureManager.getTexture(textureLocation);

		if (!(texture instanceof AutoGlowingTexture)) {
			texture = new AutoGlowingTexture(originalTextureLocation, textureLocation);
			textureManager.loadTexture(textureLocation, texture);
		}

		return (AutoGlowingTexture) texture;
	}

	@Override
	protected BufferedImage onLoadMetadata(BufferedImage image, IResource resource) throws IOException {
		BufferedImage image1 = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		GlowingMetadataSection glowingSections = resource.getMetadata("glowsections");

		if (glowingSections != null) {
			for (Section section : glowingSections.getGlowingSections()) {
				for (int x = section.getMinX(); x < section.getMaxX(); x++) {
					for (int y = section.getMinY(); y < section.getMaxY(); y++) {
						image1.setRGB(x, y, image.getRGB(x, y));
					}
				}
			}

			this.partsToRender.addAll(glowingSections.getGlowingParts());
		}

		return image1;
	}

	public Collection<String> getPartsToRender() {
		return Collections.unmodifiableCollection(partsToRender);
	}

}
