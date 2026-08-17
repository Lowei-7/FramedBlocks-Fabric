package xfacthd.framedblocks.common.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.common.FBContent;

import java.util.*;

public final class FramingSawRecipe implements Recipe<RecipeInput>
{
    public static final int CUBE_MATERIAL_VALUE = 6144; // Empirically determined value
    public static final int MAX_ADDITIVE_COUNT = 3;

    private ResourceKey<Recipe<?>> id;
    private final int materialAmount;
    private final List<FramingSawRecipeAdditive> additives;
    private final ItemStack result;
    private final IBlockType resultType;
    private final boolean disabled;

    FramingSawRecipe(int materialAmount, List<FramingSawRecipeAdditive> additives, ItemStack result, boolean disabled)
    {
        this.materialAmount = materialAmount;
        this.additives = additives;
        this.result = result;
        this.resultType = FramingSawRecipeSerializer.findResultType(result);
        this.disabled = disabled;
    }

    void setId(ResourceKey<Recipe<?>> id)
    {
        this.id = id;
    }

    public ResourceKey<Recipe<?>> getId()
    {
        return id;
    }

    @Override
    public boolean matches(RecipeInput container, Level level)
    {
        return matchWithResult(toContainer(container), level).success();
    }

    public FramingSawRecipeMatchResult matchWithResult(Container container, Level level)
    {
        ItemStack input = container.getItem(0);
        if (input.isEmpty())
        {
            return FramingSawRecipeMatchResult.MATERIAL_VALUE;
        }

        int inputValue = FramingSawRecipeCalculation.getInputValue(input, level.isClientSide());
        int totalInputValue = inputValue * input.getCount();
        if (totalInputValue < materialAmount)
        {
            return FramingSawRecipeMatchResult.MATERIAL_VALUE;
        }

        long matLcm = FramingSawRecipeCalculation.getMaterialLCM(this, inputValue);
        if (matLcm > totalInputValue)
        {
            return FramingSawRecipeMatchResult.MATERIAL_LCM;
        }

        if (FramingSawRecipeCalculation.getOutputCount(materialAmount, result, matLcm) > result.getMaxStackSize())
        {
            return FramingSawRecipeMatchResult.OUTPUT_SIZE;
        }

        for (int idx = 0; idx < MAX_ADDITIVE_COUNT; idx++)
        {
            ItemStack stack = container.getItem(idx + 1);
            FramingSawRecipeAdditive additive = idx < additives.size() ? additives.get(idx) : null;

            boolean empty = stack.isEmpty();

            if (empty && additive == null)
            {
                continue;
            }

            if (!empty && additive == null)
            {
                return FramingSawRecipeMatchResult.UNEXPECTED_ADDITIVE[idx];
            }
            else if (empty /* && additive != null*/)
            {
                return FramingSawRecipeMatchResult.MISSING_ADDITIVE[idx];
            }
            else if (!additive.ingredient().test(stack))
            {
                return FramingSawRecipeMatchResult.INCORRECT_ADDITIVE[idx];
            }

            if (stack.getCount() < FramingSawRecipeCalculation.getAdditiveCount(this, additive, matLcm))
            {
                return FramingSawRecipeMatchResult.INSUFFICIENT_ADDITIVE[idx];
            }
        }
        return FramingSawRecipeMatchResult.SUCCESS;
    }

    public FramingSawRecipeCalculation makeCraftingCalculation(Container container, boolean client)
    {
        return new FramingSawRecipeCalculation(this, container, client);
    }

    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider access)
    {
        return result.copy();
    }

    private static Container toContainer(RecipeInput input)
    {
        SimpleContainer container = new SimpleContainer(input.size());
        for (int i = 0; i < input.size(); i++)
        {
            container.setItem(i, input.getItem(i));
        }
        return container;
    }

    public int getMaterialAmount()
    {
        return materialAmount;
    }

    public List<FramingSawRecipeAdditive> getAdditives()
    {
        return additives;
    }

    public ItemStack getResult()
    {
        return result;
    }

    public IBlockType getResultType()
    {
        return resultType;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    @Override
    public boolean isSpecial()
    {
        return true;
    }

    @Override
    public PlacementInfo placementInfo()
    {
        if (disabled)
        {
            return PlacementInfo.NOT_PLACEABLE;
        }

        List<Optional<Ingredient>> ingredients = new ArrayList<>(MAX_ADDITIVE_COUNT);
        for (int i = 0; i < MAX_ADDITIVE_COUNT; i++)
        {
            if (i < additives.size())
            {
                ingredients.add(Optional.of(additives.get(i).ingredient()));
            }
            else
            {
                ingredients.add(Optional.empty());
            }
        }
        return PlacementInfo.createFromOptionals(ingredients);
    }

    @Override
    public RecipeBookCategory recipeBookCategory()
    {
        return FBContent.RECIPE_BOOK_CATEGORY_FRAMING_SAW.get();
    }

    @Override
    public RecipeSerializer<FramingSawRecipe> getSerializer()
    {
        return FBContent.RECIPE_SERIALIZER_FRAMING_SAW_RECIPE.get();
    }

    @Override
    public RecipeType<FramingSawRecipe> getType()
    {
        return FBContent.RECIPE_TYPE_FRAMING_SAW_RECIPE.get();
    }
}
