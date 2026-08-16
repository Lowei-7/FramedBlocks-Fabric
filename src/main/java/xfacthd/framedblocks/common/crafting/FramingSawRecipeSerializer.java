package xfacthd.framedblocks.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
    private static final StreamCodec<RegistryFriendlyByteBuf, FramingSawRecipeAdditive> ADDITIVE_STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            FramingSawRecipeAdditive::ingredient,
            ByteBufCodecs.INT,
            FramingSawRecipeAdditive::count,
            FramingSawRecipeAdditive::new
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, List<FramingSawRecipeAdditive>> ADDITIVES_STREAM_CODEC = ByteBufCodecs.collection(ArrayList::new, ADDITIVE_STREAM_CODEC);

    public static final MapCodec<FramingSawRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("material").forGetter(FramingSawRecipe::getMaterialAmount),
            FramingSawRecipeAdditive.LIST_CODEC.optionalFieldOf("additives", List.of()).forGetter(FramingSawRecipe::getAdditives),
            ItemStack.CODEC.fieldOf("result").forGetter(FramingSawRecipe::getResult),
            Codec.BOOL.optionalFieldOf("disabled", false).forGetter(FramingSawRecipe::isDisabled)
    ).apply(inst, FramingSawRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FramingSawRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FramingSawRecipe::getMaterialAmount,
            ADDITIVES_STREAM_CODEC,
            FramingSawRecipe::getAdditives,
            ItemStack.OPTIONAL_STREAM_CODEC,
            FramingSawRecipe::getResult,
            ByteBufCodecs.BOOL,
            FramingSawRecipe::isDisabled,
            FramingSawRecipe::new
    );

    @Override
    public MapCodec<FramingSawRecipe> codec()
    {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FramingSawRecipe> streamCodec()
    {
        return STREAM_CODEC;
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