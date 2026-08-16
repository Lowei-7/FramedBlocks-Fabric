package net.minecraftforge.client.textures;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.server.packs.resources.ResourceManager;

public interface ISpriteSourcePackAwareSpriteSupplier
{
    SpriteContents getSprite(ResourceManager resourceManager);
}
