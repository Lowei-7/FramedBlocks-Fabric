package xfacthd.framedblocks.client.render.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.mixin.ItemPropertiesInvoker;

public final class BlueprintPropertyOverride
{
    public static final ResourceLocation HAS_DATA = Utils.rl("blueprint_override");

    private BlueprintPropertyOverride() { }

    public static void register()
    {
        ItemPropertiesInvoker.framedblocks$register(
                FBContent.ITEM_FRAMED_BLUEPRINT.get(),
                HAS_DATA,
                (stack, level, entity, seed) ->
                {
                    CompoundTag tag = null;
                    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                    if (customData != null && customData.contains("blueprint_data"))
                    {
                        tag = customData.getUnsafe().getCompound("blueprint_data");
                    }
                    return tag != null && !tag.isEmpty() ? 1 : 0;
                }
        );
    }
}