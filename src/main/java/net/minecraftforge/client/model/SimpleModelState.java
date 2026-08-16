package net.minecraftforge.client.model;

import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.ModelState;

public class SimpleModelState implements ModelState
{
    private final Transformation transformation;
    private final boolean uvLocked;

    public SimpleModelState(Transformation transformation, boolean uvLocked)
    {
        this.transformation = transformation;
        this.uvLocked = uvLocked;
    }

    @Override
    public Transformation getRotation()
    {
        return transformation;
    }

    @Override
    public boolean isUvLocked()
    {
        return uvLocked;
    }
}
