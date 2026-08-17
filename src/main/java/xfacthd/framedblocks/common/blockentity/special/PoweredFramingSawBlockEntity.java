package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.crafting.*;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.menu.FramingSawMenu;
import xfacthd.framedblocks.common.util.EntityAwareEnergyStorage;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PoweredFramingSawBlockEntity extends BlockEntity
{
    public static final int ENERGY_CAPACITY = 5000;
    public static final int ENERGY_MAX_INSERT = 250;
    public static final int ENERGY_CONSUMPTION = 50;
    private static final boolean INSERT_ENERGY_DEBUG = false;
    private static final long ACTIVE_TIMEOUT = 40;
    public static final int MAX_PROGRESS = 30;

    private final ItemStackHandler itemHandler = new ItemStackHandler(FramingSawMenu.SLOT_RESULT + 1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            PoweredFramingSawBlockEntity.this.onContentsChanged(slot);
        }
    };
    private final RecipeWrapper recipeWrapper = new RecipeWrapper(itemHandler);
    private final EntityAwareEnergyStorage energyStorage = new EntityAwareEnergyStorage(
            ENERGY_CAPACITY, ENERGY_MAX_INSERT, 0, this
    );
    private FramingSawRecipeCache cache = null;
    private ResourceLocation selectedRecipeId = null;
    private FramingSawRecipe selectedRecipe = null;
    private boolean active = false;
    private long lastActive = 0;
    private boolean recipeSatisfied = false;
    private FramingSawRecipeMatchResult matchResult = null;
    private FramingSawRecipeCalculation calculation = null;
    private int outputCount = 0;
    private int progress = 0;
    private boolean needSaving = false;
    private boolean inhibitUpdate = false;
    private boolean internalAccess = false;

    public PoweredFramingSawBlockEntity(BlockPos pPos, BlockState pBlockState)
    {
        super(FBContent.BE_TYPE_POWERED_FRAMING_SAW.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PoweredFramingSawBlockEntity be)
    {
        if (!level.isClientSide() && INSERT_ENERGY_DEBUG)
        {
            be.energyStorage.receiveEnergy(ENERGY_MAX_INSERT, false);
        }

        if ((be.active || level.getGameTime() - be.lastActive > ACTIVE_TIMEOUT) && be.canRun())
        {
            if (!be.active)
            {
                be.active = true;
                level.setBlockAndUpdate(pos, state.setValue(PropertyHolder.ACTIVE, true));
            }

            be.energyStorage.extractEnergyInternal(ENERGY_CONSUMPTION);
            be.progress++;

            if (be.progress >= MAX_PROGRESS)
            {
                be.progress = 0;

                ItemStack result = be.selectedRecipe.getResult().copy();
                result.setCount(be.outputCount);
                be.internalAccess = true;
                be.inhibitUpdate = true;

                // Extraction des additifs
                for (int i = 0; i < be.selectedRecipe.getAdditives().size(); i++)
                {
                    int slot = i + FramingSawMenu.SLOT_ADDITIVE_FIRST;
                    be.itemHandler.extractItem(slot, 1, false);
                }

                // Extraction de l'entrée
                be.itemHandler.extractItem(FramingSawMenu.SLOT_INPUT, 1, false);

                be.inhibitUpdate = false;

                // Insertion du résultat
                ItemStack remainder = be.itemHandler.insertItem(FramingSawMenu.SLOT_RESULT, result, false);
                be.internalAccess = false;
            }
        }
        else if (be.active)
        {
            be.active = false;
            level.setBlockAndUpdate(pos, state.setValue(PropertyHolder.ACTIVE, false));
            be.lastActive = level.getGameTime();

            if (!be.recipeSatisfied)
            {
                be.progress = 0;
            }
        }

        if (be.needSaving)
        {
            be.setChanged();
            be.needSaving = false;
        }
    }

    private boolean canRun()
    {
        if (selectedRecipe == null || !recipeSatisfied) return false;
        if (energyStorage.getEnergyStored() < ENERGY_CONSUMPTION) return false;

        // Vérification de l'espace de sortie
        ItemStack output = itemHandler.getStackInSlot(FramingSawMenu.SLOT_RESULT);
        if (!output.isEmpty() && output.getItem() != selectedRecipe.getResult().getItem()) return false;
        if (output.getCount() + outputCount > output.getMaxStackSize()) return false;

        return true;
    }

    private void checkRecipeSatisfied()
    {
        if (selectedRecipe != null)
        {
            matchResult = selectedRecipe.matchWithResult(recipeWrapper, level);
            recipeSatisfied = matchResult.success();
        }
        else
        {
            matchResult = null;
            recipeSatisfied = false;
        }

        if (recipeSatisfied)
        {
            calculation = selectedRecipe.makeCraftingCalculation(recipeWrapper, false);
            outputCount = calculation.getOutputCount();
        }
        else
        {
            calculation = null;
            outputCount = 0;
            progress = 0;
        }
    }

    private void onContentsChanged(int slot)
    {
        needSaving = true;
        if (slot != FramingSawMenu.SLOT_RESULT && !inhibitUpdate)
        {
            checkRecipeSatisfied();
        }
    }

    private boolean isValidItem(int slot, ItemStack stack)
    {
        if (slot == FramingSawMenu.SLOT_INPUT)
        {
            return cache.getMaterialValue(stack.getItem()) > 0;
        }
        else if (slot < FramingSawMenu.SLOT_RESULT)
        {
            if (selectedRecipe != null)
            {
                int idx = slot - FramingSawMenu.SLOT_ADDITIVE_FIRST;
                List<FramingSawRecipeAdditive> additives = selectedRecipe.getAdditives();
                if (!additives.isEmpty() && idx < additives.size())
                {
                    return additives.get(idx).ingredient().test(stack);
                }
                return false;
            }
            return true;
        }
        else if (slot == FramingSawMenu.SLOT_RESULT)
        {
            return internalAccess;
        }
        throw new IllegalArgumentException("Invalid slot: " + slot);
    }

    public void selectRecipe(FramingSawRecipe recipe)
    {
        ResourceLocation lastId = selectedRecipeId;
        selectedRecipe = recipe;
        selectedRecipeId = recipe == null ? null : recipe.getId();
        checkRecipeSatisfied();
        if (!Objects.equals(lastId, selectedRecipeId))
        {
            needSaving = true;
        }
    }

    public FramingSawRecipe getSelectedRecipe()
    {
        return selectedRecipe;
    }

    public FramingSawRecipeMatchResult getMatchResult()
    {
        return matchResult;
    }

    public int getProgress()
    {
        return progress;
    }

    public ItemStackHandler getItemHandler()
    {
        return itemHandler;
    }

    public int getEnergy()
    {
        return energyStorage.getEnergyStored();
    }

    public void dropContents(Consumer<ItemStack> dropper)
    {
        inhibitUpdate = true;
        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                dropper.accept(stack);
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        inhibitUpdate = false;
    }

    public void onLoad()
    {
        //noinspection ConstantConditions
        cache = FramingSawRecipeCache.get(level.isClientSide());
        if (selectedRecipeId != null && !level.isClientSide())
        {
            FramingSawRecipe recipe = level.getRecipeManager().byKey(selectedRecipeId)
                    .filter(FramingSawRecipe.class::isInstance)
                    .map(FramingSawRecipe.class::cast)
                    .orElse(null);
            selectRecipe(recipe);
        }
    }

    public boolean isUsableByPlayer(Player player)
    {
        //noinspection ConstantConditions
        if (level.getBlockEntity(worldPosition) != this)
        {
            return false;
        }
        return !(player.distanceToSqr((double)worldPosition.getX() + 0.5D, (double)worldPosition.getY() + 0.5D, (double)worldPosition.getZ() + 0.5D) > 64.0D);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        if (selectedRecipe != null)
        {
            tag.putString("recipe", selectedRecipe.getId().toString());
        }
        CompoundTag inventoryTag = new CompoundTag();
        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                inventoryTag.put("Slot" + i, stack.save(registries, new CompoundTag()));
            }
        }
        tag.put("inventory", inventoryTag);
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putInt("progress", progress);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        if (tag.contains("recipe"))
        {
            selectedRecipeId = ResourceLocation.parse(tag.getString("recipe"));
        }
        CompoundTag inventoryTag = tag.getCompound("inventory");
        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            if (inventoryTag.contains("Slot" + i))
            {
                itemHandler.setStackInSlot(i, ItemStack.parse(registries, inventoryTag.getCompound("Slot" + i)).orElse(ItemStack.EMPTY));
            }
        }
        energyStorage.setEnergy(tag.getInt("energy"));
        progress = tag.getInt("progress");
    }
}
