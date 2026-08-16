package xfacthd.framedblocks.client.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FileUtils;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.model.SolidFrameMode;
import xfacthd.framedblocks.api.predicate.contex.ConTexMode;
import xfacthd.framedblocks.api.util.CamoMessageVerbosity;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.client.screen.overlay.BlockInteractOverlay;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class ClientConfig
{
    public static final ClientConfig INSTANCE = new ClientConfig();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "framedblocks-client.json");

    private boolean showGhostBlocks = true;
    private boolean altGhostRenderer = false;
    private boolean fancyHitboxes = true;
    private boolean detailedCulling = true;
    private boolean useDiscreteUVSteps = false;
    private ConTexMode conTexMode = ConTexMode.DETAILED;
    private CamoMessageVerbosity camoMessageVerbosity = CamoMessageVerbosity.DEFAULT;
    private boolean forceAoOnGlowingBlocks = false;
    private boolean showAllRecipePermutationsInEmi = true;
    private SolidFrameMode solidFrameMode = SolidFrameMode.DEFAULT;
    private boolean showButtonPlateOverlay = true;
    private boolean showSpecialCubeOverlay = true;

    public static BlockInteractOverlay.Mode toggleYSlopeMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode toggleWaterlogMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode stateLockMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode splitLineMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode reinforcementMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode camoRotationMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode prismOffsetMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode frameBackgroundMode = BlockInteractOverlay.Mode.DETAILED;
    public static BlockInteractOverlay.Mode oneWayWindowMode = BlockInteractOverlay.Mode.DETAILED;

    public ClientConfig()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> loadConfig());
    }

    public static boolean showGhostBlocks() { return INSTANCE.showGhostBlocks; }
    public static boolean altGhostRenderer() { return INSTANCE.altGhostRenderer; }
    public static boolean fancyHitboxes() { return INSTANCE.fancyHitboxes; }
    public static boolean detailedCulling() { return INSTANCE.detailedCulling; }
    public static boolean useDiscreteUVSteps() { return INSTANCE.useDiscreteUVSteps; }
    public static ConTexMode conTexMode() { return INSTANCE.conTexMode; }
    public static CamoMessageVerbosity camoMessageVerbosity() { return INSTANCE.camoMessageVerbosity; }
    public static boolean forceAoOnGlowingBlocks() { return INSTANCE.forceAoOnGlowingBlocks; }
    public static boolean showAllRecipePermutationsInEmi() { return INSTANCE.showAllRecipePermutationsInEmi; }
    public static SolidFrameMode solidFrameMode() { return INSTANCE.solidFrameMode; }
    public static boolean showButtonPlateOverlay() { return INSTANCE.showButtonPlateOverlay; }
    public static boolean showSpecialCubeOverlay() { return INSTANCE.showSpecialCubeOverlay; }

    private static void readOverlayMode(JsonObject json, String key, BlockInteractOverlay.Mode defaultMode)
    {
        try
        {
            if (json.has(key))
            {
                BlockInteractOverlay.Mode mode = BlockInteractOverlay.Mode.valueOf(json.get(key).getAsString());
                setOverlayMode(key, mode);
            }
            else
            {
                setOverlayMode(key, defaultMode);
            }
        }
        catch (Exception e)
        {
            setOverlayMode(key, defaultMode);
        }
    }

    private static void setOverlayMode(String key, BlockInteractOverlay.Mode mode)
    {
        switch (key)
        {
            case "toggleYSlopeMode" -> toggleYSlopeMode = mode;
            case "toggleWaterlogMode" -> toggleWaterlogMode = mode;
            case "stateLockMode" -> stateLockMode = mode;
            case "splitLineMode" -> splitLineMode = mode;
            case "reinforcementMode" -> reinforcementMode = mode;
            case "camoRotationMode" -> camoRotationMode = mode;
            case "prismOffsetMode" -> prismOffsetMode = mode;
            case "frameBackgroundMode" -> frameBackgroundMode = mode;
            case "oneWayWindowMode" -> oneWayWindowMode = mode;
        }
    }

    private static BlockInteractOverlay.Mode overlayMode(String key)
    {
        return switch (key)
        {
            case "toggleYSlopeMode" -> toggleYSlopeMode;
            case "toggleWaterlogMode" -> toggleWaterlogMode;
            case "stateLockMode" -> stateLockMode;
            case "splitLineMode" -> splitLineMode;
            case "reinforcementMode" -> reinforcementMode;
            case "camoRotationMode" -> camoRotationMode;
            case "prismOffsetMode" -> prismOffsetMode;
            case "frameBackgroundMode" -> frameBackgroundMode;
            case "oneWayWindowMode" -> oneWayWindowMode;
            default -> BlockInteractOverlay.Mode.DETAILED;
        };
    }

    private void loadConfig()
    {
        try
        {
            if (CONFIG_FILE.exists())
            {
                JsonObject json = GSON.fromJson(new FileReader(CONFIG_FILE, StandardCharsets.UTF_8), JsonObject.class);
                if (json != null)
                {
                    showGhostBlocks = json.has("showGhostBlocks") ? json.get("showGhostBlocks").getAsBoolean() : true;
                    altGhostRenderer = json.has("altGhostRenderer") ? json.get("altGhostRenderer").getAsBoolean() : false;
                    fancyHitboxes = json.has("fancyHitboxes") ? json.get("fancyHitboxes").getAsBoolean() : true;
                    detailedCulling = json.has("detailedCulling") ? json.get("detailedCulling").getAsBoolean() : true;
                    useDiscreteUVSteps = json.has("useDiscreteUVSteps") ? json.get("useDiscreteUVSteps").getAsBoolean() : false;
                    conTexMode = json.has("conTexMode") ? ConTexMode.valueOf(json.get("conTexMode").getAsString()) : ConTexMode.DETAILED;
                    camoMessageVerbosity = json.has("camoMessageVerbosity") ? CamoMessageVerbosity.valueOf(json.get("camoMessageVerbosity").getAsString()) : CamoMessageVerbosity.DEFAULT;
                    forceAoOnGlowingBlocks = json.has("forceAoOnGlowingBlocks") ? json.get("forceAoOnGlowingBlocks").getAsBoolean() : false;
                    showAllRecipePermutationsInEmi = json.has("showAllRecipePermutationsInEmi") ? json.get("showAllRecipePermutationsInEmi").getAsBoolean() : true;
                    solidFrameMode = json.has("solidFrameMode") ? SolidFrameMode.valueOf(json.get("solidFrameMode").getAsString()) : SolidFrameMode.DEFAULT;
                    showButtonPlateOverlay = json.has("showButtonPlateOverlay") ? json.get("showButtonPlateOverlay").getAsBoolean() : true;
                    showSpecialCubeOverlay = json.has("showSpecialCubeOverlay") ? json.get("showSpecialCubeOverlay").getAsBoolean() : true;

                    readOverlayMode(json, "toggleYSlopeMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "toggleWaterlogMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "stateLockMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "splitLineMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "reinforcementMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "camoRotationMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "prismOffsetMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "frameBackgroundMode", BlockInteractOverlay.Mode.DETAILED);
                    readOverlayMode(json, "oneWayWindowMode", BlockInteractOverlay.Mode.DETAILED);
                }
            }
            else
            {
                saveConfig();
            }
        }
        catch (Exception e)
        {
            FramedBlocks.LOGGER.error("Failed to load client config", e);
        }
    }

    public void saveConfig()
    {
        try
        {
            JsonObject json = new JsonObject();
            json.addProperty("showGhostBlocks", showGhostBlocks);
            json.addProperty("altGhostRenderer", altGhostRenderer);
            json.addProperty("fancyHitboxes", fancyHitboxes);
            json.addProperty("detailedCulling", detailedCulling);
            json.addProperty("useDiscreteUVSteps", useDiscreteUVSteps);
            json.addProperty("conTexMode", conTexMode.name());
            json.addProperty("camoMessageVerbosity", camoMessageVerbosity.name());
            json.addProperty("forceAoOnGlowingBlocks", forceAoOnGlowingBlocks);
            json.addProperty("showAllRecipePermutationsInEmi", showAllRecipePermutationsInEmi);
            json.addProperty("solidFrameMode", solidFrameMode.name());
            json.addProperty("showButtonPlateOverlay", showButtonPlateOverlay);
            json.addProperty("showSpecialCubeOverlay", showSpecialCubeOverlay);

            json.addProperty("toggleYSlopeMode", overlayMode("toggleYSlopeMode").name());
            json.addProperty("toggleWaterlogMode", overlayMode("toggleWaterlogMode").name());
            json.addProperty("stateLockMode", overlayMode("stateLockMode").name());
            json.addProperty("splitLineMode", overlayMode("splitLineMode").name());
            json.addProperty("reinforcementMode", overlayMode("reinforcementMode").name());
            json.addProperty("camoRotationMode", overlayMode("camoRotationMode").name());
            json.addProperty("prismOffsetMode", overlayMode("prismOffsetMode").name());
            json.addProperty("frameBackgroundMode", overlayMode("frameBackgroundMode").name());
            json.addProperty("oneWayWindowMode", overlayMode("oneWayWindowMode").name());

            FileUtils.write(CONFIG_FILE, GSON.toJson(json), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            FramedBlocks.LOGGER.error("Failed to save client config", e);
        }
    }

    public void setShowGhostBlocks(boolean value) { this.showGhostBlocks = value; saveConfig(); }
    public void setAltGhostRenderer(boolean value) { this.altGhostRenderer = value; saveConfig(); }
    public void setFancyHitboxes(boolean value) { this.fancyHitboxes = value; saveConfig(); }
    public void setDetailedCulling(boolean value) { this.detailedCulling = value; saveConfig(); }
    public void setUseDiscreteUVSteps(boolean value) { this.useDiscreteUVSteps = value; saveConfig(); }
    public void setConTexMode(ConTexMode value) { this.conTexMode = value; saveConfig(); }
    public void setCamoMessageVerbosity(CamoMessageVerbosity value) { this.camoMessageVerbosity = value; saveConfig(); }
    public void setForceAoOnGlowingBlocks(boolean value) { this.forceAoOnGlowingBlocks = value; saveConfig(); }
    public void setShowAllRecipePermutationsInEmi(boolean value) { this.showAllRecipePermutationsInEmi = value; saveConfig(); }
    public void setSolidFrameMode(SolidFrameMode value) { this.solidFrameMode = value; saveConfig(); }
    public void setShowButtonPlateOverlay(boolean value) { this.showButtonPlateOverlay = value; saveConfig(); }
    public void setShowSpecialCubeOverlay(boolean value) { this.showSpecialCubeOverlay = value; saveConfig(); }

    // Overlay mode setters removed - overlays pending Fabric port
}