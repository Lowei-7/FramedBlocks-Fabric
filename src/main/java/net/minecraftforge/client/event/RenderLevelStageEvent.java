package net.minecraftforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;

public class RenderLevelStageEvent
{
    public enum Stage
    {
        AFTER_SOLID_BLOCKS,
        AFTER_CUTOUT_MIPPED_BLOCKS,
        AFTER_CUTOUT_BLOCKS,
        AFTER_TRANSLUCENT_BLOCKS,
        AFTER_TRIPWIRE_BLOCKS,
        AFTER_PARTICLES,
        AFTER_WEATHER,
        AFTER_LEVEL
    }

    private final Stage stage;
    private final PoseStack poseStack;
    private final LevelRenderer levelRenderer;
    private final float partialTick;

    public RenderLevelStageEvent(Stage stage, PoseStack poseStack, LevelRenderer levelRenderer, float partialTick)
    {
        this.stage = stage;
        this.poseStack = poseStack;
        this.levelRenderer = levelRenderer;
        this.partialTick = partialTick;
    }

    public Stage getStage()
    {
        return stage;
    }

    public PoseStack getPoseStack()
    {
        return poseStack;
    }

    public LevelRenderer getLevelRenderer()
    {
        return levelRenderer;
    }

    public float getPartialTick()
    {
        return partialTick;
    }
}
