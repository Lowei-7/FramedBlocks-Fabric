package xfacthd.framedblocks.client.loader.overlay;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.joml.Vector3f;

import java.util.function.Function;

record OverlayGeometry(BlockModel wrapped, Vector3f offset, Vector3f scale) implements IUnbakedGeometry<OverlayGeometry>
{
    public static final Vector3f VEC_ZERO = new Vector3f();

    @Override
    public BakedModel bake(
            IGeometryBakingContext context,
            ModelBaker bakery,
            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState transform
    )
    {
        Transformation transformation = transform.getRotation().compose(new Transformation(offset, null, scale, null));
        transform = new SimpleModelState(transformation, transform.isUvLocked());

        BakedModel model = wrapped.bake(bakery, spriteGetter, transform);
        return offset.equals(VEC_ZERO) ? model : new OverlayModel(model, offset, scale);
    }

    @Override
    public void resolveDependencies(UnbakedModel.Resolver resolver, IGeometryBakingContext context)
    {
        wrapped.resolveDependencies(resolver);
    }
}
