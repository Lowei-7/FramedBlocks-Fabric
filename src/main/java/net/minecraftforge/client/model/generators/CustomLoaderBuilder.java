package net.minecraftforge.client.model.generators;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class CustomLoaderBuilder<T extends ModelBuilder<T>>
{
    protected final T parent;
    protected final ResourceLocation loaderId;

    protected CustomLoaderBuilder(T parent, ResourceLocation loaderId)
    {
        this.parent = parent;
        this.loaderId = loaderId;
    }

    public static <T extends ModelBuilder<T>> CustomLoaderBuilder<T> begin(T parent, ResourceLocation loaderId)
    {
        return new CustomLoaderBuilder<>(parent, loaderId);
    }

    public T end()
    {
        return parent;
    }

    public JsonObject toJson(JsonObject json)
    {
        json.addProperty("loader", loaderId.toString());
        return json;
    }
}
