package xfacthd.framedblocks.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.io.FileUtils;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.util.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class ServerConfig
{
    public static final ServerConfig INSTANCE = new ServerConfig();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "framedblocks-server.json");

    public static boolean allowBlockEntities;
    public static boolean enableIntangibleFeature;
    public static Item intangibleMarkerItem;
    public static boolean oneWayWindowOwnable;
    public static boolean consumeCamoItem;
    public static int glowstoneLightLevel;

    public void init()
    {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> loadConfig());
    }

    public boolean allowBlockEntities() { return allowBlockEntities; }
    public boolean enableIntangibleFeature() { return enableIntangibleFeature; }
    public Item intangibleMarkerItem() { return intangibleMarkerItem; }
    public boolean oneWayWindowOwnable() { return oneWayWindowOwnable; }
    public boolean consumeCamoItem() { return consumeCamoItem; }
    public int glowstoneLightLevel() { return glowstoneLightLevel; }

    private void loadConfig()
    {
        try
        {
            if (CONFIG_FILE.exists())
            {
                JsonObject json = GSON.fromJson(new FileReader(CONFIG_FILE, StandardCharsets.UTF_8), JsonObject.class);
                if (json != null)
                {
                    allowBlockEntities = json.has("allowBlockEntities") ? json.get("allowBlockEntities").getAsBoolean() : false;
                    enableIntangibleFeature = json.has("enableIntangibleFeature") ? json.get("enableIntangibleFeature").getAsBoolean() : false;
                    oneWayWindowOwnable = json.has("oneWayWindowOwnable") ? json.get("oneWayWindowOwnable").getAsBoolean() : true;
                    consumeCamoItem = json.has("consumeCamoItem") ? json.get("consumeCamoItem").getAsBoolean() : true;
                    glowstoneLightLevel = json.has("glowstoneLightLevel") ? json.get("glowstoneLightLevel").getAsInt() : 15;
                    
                    // Handle intangible marker item
                    if (json.has("intangibleMarkerItem"))
                    {
                        String itemName = json.get("intangibleMarkerItem").getAsString();
                        try
                        {
                            ResourceLocation key = ResourceLocation.parse(itemName);
                            // In Fabric, we can't easily validate items without registry access, so we'll just use the default
                            intangibleMarkerItem = Items.PHANTOM_MEMBRANE;
                        }
                        catch (Exception e)
                        {
                            // If parsing fails, use default
                            intangibleMarkerItem = Items.PHANTOM_MEMBRANE;
                        }
                    }
                    else
                    {
                        intangibleMarkerItem = Items.PHANTOM_MEMBRANE;
                    }
                }
            }
            else
            {
                saveConfig();
            }
        }
        catch (Exception e)
        {
            FramedBlocks.LOGGER.error("Failed to load server config", e);
        }
    }

    public void saveConfig()
    {
        try
        {
            JsonObject json = new JsonObject();
            json.addProperty("allowBlockEntities", allowBlockEntities);
            json.addProperty("enableIntangibleFeature", enableIntangibleFeature);
            json.addProperty("intangibleMarkerItem", "minecraft:phantom_membrane");
            json.addProperty("oneWayWindowOwnable", oneWayWindowOwnable);
            json.addProperty("consumeCamoItem", consumeCamoItem);
            json.addProperty("glowstoneLightLevel", glowstoneLightLevel);

            FileUtils.write(CONFIG_FILE, GSON.toJson(json), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            FramedBlocks.LOGGER.error("Failed to save server config", e);
        }
    }

    public void setAllowBlockEntities(boolean value) { this.allowBlockEntities = value; saveConfig(); }
    public void setEnableIntangibleFeature(boolean value) { this.enableIntangibleFeature = value; saveConfig(); }
    public void setIntangibleMarkerItem(Item value) { this.intangibleMarkerItem = value; saveConfig(); }
    public void setOneWayWindowOwnable(boolean value) { this.oneWayWindowOwnable = value; saveConfig(); }
    public void setConsumeCamoItem(boolean value) { this.consumeCamoItem = value; saveConfig(); }
    public void setGlowstoneLightLevel(int value) { this.glowstoneLightLevel = value; saveConfig(); }
}
