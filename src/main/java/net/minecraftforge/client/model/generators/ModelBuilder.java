package net.minecraftforge.client.model.generators;

import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class ModelBuilder<T extends ModelBuilder<T>>
{
    protected final ResourceLocation outputLocation;
    protected final PackOutput output;

    public ModelBuilder(ResourceLocation outputLocation, PackOutput output)
    {
        this.outputLocation = outputLocation;
        this.output = output;
    }

    @SuppressWarnings("unchecked")
    protected T self()
    {
        return (T) this;
    }

    public JsonObject toJson()
    {
        JsonObject json = new JsonObject();
        json.addProperty("parent", outputLocation.toString());
        return json;
    }
}
