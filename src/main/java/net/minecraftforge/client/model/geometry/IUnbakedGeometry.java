package net.minecraftforge.client.model.geometry;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface IUnbakedGeometry<T extends IUnbakedGeometry<T>>
{
    BakedModel bake(
            IGeometryBakingContext context,
            ModelBaker bakery,
            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState transform,
            ItemOverrides overrides,
            ResourceLocation modelLocation
    );

    void resolveParents(
            Function<ResourceLocation, net.minecraft.client.resources.model.UnbakedModel> modelGetter,
            IGeometryBakingContext context
    );
}
