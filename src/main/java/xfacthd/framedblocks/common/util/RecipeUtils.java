package xfacthd.framedblocks.common.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import xfacthd.framedblocks.api.util.Utils;

import java.lang.invoke.MethodHandle;

public final class RecipeUtils
{
    private static final MethodHandle INGREDIENT_GET_VALUES = Utils.unreflectField(Ingredient.class, "field_9019", "values");
    private static final MethodHandle INGREDIENT_TAGVALUE_GET_TAG;
    
    static
    {
        Class<?> tagValueClass = null;
        for (Class<?> clazz : Ingredient.class.getDeclaredClasses())
        {
            if (clazz.getSimpleName().equals("TagValue"))
            {
                tagValueClass = clazz;
                break;
            }
        }
        if (tagValueClass == null)
        {
            throw new RuntimeException("Failed to find Ingredient$TagValue class");
        }
        INGREDIENT_TAGVALUE_GET_TAG = Utils.unreflectField(tagValueClass, "field_9022", "tag");
    }

    public static Object getSingleIngredientValue(Ingredient ing)
    {
        Object[] values;
        try
        {
            values = (Object[]) INGREDIENT_GET_VALUES.invoke(ing);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
        return values.length == 1 ? values[0] : null;
    }

    @SuppressWarnings("unchecked")
    public static TagKey<Item> getItemTagFromValue(Object value)
    {
        try
        {
            return (TagKey<Item>) INGREDIENT_TAGVALUE_GET_TAG.invoke(value);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }



    private RecipeUtils() { }
}
