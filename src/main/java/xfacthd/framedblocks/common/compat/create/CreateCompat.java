package xfacthd.framedblocks.common.compat.create;

import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.contraption.BlockMovementChecks;
import com.simibubi.create.api.schematic.nbt.SafeNbtWriterRegistry;
import com.simibubi.create.api.schematic.requirement.SchematicRequirementRegistries;
import com.simibubi.create.api.schematic.state.SchematicStateFilterRegistry;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.FenceGateMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.LeverMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.TrapdoorMovingInteraction;
import com.simibubi.create.foundation.block.connected.CTModel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.fml.ModList;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import xfacthd.framedblocks.common.util.fabric.RegistryObject;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.client.data.ConTexDataHandler;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.api.compat.create.FramedBlockSafeNbtWriter;
import xfacthd.framedblocks.api.compat.create.FramedBlockEntityItemRequirement;
import xfacthd.framedblocks.common.blockentity.special.FramedStorageBlockEntity;
import xfacthd.framedblocks.common.compat.create.schematic.nbt.FramedChiseledBookshelfSafeNbtWriter;
import xfacthd.framedblocks.common.compat.create.schematic.nbt.FramedItemFrameSafeNbtWriter;
import xfacthd.framedblocks.common.compat.create.schematic.requirements.FramedDoorBlockItemRequirement;
import xfacthd.framedblocks.common.compat.create.schematic.requirements.FramedFlowerPotBlockEntityItemRequirement;
import xfacthd.framedblocks.common.compat.create.schematic.requirements.FramedItemFrameBlockEntityItemRequirement;
import xfacthd.framedblocks.common.compat.create.schematic.requirements.FramedSpecialDoubleBlockItemRequirements;
import xfacthd.framedblocks.common.compat.create.schematic.state.FramedChiseledBookshelfStateFilter;
import xfacthd.framedblocks.common.util.RegisteredBE;

import java.util.Map;

