package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.menu.FramedStorageMenu;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FramedStorageBlockEntity extends FramedBlockEntity implements MenuProvider, Nameable, Clearable
{
    public static final Component TITLE = Utils.translate("title", "framed_secret_storage");
    public static final String INVENTORY_NBT_KEY = "inventory";

    public static final int INVENTORY_SIZE = 27;

    // Real inventory backing the FramedStorageMenu. The menu binds directly to this
    // container so every item change is written straight back into the block entity
    // and persisted to NBT on chunk save.
    private final Container inventory = new SimpleContainer(INVENTORY_SIZE)
    {
        @Override
        public void setItem(int slot, ItemStack stack)
        {
            super.setItem(slot, stack);
            FramedStorageBlockEntity.this.setChanged();
        }

        @Override
        public ItemStack removeItem(int slot, int amount)
        {
            ItemStack stack = super.removeItem(slot, amount);
            FramedStorageBlockEntity.this.setChanged();
            return stack;
        }
    };
    private Component customName = null;

    public FramedStorageBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_SECRET_STORAGE.get(), pos, state);
    }

    protected FramedStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public Container getInventory()
    {
        return inventory;
    }

    public void open(ServerPlayer player)
    {
        player.openMenu(this);
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

    public List<ItemStack> getDrops()
    {
        List<ItemStack> drops = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++)
        {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty())
            {
                drops.add(stack);
            }
        }
        return drops;
    }

    @Override
    public void clearContent()
    {
        inventory.clearContent();
    }

    public int getAnalogOutputSignal()
    {
        int stacks = 0;
        float fullness = 0;

        for (int i = 0; i < inventory.getContainerSize(); i++)
        {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty())
            {
                float sizeLimit = stack.getMaxStackSize();
                fullness += (float)stack.getCount() / sizeLimit;
                stacks++;
            }
        }

        fullness /= (float)inventory.getContainerSize();
        return Mth.floor(fullness * 14F) + (stacks > 0 ? 1 : 0);
    }

    public void setCustomName(Component customName)
    {
        this.customName = customName;
        setChanged();
    }

    @Override
    public Component getName()
    {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    public Component getCustomName()
    {
        return customName;
    }

    @Override //Prevent writing inventory contents
    public CompoundTag writeToBlueprint()
    {
        CompoundTag tag = saveWithoutMetadata(level.registryAccess());
        tag.remove(INVENTORY_NBT_KEY);
        tag.remove("custom_name");
        return tag;
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries)
    {
        ListTag itemsTag = new ListTag();
        for (int i = 0; i < inventory.getContainerSize(); i++)
        {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty())
            {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte)i);
                stack.save(registries, itemTag);
                itemsTag.add(itemTag);
            }
        }
        if (!itemsTag.isEmpty())
        {
            nbt.put(INVENTORY_NBT_KEY, itemsTag);
        }

        if (customName != null)
        {
            nbt.putString("custom_name", Component.Serializer.toJson(customName, registries));
        }
        super.saveAdditional(nbt, registries);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries)
    {
        super.loadAdditional(nbt, registries);

        inventory.clearContent();
        if (nbt.contains(INVENTORY_NBT_KEY, Tag.TAG_LIST))
        {
            ListTag itemsTag = nbt.getList(INVENTORY_NBT_KEY, Tag.TAG_COMPOUND);
            for (int i = 0; i < itemsTag.size(); i++)
            {
                CompoundTag itemTag = itemsTag.getCompound(i);
                int slot = itemTag.getByte("Slot") & 0xFF;
                if (slot >= 0 && slot < inventory.getContainerSize())
                {
                    inventory.setItem(slot, ItemStack.parse(registries, itemTag).orElse(ItemStack.EMPTY));
                }
            }
        }

        if (nbt.contains("custom_name", Tag.TAG_STRING))
        {
            customName = Component.Serializer.fromJson(nbt.getString("custom_name"), registries);
        }
    }

    protected Component getDefaultName()
    {
        return TITLE;
    }

    @Override
    public final Component getDisplayName()
    {
        return getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player)
    {
        return new FramedStorageMenu(windowId, inv, this);
    }
}
