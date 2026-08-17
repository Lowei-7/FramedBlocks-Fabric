package net.minecraftforge.client.model.geometry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;

import java.util.function.Function;

public interface IUnbakedGeometry<T extends IUnbakedGeometry<T>>
{
    BakedModel bake(
            IGeometryBakingContext context,
            ModelBaker bakery,
            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState transform
    );

    void resolveDependencies(
            UnbakedModel.Resolver resolver,
            IGeometryBakingContext context
    );
}
