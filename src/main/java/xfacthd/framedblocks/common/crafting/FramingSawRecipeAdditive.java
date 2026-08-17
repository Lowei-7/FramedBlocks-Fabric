package xfacthd.framedblocks.common.crafting;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public record FramingSawRecipeAdditive(Ingredient ingredient, int count)
{
    public static final Codec<FramingSawRecipeAdditive> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(FramingSawRecipeAdditive::ingredient),
            Codec.INT.fieldOf("count").forGetter(FramingSawRecipeAdditive::count)
    ).apply(inst, FramingSawRecipeAdditive::new));

    public static final Codec<List<FramingSawRecipeAdditive>> LIST_CODEC = CODEC.listOf();

    public FramingSawRecipeAdditive
    {
        Preconditions.checkArgument(ingredient != null, "Additive ingredient must be non-null");
        Preconditions.checkArgument(count > 0, "Additive count must be greater than 0");
    }

    public static FramingSawRecipeAdditive of(Ingredient ingredient)
    {
        return of(ingredient, 1);
    }

    public static FramingSawRecipeAdditive of(Ingredient ingredient, int count)
    {
        return new FramingSawRecipeAdditive(ingredient, count);
    }

    public static FramingSawRecipeAdditive of(ItemLike item)
    {
        return of(item, 1);
    }

    public static FramingSawRecipeAdditive of(ItemLike item, int count)
    {
        return new FramingSawRecipeAdditive(Ingredient.of(item), count);
    }
}
