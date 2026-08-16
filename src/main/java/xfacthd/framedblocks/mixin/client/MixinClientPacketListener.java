package xfacthd.framedblocks.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.common.crafting.FramingSawRecipeCache;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener
{
    @Inject(method = "handleUpdateRecipes", at = @At("RETURN"))
    private void framedblocks$onRecipesUpdated(ClientboundUpdateRecipesPacket packet, CallbackInfo ci)
    {
        ClientPacketListener self = (ClientPacketListener) (Object) this;
        FramingSawRecipeCache.get(true).update(self.getRecipeManager());
    }

    @Redirect(
            method = "method_38542",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;loadWithComponents(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V"
            )
    )
    private void framedblocks$redirectLoad(BlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider registries, ClientboundBlockEntityDataPacket packet)
    {
        if (blockEntity instanceof FramedBlockEntity framedBe)
        {
            Connection conn = ((ClientPacketListener) (Object) this).getConnection();
            framedBe.onDataPacket(conn, packet);
        }
        else
        {
            blockEntity.loadWithComponents(tag, registries);
        }
    }
}