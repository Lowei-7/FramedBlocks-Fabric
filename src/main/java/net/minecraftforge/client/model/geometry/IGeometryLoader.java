package net.minecraftforge.client.model.geometry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface IGeometryLoader<T extends IUnbakedGeometry<T>>
{
    T read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws Exception;
}
