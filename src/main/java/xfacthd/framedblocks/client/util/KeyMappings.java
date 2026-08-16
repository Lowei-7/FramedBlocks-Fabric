package xfacthd.framedblocks.client.util;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.glfw.GLFW;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.util.FramedConstants;

import java.util.function.Supplier;

public final class KeyMappings
{
    public static final String KEY_CATEGORY = FramedConstants.MOD_ID + ".key.categories.framedblocks";
    public static final Supplier<KeyMapping> KEYMAPPING_UPDATE_CULLING = () -> new KeyMapping(FramedConstants.MOD_ID + ".key.update_cull", GLFW.GLFW_KEY_F9, KEY_CATEGORY);
    public static final Supplier<KeyMapping> KEYMAPPING_WIPE_CACHE = () -> new KeyMapping(FramedConstants.MOD_ID + ".key.wipe_cache", -1, KEY_CATEGORY);

    public static void onClientTick(Minecraft client)
    {
        Level level = client.level;
        if (level == null || client.screen != null)
        {
            return;
        }

        if (KEYMAPPING_UPDATE_CULLING.get().consumeClick())
        {
            HitResult hit = client.hitResult;
            if (hit instanceof BlockHitResult blockHit && level.getBlockEntity(blockHit.getBlockPos()) instanceof FramedBlockEntity be)
            {
                try
                {
                    be.updateCulling(true, true);
                }
                catch (Throwable throwable)
                {
                    FramedBlocks.LOGGER.error(
                            "Encountered unexpected exception while updating culling of " + be.getBlockState().getBlock(),
                            throwable
                    );
                }

                BlockPos pos = blockHit.getBlockPos();
                Component blockName = be.getBlockState().getBlock().getName();

                Component msg = Component.literal("Culling updated for '")
                        .append(blockName)
                        .append("' at ")
                        .append(Component.literal(
                                String.format("{x=%d, y=%d, z=%d}", pos.getX(), pos.getY(), pos.getZ())
                        ));

                //noinspection ConstantConditions
                client.player.displayClientMessage(msg, true);
            }
        }

        if (KEYMAPPING_WIPE_CACHE.get().consumeClick())
        {
            FramedClientUtils.clearModelCaches();

            //noinspection ConstantConditions
            client.player.displayClientMessage(Component.literal("Model cache cleared"), true);
        }
    }

    private KeyMappings() { }
}
