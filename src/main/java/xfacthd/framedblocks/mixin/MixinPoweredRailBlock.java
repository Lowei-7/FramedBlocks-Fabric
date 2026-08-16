package xfacthd.framedblocks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PoweredRailBlock.class)
public abstract class MixinPoweredRailBlock
{
    /**
     * The vanilla constructor registers a default state that hardcodes the vanilla {@code SHAPE} property.
     * Framed rail-slope variants replace that property with a custom ascending-only shape property, so
     * {@code setValue(SHAPE, ...)} would throw. Skip setting the property when it isn't present on the block.
     */
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;setValue(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"
            )
    )
    private static Object framedblocks$guardSetValue(
            BlockState state, Property<?> property, Comparable<?> value,
            net.minecraft.world.level.block.state.BlockBehaviour.Properties props
    )
    {
        return state.hasProperty(property) ? state.setValue((Property) property, (Comparable) value) : state;
    }

    /**
     * Forge patch: route shape lookups through {@code getShapeProperty()} so the framed variants, which use a
     * custom ascending-only shape property, work with the vanilla powered-rail signal logic
     */
    @Redirect(
            method = "findPoweredRailSignal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"
            )
    )
    private Comparable<?> framedblocks$redirectShapeLookupFind(
            BlockState state, Property<?> property, Level level, BlockPos pos, BlockState adjState, boolean powered, int depth
    )
    {
        if (property == PoweredRailBlock.SHAPE)
        {
            PoweredRailBlock self = (PoweredRailBlock) (Object) this;
            return state.getValue(self.getShapeProperty());
        }
        return state.getValue(property);
    }

    /**
     * Forge patch: same redirect as above for {@code isSameRailWithPower}
     */
    @Redirect(
            method = "isSameRailWithPower",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"
            )
    )
    private Comparable<?> framedblocks$redirectShapeLookupSame(
            BlockState state, Property<?> property, Level level, BlockPos pos, boolean powered, int depth, RailShape shape
    )
    {
        if (property == PoweredRailBlock.SHAPE)
        {
            PoweredRailBlock self = (PoweredRailBlock) (Object) this;
            return state.getValue(self.getShapeProperty());
        }
        return state.getValue(property);
    }
}