public final class CreateCompat
{
    public static void init()
    {
        if (ModList.get().isLoaded("create"))
        {
            // Safeguard against potential changes in Create since the ct context property is not exposed as API
            try
            {
                if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT)
                {
                    GuardedClientAccess.init();
                }
            }
            catch (Throwable e)
            {
                FramedBlocks.LOGGER.warn("An error occured while initializing client-only Create integration!", e);
            }
        }
    }

    public static void commonSetup()
    {
        if (ModList.get().isLoaded("create"))
        {
            try
            {
                GuardedAccess.init();
            }
            catch (Throwable e)
            {
                FramedBlocks.LOGGER.warn("An error occured while initializing Create integration!", e);
            }
        }
    }

    private static final class GuardedAccess
    {
        private static final Map<RegisteredBE<?>, SchematicRequirementRegistries.BlockEntityRequirement> SPECIAL_REQUIREMENT_BLOCK_ENTITIES = Map.of(
                FBContent.BE_TYPE_FRAMED_FLOWER_POT, new FramedFlowerPotBlockEntityItemRequirement(),
                FBContent.BE_TYPE_FRAMED_ITEM_FRAME, new FramedItemFrameBlockEntityItemRequirement()
        );
        private static final Map<RegisteredBE<?>, SafeNbtWriterRegistry.SafeNbtWriter> SPECIAL_NBT_BLOCK_ENTITIES = Map.of(
                FBContent.blockEntityTypeFramedChest, new FramedBlockSafeNbtWriter(FramedStorageBlockEntity.INVENTORY_NBT_KEY),
                FBContent.BE_TYPE_FRAMED_SECRET_STORAGE, new FramedBlockSafeNbtWriter(FramedStorageBlockEntity.INVENTORY_NBT_KEY),
                FBContent.BE_TYPE_FRAMED_ITEM_FRAME, new FramedItemFrameSafeNbtWriter(),
                FBContent.BE_TYPE_FRAMED_CHISELED_BOOKSHELF, new FramedChiseledBookshelfSafeNbtWriter()
        );

        public static void init()
        {
            // The interaction behaviour implementations are not exposed as API
            try
            {
                registerInteractionBehaviour(FBContent.BLOCK_FRAMED_LEVER, new LeverMovingInteraction());
                registerInteractionBehaviour(FBContent.BLOCK_FRAMED_DOOR, new DoorMovingInteraction());
                registerInteractionBehaviour(FBContent.BLOCK_FRAMED_TRAP_DOOR, new TrapdoorMovingInteraction());
                registerInteractionBehaviour(FBContent.BLOCK_FRAMED_FENCE_GATE, new FenceGateMovingInteraction());
            }
            catch (Throwable t)
            {
                FramedBlocks.LOGGER.warn("An error occured while registering MovingInteractions for Create contraptions!", t);
            }

            FramedBlockMovementChecks movementChecks = new FramedBlockMovementChecks();
            BlockMovementChecks.registerMovementNecessaryCheck(movementChecks);
            BlockMovementChecks.registerMovementAllowedCheck(movementChecks);
            BlockMovementChecks.registerBrittleCheck(movementChecks);
            BlockMovementChecks.registerAttachedCheck(movementChecks);
            BlockMovementChecks.registerNotSupportiveCheck(movementChecks);

            registerBlockItemRequirement(
                    FBContent.BLOCK_FRAMED_DOUBLE_SLAB,
                    new FramedSpecialDoubleBlockItemRequirements(FBContent.BLOCK_FRAMED_SLAB)
            );
            registerBlockItemRequirement(
                    FBContent.BLOCK_FRAMED_DOUBLE_PANEL,
                    new FramedSpecialDoubleBlockItemRequirements(FBContent.BLOCK_FRAMED_PANEL)
            );
            registerBlockItemRequirement(FBContent.BLOCK_FRAMED_DOOR, FramedDoorBlockItemRequirement.INSTANCE);
            registerBlockItemRequirement(FBContent.BLOCK_FRAMED_IRON_DOOR, FramedDoorBlockItemRequirement.INSTANCE);

            for (RegisteredBE<?> blockEntity : FBContent.getFramedBlockEntities())
            {
                registerBlockEntityItemRequirement(blockEntity, SPECIAL_REQUIREMENT_BLOCK_ENTITIES.getOrDefault(
                        blockEntity, FramedBlockEntityItemRequirement.INSTANCE
                ));
                registerSafeNbtWriter(blockEntity, SPECIAL_NBT_BLOCK_ENTITIES.getOrDefault(
                        blockEntity, FramedBlockSafeNbtWriter.INSTANCE
                ));
            }

            registerStateFilter(FBContent.BLOCK_FRAMED_CHISELED_BOOKSHELF, new FramedChiseledBookshelfStateFilter());
        }

        private static void registerInteractionBehaviour(RegistryObject<Block> block, MovingInteractionBehaviour behaviour)
        {
            MovingInteractionBehaviour.REGISTRY.register(block.get(), behaviour);
        }

        private static void registerBlockItemRequirement(RegistryObject<Block> type, SchematicRequirementRegistries.BlockRequirement itemRequirement)
        {
            SchematicRequirementRegistries.BLOCKS.register(type.get(), itemRequirement);
        }

        private static void registerBlockEntityItemRequirement(RegisteredBE<?> type, SchematicRequirementRegistries.BlockEntityRequirement itemRequirement)
        {
            SchematicRequirementRegistries.BLOCK_ENTITIES.register(type.get(), itemRequirement);
        }

        private static void registerSafeNbtWriter(RegisteredBE<?> type, SafeNbtWriterRegistry.SafeNbtWriter writer)
        {
            SafeNbtWriterRegistry.REGISTRY.register(type.get(), writer);
        }

        private static void registerStateFilter(RegistryObject<Block> block, SchematicStateFilterRegistry.StateFilter filter)
        {
            SchematicStateFilterRegistry.REGISTRY.register(block.get(), filter);
        }
    }

    private static final class GuardedClientAccess
    {
        public static void init()
        {
            ModelProperty<?> ctProperty = ObfuscationReflectionHelper.getPrivateValue(CTModel.class, null, "CT_PROPERTY");
            ConTexDataHandler.addConTexProperty(ctProperty);
        }
    }



    private CreateCompat() { }
}
