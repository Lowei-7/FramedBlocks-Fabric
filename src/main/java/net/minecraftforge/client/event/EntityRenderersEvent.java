package net.minecraftforge.client.event;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EntityRenderersEvent
{
    public static class RegisterRenderers extends EntityRenderersEvent
    {
        public <T extends BlockEntity> void registerBlockEntityRenderer(
                BlockEntityType<T> type,
                BlockEntityRendererProvider<T> provider
        )
        {
            // Stub: In Fabric, use BlockEntityRendererFactories.register()
        }
    }
}
