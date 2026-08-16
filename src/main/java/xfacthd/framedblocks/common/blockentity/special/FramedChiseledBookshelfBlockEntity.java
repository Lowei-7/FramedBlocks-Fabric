package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.common.FBContent;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FramedChiseledBookshelfBlockEntity extends FramedBlockEntity
{
    public static final String INVENTORY_NBT_KEY = "inventory";
    public static final String LAST_SLOT_NBT_KEY = "last_slot";

    private ItemStack[] inventory;
    private int lastInteractedSlot = -1;

    public FramedChiseledBookshelfBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_CHISELED_BOOKSHELF.get(), pos, state);
        inventory = createInventory();
    }

    private static ItemStack[] createInventory()
    {
        ItemStack[] inventory = new ItemStack[ChiseledBookShelfBlockEntity.MAX_BOOKS_IN_STORAGE];
        Arrays.fill(inventory, ItemStack.EMPTY);
        return inventory;
    }

    public void placeBook(ItemStack stack, int slot)
    {
        inventory[slot] = stack;
        updateState(slot);
        setChanged();
    }

    public ItemStack takeBook(int slot)
    {
        ItemStack stack = inventory[slot];
        inventory[slot] = ItemStack.EMPTY;
        updateState(slot);
        setChanged();
        return stack;
    }

    private void updateState(int slot)
    {
        lastInteractedSlot = slot;

        BlockState state = getBlockState();
        for (int i = 0; i < ChiseledBookShelfBlockEntity.MAX_BOOKS_IN_STORAGE; i++)
        {
            BooleanProperty prop = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i);
            state = state.setValue(prop, !inventory[i].isEmpty());
        }
        //noinspection ConstantConditions
        level.setBlockAndUpdate(worldPosition, state);
    }

    public void forceStateUpdate()
    {
        updateState(lastInteractedSlot);
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
    }

    public List<ItemStack> getDrops()
    {
        List<ItemStack> drops = new ArrayList<>();
        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];
            if (!stack.isEmpty())
            {
                drops.add(stack);
            }
        }
        return drops;
    }

    public void clearContents()
    {
        for (int i = 0; i < inventory.length; i++)
        {
            inventory[i] = ItemStack.EMPTY;
        }
    }

    public int getAnalogOutputSignal()
    {
        return lastInteractedSlot + 1;
    }

    @Override //Prevent writing inventory contents
    public CompoundTag writeToBlueprint()
    {
        CompoundTag tag = saveWithoutMetadata(level.registryAccess());
        tag.remove(INVENTORY_NBT_KEY);
        tag.remove(LAST_SLOT_NBT_KEY);
        return tag;
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries)
    {
        CompoundTag inventoryTag = new CompoundTag();
        for (int i = 0; i < inventory.length; i++)
        {
            if (!inventory[i].isEmpty())
            {
                inventoryTag.put("Slot" + i, inventory[i].save(registries, new CompoundTag()));
            }
        }
        nbt.put(INVENTORY_NBT_KEY, inventoryTag);
        nbt.putInt(LAST_SLOT_NBT_KEY, lastInteractedSlot);
        super.saveAdditional(nbt, registries);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries)
    {
        super.loadAdditional(nbt, registries);
        CompoundTag inventoryTag = nbt.getCompound(INVENTORY_NBT_KEY);
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventoryTag.contains("Slot" + i))
            {
                inventory[i] = ItemStack.parse(registries, inventoryTag.getCompound("Slot" + i)).orElse(ItemStack.EMPTY);
            }
            else
            {
                inventory[i] = ItemStack.EMPTY;
            }
        }
        lastInteractedSlot = nbt.getInt(LAST_SLOT_NBT_KEY);
    }
}
