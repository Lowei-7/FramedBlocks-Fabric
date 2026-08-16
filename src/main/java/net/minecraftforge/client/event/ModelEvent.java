package net.minecraftforge.client.event;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import java.util.Map;

public class ModelEvent
{
    public static class RegisterGeometryLoaders extends ModelEvent
    {
        public void register(String id, IGeometryLoader<?> loader)
        {
            // Stub
        }
    }

    public static class RegisterAdditional extends ModelEvent
    {
        public void register(ResourceLocation model)
        {
            // Stub
        }
    }

    public static class ModifyBakingResult extends ModelEvent
    {
        private final Map<ResourceLocation, BakedModel> models;
        private final ModelBakery modelBakery;

        public ModifyBakingResult(Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery)
        {
            this.models = models;
            this.modelBakery = modelBakery;
        }

        public Map<ResourceLocation, BakedModel> getModels()
        {
            return models;
        }

        public ModelBakery getModelBakery()
        {
            return modelBakery;
        }
    }

    public static class BakingCompleted extends ModelEvent
    {
        private final Map<ResourceLocation, BakedModel> models;
        private final ModelBakery modelBakery;

        public BakingCompleted(Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery)
        {
            this.models = models;
            this.modelBakery = modelBakery;
        }

        public Map<ResourceLocation, BakedModel> getModels()
        {
            return models;
        }

        public ModelBakery getModelBakery()
        {
            return modelBakery;
        }
    }
}
