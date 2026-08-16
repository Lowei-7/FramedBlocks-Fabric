package xfacthd.framedblocks.mixin;

import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DetectorRailBlock.class)
public abstract class MixinDetectorRailBlock
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
}