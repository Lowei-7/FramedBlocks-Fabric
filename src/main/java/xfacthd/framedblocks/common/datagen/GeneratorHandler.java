package xfacthd.framedblocks.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.common.datagen.providers.*;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorStep;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@net.fabricmc.api.ModInitializer
public final class GeneratorHandler
{
    public static void onInitializeDataGenerator(FabricDataGenerator gen)
    {
        PackOutput output = gen.getOutput();
        ExistingFileHelper fileHelper = gen.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = gen.getHolderLookupProvider();

        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.CLIENT, new FramedSpriteSourceProvider(output, fileHelper));
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.CLIENT, new FramedBlockStateProvider(output, fileHelper));
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.CLIENT, new FramedItemModelProvider(output, fileHelper));
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.SERVER, new FramedLootTableProvider(output));
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.SERVER, new FramedRecipeProvider(output));
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.SERVER, new FramingSawRecipeProvider(output));
        BlockTagsProvider tagProvider = new FramedBlockTagProvider(output, lookupProvider, fileHelper);
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.SERVER, tagProvider);
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.SERVER, new FramedItemTagProvider(output, lookupProvider, tagProvider.contentsGetter(), fileHelper));
        gen.addProvider(net.fabricmc.fabric.api.datagen.v1.FabricDataOutput.Preset.CLIENT, new FramedLanguageProvider(output));
    }
    {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeClient(), new FramedSpriteSourceProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new FramedBlockStateProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new FramedItemModelProvider(output, fileHelper));
        gen.addProvider(event.includeServer(), new FramedLootTableProvider(output));
        gen.addProvider(event.includeServer(), new FramedRecipeProvider(output));
        gen.addProvider(event.includeServer(), new FramingSawRecipeProvider(output));
        BlockTagsProvider tagProvider = new FramedBlockTagProvider(output, lookupProvider, fileHelper);
        gen.addProvider(event.includeServer(), tagProvider);
        gen.addProvider(event.includeServer(), new FramedItemTagProvider(output, lookupProvider, tagProvider.contentsGetter(), fileHelper));
        gen.addProvider(event.includeClient(), new FramedLanguageProvider(output));
    }



    private GeneratorHandler() { }
}