package xfacthd.framedblocks.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpriteSources.class)
public interface SpriteSourcesAccessor
{
    @Invoker("register")
    static SpriteSourceType register(String name, Codec<? extends SpriteSource> codec)
    {
        throw new AssertionError("Mixin not applied");
    }
}