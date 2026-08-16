package xfacthd.framedblocks.common.net;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import xfacthd.framedblocks.common.blockentity.special.FramedSignBlockEntity;

@Environment(EnvType.CLIENT)
public final class FramedSignScreenOpener
{
    public static void openScreen(Minecraft client, OpenSignScreenPacket packet)
    {
        BlockPos pos = packet.pos();
        if (client.level == null) { return; }
        BlockEntity be = client.level.getBlockEntity(pos);
        if (!(be instanceof FramedSignBlockEntity sign)) { return; }

        // FramedSignScreen is excluded from the build until the rendering port (Phase 3/4) is done
        openScreenReflective(sign, packet.frontText());
    }

    private static void openScreenReflective(FramedSignBlockEntity sign, boolean front)
    {
        try
        {
            Class<?> screenClass = Class.forName("xfacthd.framedblocks.client.screen.FramedSignScreen");
            Object screen = screenClass.getConstructor(FramedSignBlockEntity.class, boolean.class)
                    .newInstance(sign, front);
            Minecraft.getInstance().setScreen((net.minecraft.client.gui.screens.Screen) screen);
        }
        catch (ReflectiveOperationException e)
        {
            xfacthd.framedblocks.FramedBlocks.LOGGER.warn("Unable to open FramedSignScreen, screen class not yet ported", e);
        }
    }

    private FramedSignScreenOpener() { }
}
