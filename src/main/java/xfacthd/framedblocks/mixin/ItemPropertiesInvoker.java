package xfacthd.framedblocks.mixin;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemProperties.class)
public interface ItemPropertiesInvoker
{
    @Invoker("register")
    static void framedblocks$register(Item item, ResourceLocation name, ClampedItemPropertyFunction function) { throw new AssertionError(); }
}