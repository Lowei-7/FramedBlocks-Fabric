package xfacthd.framedblocks.mixin.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xfacthd.framedblocks.api.block.FramedBlockEntity;

/**
 * Mirrors the Forge patch in LevelChunk.replaceWithPacketData, which handles the block entity
 * data sent in the chunk packet with {@link BlockEntity#handleUpdateTag(CompoundTag)} instead of
 * {@link BlockEntity#load(CompoundTag)}. The update tag uses the network serialization format
 * which is not load()-compatible for FramedBlockEntity and its subclasses (the camo "type" is a
 * sync ID int instead of a registry name string). On Forge this method is supplied by the
 * IForgeBlockEntity interface; on Fabric it only exists on the FramedBlockEntity hierarchy.
 */
@Mixin(LevelChunk.class)
public abstract class MixinLevelChunk
{
    @Redirect(
            method = "method_31716",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;load(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    private void framedblocks$redirectChunkDataLoad(BlockEntity blockEntity, CompoundTag tag)
    {
        if (blockEntity instanceof FramedBlockEntity framedBe)
        {
            framedBe.handleUpdateTag(tag);
        }
        else
        {
            blockEntity.load(tag);
        }
    }
}
