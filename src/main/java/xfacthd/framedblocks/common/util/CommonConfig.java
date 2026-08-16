package xfacthd.framedblocks.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.util.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class CommonConfig
{
    public static final CommonConfig INSTANCE = new CommonConfig();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "framedblocks-common.json");

    public static boolean fireproofBlocks = false;

    public void init()
    {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> loadConfig());
    }

    public boolean fireproofBlocks() { return fireproofBlocks; }

    private void loadConfig()
    {
        try
        {
            if (CONFIG_FILE.exists())
            {
                JsonObject json = GSON.fromJson(new FileReader(CONFIG_FILE, StandardCharsets.UTF_8), JsonObject.class);
                if (json != null)
                {
                    fireproofBlocks = json.has("fireproofBlocks") ? json.get("fireproofBlocks").getAsBoolean() : false;
                }
            }
            else
            {
                saveConfig();
            }
        }
        catch (Exception e)
        {
            FramedBlocks.LOGGER.error("Failed to load common config", e);
        }
    }

    public void saveConfig()
    {
        try
        {
            JsonObject json = new JsonObject();
            json.addProperty("fireproofBlocks", fireproofBlocks);

            FileUtils.write(CONFIG_FILE, GSON.toJson(json), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            FramedBlocks.LOGGER.error("Failed to save common config", e);
        }
    }

    public void setFireproofBlocks(boolean value) { this.fireproofBlocks = value; saveConfig(); }
}