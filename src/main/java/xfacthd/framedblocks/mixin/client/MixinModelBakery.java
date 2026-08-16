package xfacthd.framedblocks.mixin.client;

import net.minecraft.client.resources.model.ModelBakery;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xfacthd.framedblocks.client.model.FramedModelBaking;

@Mixin(ModelBakery.class)
public abstract class MixinModelBakery
{
    @Inject(method = "bakeModels", at = @At("RETURN"))
    private void framedblocks$onModelsBaked(CallbackInfo ci)
    {
        ModelBakery self = (ModelBakery) (Object) this;
        FramedModelBaking.onModelsBaked(self.getBakedTopLevelModels());
    }
}
