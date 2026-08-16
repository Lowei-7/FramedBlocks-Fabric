package net.minecraftforge.client.model.data;

import java.util.IdentityHashMap;
import java.util.Map;

public class ModelData
{
    private final Map<ModelProperty<?>, Object> data;

    public static final ModelData EMPTY = new ModelData();

    private ModelData()
    {
        this.data = new IdentityHashMap<>();
    }

    private ModelData(Map<ModelProperty<?>, Object> data)
    {
        this.data = new IdentityHashMap<>(data);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public Builder derive()
    {
        return new Builder(data);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ModelProperty<T> property)
    {
        return (T) data.get(property);
    }

    public static class Builder
    {
        private final Map<ModelProperty<?>, Object> data = new IdentityHashMap<>();

        private Builder() { }

        private Builder(Map<ModelProperty<?>, Object> data)
        {
            this.data.putAll(data);
        }

        public <T> Builder with(ModelProperty<T> property, T value)
        {
            data.put(property, value);
            return this;
        }

        public ModelData build()
        {
            return new ModelData(data);
        }
    }
}
