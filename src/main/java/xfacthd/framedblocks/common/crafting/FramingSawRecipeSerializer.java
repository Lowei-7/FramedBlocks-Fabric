package xfacthd.framedblocks.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.type.IBlockType;

import java.util.ArrayList;
import java.util.List;

public final class FramingSawRecipeSerializer implements RecipeSerializer<FramingSawRecipe>
{
    public static final Codec<FramingSawRecipe> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("material").forGetter(FramingSawRecipe::getMaterialAmount),
            FramingSawRecipeAdditive.LIST_CODEC.optionalFieldOf("additives", List.of()).forGetter(FramingSawRecipe::getAdditives),
            ItemStack.CODEC.fieldOf("result").forGetter(FramingSawRecipe::getResult),
            Codec.BOOL.optionalFieldOf("disabled", false).forGetter(FramingSawRecipe::isDisabled)
    ).apply(inst, FramingSawRecipe::new));

    @Override
    public Codec<FramingSawRecipe> codec()
    {
        return CODEC;
    }

    @Override
    public FramingSawRecipe fromNetwork(FriendlyByteBuf buffer)
    {
        int material = buffer.readInt();

        int count = buffer.readInt();
        List<FramingSawRecipeAdditive> additives = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
        {
            Ingredient additive = Ingredient.fromNetwork(buffer);
            int additiveCount = buffer.readInt();
            additives.add(new FramingSawRecipeAdditive(additive, additiveCount));
        }

        ItemStack result = buffer.readItem();
        boolean disabled = buffer.readBoolean();

        return new FramingSawRecipe(material, additives, result, disabled);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FramingSawRecipe recipe)
    {
        buffer.writeInt(recipe.getMaterialAmount());

        List<FramingSawRecipeAdditive> additives = recipe.getAdditives();
        buffer.writeInt(additives.size());
        for (FramingSawRecipeAdditive additive : additives)
        {
            additive.ingredient().toNetwork(buffer);
            buffer.writeInt(additive.count());
        }

        buffer.writeItem(recipe.getResult());
        buffer.writeBoolean(recipe.isDisabled());
    }

    static IBlockType findResultType(ItemStack result)
    {
        if (!(result.getItem() instanceof BlockItem item))
        {
            throw new IllegalArgumentException("Result items must be BlockItems");
        }
        if (!(item.getBlock() instanceof IFramedBlock block))
        {
            throw new IllegalArgumentException("Block of result items must be IFramedBlocks");
        }
        return block.getBlockType();
    }
}
