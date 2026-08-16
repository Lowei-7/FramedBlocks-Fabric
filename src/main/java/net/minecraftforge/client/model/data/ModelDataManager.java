package net.minecraftforge.client.model.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

/**
 * Fabric stub for Forge ModelDataManager.
 * In Fabric, block entity render data is accessed via BlockEntity.getRenderData() or similar.
 */
public class ModelDataManager
{
    @Nullable
    public static ModelData getModelData(LevelAccessor level, BlockPos pos)
    {
        // In Fabric, we return EMPTY as a fallback - connected textures won't fully work
        // without a proper mixin into ModelDataManager
        return ModelData.EMPTY;
    }

    // Instance method to match AppearanceHelper's usage: level.getModelDataManager()
    // This won't actually be called since BlockGetter doesn't have this method in Fabric
    // The AppearanceHelper needs to be adapted to use this differently
}