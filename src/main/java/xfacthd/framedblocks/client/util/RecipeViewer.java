package xfacthd.framedblocks.client.util;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class RecipeViewer
{
    private static final RecipeViewer INSTANCE = new RecipeViewer();

    public static RecipeViewer get()
    {
        return INSTANCE;
    }

    public @Nullable LookupTarget isShowRecipePressed(int keyCode, int scanCode)
    {
        return null;
    }

    public boolean handleShowRecipeRequest(ItemStack stack, LookupTarget target)
    {
        return false;
    }

    public enum LookupTarget
    {
        RECIPE,
        USAGE
    }
}
