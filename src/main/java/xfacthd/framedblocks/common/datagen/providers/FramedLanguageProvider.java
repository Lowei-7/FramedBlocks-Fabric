package xfacthd.framedblocks.common.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.client.screen.*;
import xfacthd.framedblocks.client.screen.overlay.*;
import xfacthd.framedblocks.client.util.ClientConfig;
import xfacthd.framedblocks.client.util.KeyMappings;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.block.slopeslab.FramedSlopeSlabBlock;
import xfacthd.framedblocks.common.block.special.FramingSawBlock;
import xfacthd.framedblocks.common.block.special.PoweredFramingSawBlock;
import xfacthd.framedblocks.common.blockentity.special.FramedStorageBlockEntity;
import xfacthd.framedblocks.common.compat.jade.JadeCompat;
import xfacthd.framedblocks.common.compat.jei.JeiCompat;
import xfacthd.framedblocks.common.crafting.FramingSawRecipeMatchResult;
import xfacthd.framedblocks.common.data.property.NullableDirection;
import xfacthd.framedblocks.common.item.FramedBlueprintItem;
import xfacthd.framedblocks.common.blockentity.special.FramedChestBlockEntity;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.common.util.*;

public final class FramedLanguageProvider extends FabricLanguageProvider
{
    public FramedLanguageProvider(PackOutput output) { super(output, FramedConstants.MOD_ID, "en_us"); }

    @Override
    protected void generateTranslations(TranslationBuilder translationBuilder)
    {
        addFramedBlockTranslations(translationBuilder);
        addSpecialBlockTranslations(translationBuilder);
        addItemTranslations(translationBuilder);
        addSpecialTranslations(translationBuilder);
        addStatusMessageTranslations(translationBuilder);
        addScreenTranslations(translationBuilder);
        addTooltipTranslations(translationBuilder);
        addOverlayTranslations(translationBuilder);
        addConfigTranslations(translationBuilder);
    }

    private void addFramedBlockTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(FBContent.BLOCK_FRAMED_CUBE.get(), "Framed Cube");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLOPE.get(), "Framed Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CORNER_SLOPE.get(), "Framed Corner Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INNER_CORNER_SLOPE.get(), "Framed Inner Corner Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PRISM_CORNER.get(), "Framed Prism Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INNER_PRISM_CORNER.get(), "Framed Inner Prism Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_THREEWAY_CORNER.get(), "Framed Threeway Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INNER_THREEWAY_CORNER.get(), "Framed Inner Threeway Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLOPE_EDGE.get(), "Framed Slope Edge");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ELEVATED_SLOPE_EDGE.get(), "Framed Elevated Slope Edge");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_SLOPE_EDGE.get(), "Framed Elevated Double Slope Edge");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STACKED_SLOPE_EDGE.get(), "Framed Stacked Slope Edge");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLAB.get(), "Framed Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLAB_EDGE.get(), "Framed Slab Edge");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLAB_CORNER.get(), "Framed Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DIVIDED_SLAB.get(), "Framed Divided Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PANEL.get(), "Framed Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CORNER_PILLAR.get(), "Framed Corner Pillar");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DIVIDED_PANEL_HOR.get(), "Framed Divided Panel (Horizontal)");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DIVIDED_PANEL_VERT.get(), "Framed Divided Panel (Vertical)");
        translationBuilder.add(FBContent.BLOCK_FRAMED_MASONRY_CORNER_SEGMENT.get(), "Framed Masonry Corner Segment");
        translationBuilder.add(FBContent.BLOCK_FRAMED_MASONRY_CORNER.get(), "Framed Masonry Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STAIRS.get(), "Framed Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_THREEWAY_CORNER_PILLAR.get(), "Framed Threeway Corner Pillar");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_THREEWAY_CORNER_PILLAR.get(), "Framed Double Threeway Corner Pillar");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WALL.get(), "Framed Wall");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FENCE.get(), "Framed Fence");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FENCE_GATE.get(), "Framed Fence Gate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOOR.get(), "Framed Door");
        translationBuilder.add(FBContent.BLOCK_FRAMED_IRON_DOOR.get(), "Framed Iron Door");
        translationBuilder.add(FBContent.BLOCK_FRAMED_TRAP_DOOR.get(), "Framed Trapdoor");
        translationBuilder.add(FBContent.BLOCK_FRAMED_IRON_TRAP_DOOR.get(), "Framed Iron Trapdoor");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PRESSURE_PLATE.get(), "Framed Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WATERLOGGABLE_PRESSURE_PLATE.get(), "Framed Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STONE_PRESSURE_PLATE.get(), "Framed Stone Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WATERLOGGABLE_STONE_PRESSURE_PLATE.get(), "Framed Stone Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_OBSIDIAN_PRESSURE_PLATE.get(), "Framed Obsidian Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WATERLOGGABLE_OBSIDIAN_PRESSURE_PLATE.get(), "Framed Obsidian Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_GOLD_PRESSURE_PLATE.get(), "Framed Light Weighted Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WATERLOGGABLE_GOLD_PRESSURE_PLATE.get(), "Framed Light Weighted Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_IRON_PRESSURE_PLATE.get(), "Framed Heavy Weighted Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WATERLOGGABLE_IRON_PRESSURE_PLATE.get(), "Framed Heavy Weighted Pressure Plate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LADDER.get(), "Framed Ladder");
        translationBuilder.add(FBContent.BLOCK_FRAMED_BUTTON.get(), "Framed Button");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STONE_BUTTON.get(), "Framed Stone Button");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LEVER.get(), "Framed Lever");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SIGN.get(), "Framed Sign");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WALL_SIGN.get(), "Framed Sign");
        translationBuilder.add(FBContent.BLOCK_FRAMED_HANGING_SIGN.get(), "Framed Hanging Sign");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WALL_HANGING_SIGN.get(), "Framed Hanging Sign");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_SLAB.get(), "Framed Double Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ADJ_DOUBLE_SLAB.get(), "Framed Adjustable Double Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ADJ_DOUBLE_COPYCAT_SLAB.get(), "Framed Adjustable Double Copycat Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_PANEL.get(), "Framed Double Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ADJ_DOUBLE_PANEL.get(), "Framed Adjustable Double Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ADJ_DOUBLE_COPYCAT_PANEL.get(), "Framed Adjustable Double Copycat Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_SLOPE.get(), "Framed Double Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_CORNER.get(), "Framed Double Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_PRISM_CORNER.get(), "Framed Double Prism Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_THREEWAY_CORNER.get(), "Framed Double Threeway Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_TORCH.get(), "Framed Torch"); //Wall torch name is handled through WallTorchBlock
        translationBuilder.add(FBContent.BLOCK_FRAMED_SOUL_TORCH.get(), "Framed Soul Torch"); //See above
        translationBuilder.add(FBContent.BLOCK_FRAMED_REDSTONE_TORCH.get(), "Framed Redstone Torch"); //See above
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLOOR.get(), "Framed Floor Board");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LATTICE.get(), "Framed Lattice");
        translationBuilder.add(FBContent.BLOCK_FRAMED_THICK_LATTICE.get(), "Framed Thick Lattice");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_STAIRS.get(), "Framed Vertical Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHEST.get(), "Framed Chest");
        translationBuilder.add(FBContent.BLOCK_FRAMED_BARS.get(), "Framed Bars");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PANE.get(), "Framed Pane");
        translationBuilder.add(FBContent.BLOCK_FRAMED_RAIL_SLOPE.get(), "Framed Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_POWERED_RAIL_SLOPE.get(), "Framed Powered Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DETECTOR_RAIL_SLOPE.get(), "Framed Detector Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ACTIVATOR_RAIL_SLOPE.get(), "Framed Activator Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLOWER_POT.get(), "Framed Flower Pot");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PILLAR.get(), "Framed Pillar");
        translationBuilder.add(FBContent.BLOCK_FRAMED_HALF_PILLAR.get(), "Framed Half Pillar");
        translationBuilder.add(FBContent.BLOCK_FRAMED_POST.get(), "Framed Post");
        translationBuilder.add(FBContent.BLOCK_FRAMED_COLLAPSIBLE_BLOCK.get(), "Framed Collapsible Block");
        translationBuilder.add(FBContent.BLOCK_FRAMED_COLLAPSIBLE_COPYCAT_BLOCK.get(), "Framed Collapsible Copycat Block");
        translationBuilder.add(FBContent.BLOCK_FRAMED_HALF_STAIRS.get(), "Framed Half Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DIVIDED_STAIRS.get(), "Framed Divided Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_HALF_STAIRS.get(), "Framed Double Half Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLICED_STAIRS_SLAB.get(), "Framed Sliced Stairs (Slab)");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLICED_STAIRS_PANEL.get(), "Framed Sliced Stairs (Panel)");
        translationBuilder.add(FBContent.BLOCK_FRAMED_BOUNCY_CUBE.get(), "Framed Bouncy Cube");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SECRET_STORAGE.get(), "Framed Secret Storage");
        translationBuilder.add(FBContent.BLOCK_FRAMED_REDSTONE_BLOCK.get(), "Framed Redstone Block");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PRISM.get(), "Framed Prism");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INNER_PRISM.get(), "Framed Inner Prism");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_PRISM.get(), "Framed Double Prism");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLOPED_PRISM.get(), "Framed Sloped Prism");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INNER_SLOPED_PRISM.get(), "Framed Inner Sloped Prism");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_SLOPED_PRISM.get(), "Framed Double Sloped Prism");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLOPE_SLAB.get(), "Framed Slope Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ELEVATED_SLOPE_SLAB.get(), "Framed Elevated Slope Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_COMPOUND_SLOPE_SLAB.get(), "Framed Compound Slope Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_SLOPE_SLAB.get(), "Framed Double Slope Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_SLOPE_SLAB.get(), "Framed Inverted Double Slope Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ELEVATED_DOUBLE_SLOPE_SLAB.get(), "Framed Elevated Double Slope Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STACKED_SLOPE_SLAB.get(), "Framed Stacked Slope Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_SLOPE_SLAB_CORNER.get(), "Framed Flat Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_INNER_SLOPE_SLAB_CORNER.get(), "Framed Flat Inner Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_ELEVATED_SLOPE_SLAB_CORNER.get(), "Framed Flat Elevated Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_ELEVATED_INNER_SLOPE_SLAB_CORNER.get(), "Framed Flat Elevated Inner Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_DOUBLE_SLOPE_SLAB_CORNER.get(), "Framed Flat Double Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_INVERSE_DOUBLE_SLOPE_SLAB_CORNER.get(), "Framed Flat Inverse Double Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_ELEVATED_DOUBLE_SLOPE_SLAB_CORNER.get(), "Framed Flat Elevated Double Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_ELEVATED_INNER_DOUBLE_SLOPE_SLAB_CORNER.get(), "Framed Flat Elevated Inner Double Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_STACKED_SLOPE_SLAB_CORNER.get(), "Framed Flat Stacked Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_STACKED_INNER_SLOPE_SLAB_CORNER.get(), "Framed Flat Stacked Inner Slope Slab Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_HALF_STAIRS.get(), "Framed Vertical Half Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_DIVIDED_STAIRS.get(), "Framed Vertical Divided Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_HALF_STAIRS.get(), "Framed Vertical Double Half Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_SLICED_STAIRS.get(), "Framed Vertical Sliced Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLOPE_PANEL.get(), "Framed Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_SLOPE_PANEL.get(), "Framed Extended Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_COMPOUND_SLOPE_PANEL.get(), "Framed Compound Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_SLOPE_PANEL.get(), "Framed Double Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_SLOPE_PANEL.get(), "Framed Inverted Double Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_SLOPE_PANEL.get(), "Framed Extended Double Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STACKED_SLOPE_PANEL.get(), "Framed Stacked Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_SLOPE_PANEL_CORNER.get(), "Framed Flat Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_INNER_SLOPE_PANEL_CORNER.get(), "Framed Flat Inner Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_EXTENDED_SLOPE_PANEL_CORNER.get(), "Framed Flat Extended Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_EXTENDED_INNER_SLOPE_PANEL_CORNER.get(), "Framed Flat Extended Inner Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_DOUBLE_SLOPE_PANEL_CORNER.get(), "Framed Flat Double Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_INVERSE_DOUBLE_SLOPE_PANEL_CORNER.get(), "Framed Flat Inverse Double Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_EXTENDED_DOUBLE_SLOPE_PANEL_CORNER.get(), "Framed Flat Extended Double Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_EXTENDED_INNER_DOUBLE_SLOPE_PANEL_CORNER.get(), "Framed Flat Extended Inner Double Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_STACKED_SLOPE_PANEL_CORNER.get(), "Framed Flat Stacked Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FLAT_STACKED_INNER_SLOPE_PANEL_CORNER.get(), "Framed Flat Stacked Inner Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SMALL_CORNER_SLOPE_PANEL.get(), "Framed Small Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SMALL_CORNER_SLOPE_PANEL_WALL.get(), "Framed Small Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_CORNER_SLOPE_PANEL.get(), "Framed Large Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_CORNER_SLOPE_PANEL_WALL.get(), "Framed Large Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL.get(), "Framed Small Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SMALL_INNER_CORNER_SLOPE_PANEL_WALL.get(), "Framed Small Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL.get(), "Framed Large Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_INNER_CORNER_SLOPE_PANEL_WALL.get(), "Framed Large Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL.get(), "Framed Small Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SMALL_DOUBLE_CORNER_SLOPE_PANEL_WALL.get(), "Framed Small Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_DOUBLE_CORNER_SLOPE_PANEL.get(), "Framed Large Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_DOUBLE_CORNER_SLOPE_PANEL_WALL.get(), "Framed Large Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_CORNER_SLOPE_PANEL.get(), "Framed Inverse Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_INVERSE_DOUBLE_CORNER_SLOPE_PANEL_WALL.get(), "Framed Inverse Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_CORNER_SLOPE_PANEL.get(), "Framed Extended Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_CORNER_SLOPE_PANEL_WALL.get(), "Framed Extended Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_INNER_CORNER_SLOPE_PANEL.get(), "Framed Extended Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_INNER_CORNER_SLOPE_PANEL_WALL.get(), "Framed Extended Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_CORNER_SLOPE_PANEL.get(), "Framed Extended Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_DOUBLE_CORNER_SLOPE_PANEL_WALL.get(), "Framed Extended Double Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_INNER_DOUBLE_CORNER_SLOPE_PANEL.get(), "Framed Extended Inner Double Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_EXTENDED_INNER_DOUBLE_CORNER_SLOPE_PANEL_WALL.get(), "Framed Extended Inner Double Slope Panel Corner");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_PANEL.get(), "Framed Stacked Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STACKED_CORNER_SLOPE_PANEL_WALL.get(), "Framed Stacked Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL.get(), "Framed Stacked Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_STACKED_INNER_CORNER_SLOPE_PANEL_WALL.get(), "Framed Stacked Inner Corner Slope Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_STAIRS.get(), "Framed Double Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_STAIRS.get(), "Framed Vertical Double Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_WALL_BOARD.get(), "Framed Wall Board");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CORNER_STRIP.get(), "Framed Corner Strip");
        translationBuilder.add(FBContent.BLOCK_FRAMED_GLOWING_CUBE.get(), "Framed Glowing Cube");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PYRAMID.get(), "Framed Pyramid");
        translationBuilder.add(FBContent.BLOCK_FRAMED_PYRAMID_SLAB.get(), "Framed Pyramid Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_HORIZONTAL_PANE.get(), "Framed Horizontal Pane");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_BUTTON.get(), "Large Framed Button");
        translationBuilder.add(FBContent.BLOCK_FRAMED_LARGE_STONE_BUTTON.get(), "Large Framed Stone Button");
        translationBuilder.add(FBContent.BLOCK_FRAMED_TARGET.get(), "Framed Target");
        translationBuilder.add(FBContent.BLOCK_FRAMED_GATE.get(), "Framed Gate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_IRON_GATE.get(), "Framed Iron Gate");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ITEM_FRAME.get(), "Framed Item Frame");
        translationBuilder.add(FBContent.BLOCK_FRAMED_GLOWING_ITEM_FRAME.get(), "Framed Glow Item Frame");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_RAIL.get(), "Framed Fancy Rail");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_POWERED_RAIL.get(), "Framed Fancy Powered Rail");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL.get(), "Framed Fancy Detector Rail");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL.get(), "Framed Fancy Activator Rail");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_RAIL_SLOPE.get(), "Framed Fancy Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_POWERED_Rn_SLOPE.get(), "Framed Fancy Powered Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_DETECTOR_RAIL_SLOPE.get(), "Framed Fancy Detector Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_FANCY_ACTIVATOR_RAIL_SLOPE.get(), "Framed Fancy Activator Rail Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_HALF_SLOPE.get(), "Framed Half Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_HALF_SLOPE.get(), "Framed Half Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DIVIDED_SLOPE.get(), "Framed Divided Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_DOUBLE_HALF_SLOPE.get(), "Framed Double Half Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_DOUBLE_HALF_SLOPE.get(), "Framed Double Half Slope");
        translationBuilder.add(FBContent.BLOCK_FRAMED_SLOPED_STAIRS.get(), "Framed Sloped Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_VERTICAL_SLOPED_STAIRS.get(), "Framed Vertical Sloped Stairs");
        translationBuilder.add(FBContent.BLOCK_FRAMED_MINI_CUBE.get(), "Framed Mini Cube");
        translationBuilder.add(FBContent.BLOCK_FRAMED_ONE_WAY_WINDOW.get(), "Framed One-Way Window");
        translationBuilder.add(FBContent.BLOCK_FRAMED_BOOKSHELF.get(), "Framed Bookshelf");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHISELED_BOOKSHELF.get(), "Framed Chiseled Bookshelf");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CENTERED_SLAB.get(), "Framed Centered Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CENTERED_PANEL.get(), "Framed Centered Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHECKERED_CUBE_SEGMENT.get(), "Framed Checkered Cube Segment");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHECKERED_CUBE.get(), "Framed Checkered Cube");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHECKERED_SLAB_SEGMENT.get(), "Framed Checkered Slab Segment");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHECKERED_SLAB.get(), "Framed Checkered Slab");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHECKERED_PANEL_SEGMENT.get(), "Framed Checkered Panel Segment");
        translationBuilder.add(FBContent.BLOCK_FRAMED_CHECKERED_PANEL.get(), "Framed Checkered Panel");
        translationBuilder.add(FBContent.BLOCK_FRAMED_TUBE.get(), "Framed Tube");
    }

    private void addSpecialBlockTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(FBContent.BLOCK_FRAMING_SAW.get(), "Framing Saw");
        translationBuilder.add(FBContent.BLOCK_POWERED_FRAMING_SAW.get(), "Powered Framing Saw");
    }

    private void addItemTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(FBContent.ITEM_FRAMED_HAMMER.get(), "Framed Hammer");
        translationBuilder.add(FBContent.ITEM_FRAMED_WRENCH.get(), "Framed Wrench");
        translationBuilder.add(FBContent.ITEM_FRAMED_BLUEPRINT.get(), "Framed Blueprint");
        translationBuilder.add(FBContent.ITEM_FRAMED_KEY.get(), "Framed Key");
        translationBuilder.add(FBContent.ITEM_FRAMED_SCREWDRIVER.get(), "Framed Screwdriver");
        translationBuilder.add(FBContent.ITEM_FRAMED_REINFORCEMENT.get(), "Framed Reinforcement");
    }

    private void addSpecialTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(KeyMappings.KEY_CATEGORY, "FramedBlocks");
        translationBuilder.add(KeyMappings.KEYMAPPING_UPDATE_CULLING.get().getName(), "Update culling cache");

        translationBuilder.add(FBContent.MAIN_TAB.get().getDisplayName(), "FramedBlocks");

        translationBuilder.add(JeiCompat.MSG_INVALID_RECIPE, "Invalid recipe");
        translationBuilder.add(JeiCompat.MSG_TRANSFER_NOT_IMPLEMENTED, "Transfer not implemented, no items will be transferred");

        translationBuilder.add(JadeCompat.configTranslation(JadeCompat.ID_ITEM_FRAME), "Framed Item Frame");
    }

    private void addStatusMessageTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(FramedBlockEntity.MSG_BLACKLISTED, "This block is disallowed as a camo!");
        translationBuilder.add(FramedBlockEntity.MSG_BLOCK_ENTITY, "Blocks with BlockEntities cannot be inserted into framed blocks!");
        translationBuilder.add(FramedBlockEntity.MSG_NON_SOLID, "Untagged non-solid blocks cannot be inserted into framed blocks!");

        translationBuilder.add(IFramedBlock.LOCK_MESSAGE, "The state of this block is now %s");
    }

    private void addScreenTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(FramedChestBlockEntity.TITLE, "Framed Chest");
        translationBuilder.add(FramedStorageBlockEntity.TITLE, "Framed Secret Storage");

        translationBuilder.add(FramedSignScreen.TITLE, "Edit sign");

        translationBuilder.add(FramingSawBlock.SAW_MENU_TITLE, "Framing Saw");
        translationBuilder.add(PoweredFramingSawBlock.POWERED_SAW_MENU_TITLE, "Powered Framing Saw");
        translationBuilder.add(FramingSawScreen.TOOLTIP_MATERIAL, "Material value: %s");
        translationBuilder.add(FramingSawScreen.TOOLTIP_LOOSE_ADDITIVE, "Item was crafted with additive ingredients, these will be lost");
        translationBuilder.add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_ITEM, "Have %s, but need %s");
        translationBuilder.add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_TAG, "Have %s, but need any %s");
        translationBuilder.add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_ITEM_COUNT, "Have %s item(s), but need at least %s item(s)");
        translationBuilder.add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_MATERIAL_COUNT, "Have %s material, but need at least %s material");
        translationBuilder.add(FramingSawScreen.TOOLTIP_OUTPUT_COUNT, "Result size: %s, max size: %s");
        translationBuilder.add(FramingSawScreen.TOOLTIP_HAVE_ITEM_NONE, "none");
        translationBuilder.add(FramingSawScreen.TOOLTIP_PRESS_TO_SHOW, "Press [%s] to show all possible items");
        translationBuilder.add(PoweredFramingSawScreen.TITLE_TARGETBLOCK, "Target:");
        translationBuilder.add(PoweredFramingSawScreen.MSG_STATUS, "Status: ");
        translationBuilder.add(PoweredFramingSawScreen.MSG_STATUS_NO_RECIPE, "No recipe");
        translationBuilder.add(PoweredFramingSawScreen.MSG_STATUS_NO_MATCH, "Recipe doesn't match");
        translationBuilder.add(PoweredFramingSawScreen.MSG_STATUS_READY, "Ready");
        translationBuilder.add(PoweredFramingSawScreen.TOOLTIP_STATUS_NO_RECIPE, "No recipe selected, click the target slot with any framed block to select a recipe");
        translationBuilder.add(PoweredFramingSawScreen.TOOLTIP_ENERGY, "%s / %s FE");
        translationBuilder.add(FramingSawRecipeMatchResult.SUCCESS.translation(), "Craftable");
        translationBuilder.add(FramingSawRecipeMatchResult.MATERIAL_VALUE.translation(), "Insufficient input material available");
        translationBuilder.add(FramingSawRecipeMatchResult.MATERIAL_LCM.translation(), "Too few input items to evenly convert to this output");
        translationBuilder.add(FramingSawRecipeMatchResult.OUTPUT_SIZE.translation(), "Result count exceeds maximum result stack size");
        translationBuilder.add(FramingSawRecipeMatchResult.MISSING_ADDITIVE_0.translation(), "Missing additive ingredient in the first slot");
        translationBuilder.add(FramingSawRecipeMatchResult.MISSING_ADDITIVE_1.translation(), "Missing additive ingredient in the second slot");
        translationBuilder.add(FramingSawRecipeMatchResult.MISSING_ADDITIVE_2.translation(), "Missing additive ingredient in the third slot");
        translationBuilder.add(FramingSawRecipeMatchResult.UNEXPECTED_ADDITIVE_0.translation(), "Unexpected additive ingredient present in the first slot");
        translationBuilder.add(FramingSawRecipeMatchResult.UNEXPECTED_ADDITIVE_1.translation(), "Unexpected additive ingredient present in the second slot");
        translationBuilder.add(FramingSawRecipeMatchResult.UNEXPECTED_ADDITIVE_2.translation(), "Unexpected additive ingredient present in the third slot");
        translationBuilder.add(FramingSawRecipeMatchResult.INCORRECT_ADDITIVE_0.translation(), "Incorrect additive ingredient present in the first slot");
        translationBuilder.add(FramingSawRecipeMatchResult.INCORRECT_ADDITIVE_1.translation(), "Incorrect additive ingredient present in the second slot");
        translationBuilder.add(FramingSawRecipeMatchResult.INCORRECT_ADDITIVE_2.translation(), "Incorrect additive ingredient present in the third slot");
        translationBuilder.add(FramingSawRecipeMatchResult.INSUFFICIENT_ADDITIVE_0.translation(), "Insufficient amount of additive ingredient present in the first slot");
        translationBuilder.add(FramingSawRecipeMatchResult.INSUFFICIENT_ADDITIVE_1.translation(), "Insufficient amount of additive ingredient present in the second slot");
        translationBuilder.add(FramingSawRecipeMatchResult.INSUFFICIENT_ADDITIVE_2.translation(), "Insufficient amount of additive ingredient present in the third slot");
    }

    private void addTooltipTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(FramedBlueprintItem.CONTAINED_BLOCK, "Contained Block: %s");
        translationBuilder.add(FramedBlueprintItem.CAMO_BLOCK, "Camo Block: %s");
        translationBuilder.add(FramedBlueprintItem.IS_ILLUMINATED, "Illuminated: %s");
        translationBuilder.add(FramedBlueprintItem.IS_INTANGIBLE, "Intangible: %s");
        translationBuilder.add(FramedBlueprintItem.IS_REINFORCED, "Reinforced: %s");
        translationBuilder.add(FramedBlueprintItem.MISSING_MATERIALS, "[Framed Blueprint] Missing required materials:");
        translationBuilder.add(FramedBlueprintItem.BLOCK_NONE, "None");
        translationBuilder.add(FramedBlueprintItem.BLOCK_INVALID, "Invalid");
        translationBuilder.add(FramedBlueprintItem.FALSE, "false");
        translationBuilder.add(FramedBlueprintItem.TRUE, "true");
        translationBuilder.add(FramedBlueprintItem.CANT_COPY, "[Framed Blueprint] This block can currently not be copied!");
        translationBuilder.add(FramedBlueprintItem.CANT_PLACE_FLUID_CAMO, "[Framed Blueprint] Copying blocks with fluid camos is currently not possible!");
        translationBuilder.add(FramedSlopeSlabBlock.PLACE_UPSIDE_DOWN, "Hold sneak key to place upside down");
    }

    private void addOverlayTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(StateLockOverlay.LOCK_MESSAGE, "State %s");
        translationBuilder.add(IFramedBlock.STATE_LOCKED, "locked");
        translationBuilder.add(IFramedBlock.STATE_UNLOCKED, "unlocked");

        translationBuilder.add(ToggleWaterloggableOverlay.MSG_IS_WATERLOGGABLE, "Block is waterloggable.");
        translationBuilder.add(ToggleWaterloggableOverlay.MSG_IS_NOT_WATERLOGGABLE, "Block is not waterloggable.");
        translationBuilder.add(ToggleWaterloggableOverlay.MSG_MAKE_WATERLOGGABLE, "Hit with a Framed Hammer to make waterloggable");
        translationBuilder.add(ToggleWaterloggableOverlay.MSG_MAKE_NOT_WATERLOGGABLE, "Hit with a Framed Hammer to make not waterloggable");

        translationBuilder.add(ToggleYSlopeOverlay.SLOPE_MESSAGE, "Block uses %s faces for vertical sloped faces.");
        translationBuilder.add(ToggleYSlopeOverlay.TOGGLE_MESSAGE, "Hit with a Framed Wrench to switch to %s faces");
        translationBuilder.add(ToggleYSlopeOverlay.SLOPE_HOR, "horizontal");
        translationBuilder.add(ToggleYSlopeOverlay.SLOPE_VERT, "vertical");

        translationBuilder.add(ReinforcementOverlay.REINFORCE_MESSAGE, "Block is %s.");
        translationBuilder.add(ReinforcementOverlay.STATE_NOT_REINFORCED, "not reinforced");
        translationBuilder.add(ReinforcementOverlay.STATE_REINFORCED, "reinforced");

        translationBuilder.add(PrismOffsetOverlay.PRISM_OFFSET_FALSE, "Triangle texture is not offset.");
        translationBuilder.add(PrismOffsetOverlay.PRISM_OFFSET_TRUE, "Triangle texture is offset by half a block.");
        translationBuilder.add(PrismOffsetOverlay.MSG_SWITCH_OFFSET, "Hit with a Framed Hammer to toggle the offset");

        translationBuilder.add(SplitLineOverlay.SPLIT_LINE_FALSE, "Split-line of the deformed face runs along the steep diagonal.");
        translationBuilder.add(SplitLineOverlay.SPLIT_LINE_TRUE, "Split-line of the deformed face runs along the shallow diagonal.");
        translationBuilder.add(SplitLineOverlay.MSG_SWITCH_SPLIT_LINE, "Hit with a Framed Wrench to switch the orientation of the split-line");

        translationBuilder.add(OneWayWindowOverlay.LINE_CURR_FACE, "Current see-through side: %s");
        translationBuilder.add(OneWayWindowOverlay.LINE_SET_FACE, "Hit with a Framed Wrench to set the see-through side to %s");
        translationBuilder.add(OneWayWindowOverlay.LINE_CLEAR_FACE, "Hit with a Framed Wrench while crouching to clear see-through side");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_LINES[NullableDirection.NONE.ordinal()], "None");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_LINES[NullableDirection.DOWN.ordinal()], "Down");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_LINES[NullableDirection.UP.ordinal()], "Up");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_LINES[NullableDirection.NORTH.ordinal()], "North");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_LINES[NullableDirection.SOUTH.ordinal()], "South");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_LINES[NullableDirection.WEST.ordinal()], "West");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_LINES[NullableDirection.EAST.ordinal()], "East");
        translationBuilder.add(OneWayWindowOverlay.DIR_VALUE_LINES[Direction.DOWN.ordinal()], "Down");
        translationBuilder.add(OneWayWindowOverlay.DIR_VALUE_LINES[Direction.UP.ordinal()], "Up");
        translationBuilder.add(OneWayWindowOverlay.DIR_VALUE_LINES[Direction.NORTH.ordinal()], "North");
        translationBuilder.add(OneWayWindowOverlay.DIR_VALUE_LINES[Direction.SOUTH.ordinal()], "South");
        translationBuilder.add(OneWayWindowOverlay.DIR_VALUE_LINES[Direction.WEST.ordinal()], "West");
        translationBuilder.add(OneWayWindowOverlay.DIR_VALUE_LINES[Direction.EAST.ordinal()], "East");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_ABBRS[NullableDirection.NONE.ordinal()], "-");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_ABBRS[NullableDirection.DOWN.ordinal()], "D");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_ABBRS[NullableDirection.UP.ordinal()], "U");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_ABBRS[NullableDirection.NORTH.ordinal()], "N");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_ABBRS[NullableDirection.SOUTH.ordinal()], "S");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_ABBRS[NullableDirection.WEST.ordinal()], "W");
        translationBuilder.add(OneWayWindowOverlay.FACE_VALUE_ABBRS[NullableDirection.EAST.ordinal()], "E");

        translationBuilder.add(FrameBackgroundOverlay.LINE_USE_CAMO_BG, "Framed Item Frame uses the camo as background");
        translationBuilder.add(FrameBackgroundOverlay.LINE_USE_LEATHER_BG, "Framed Item Frame uses leather as background");
        translationBuilder.add(FrameBackgroundOverlay.LINE_SET_CAMO_BG, "Hit with a Framed Hammer to use the camo as background");
        translationBuilder.add(FrameBackgroundOverlay.LINE_SET_LEATHER_BG, "Hit with a Framed Hammer to use leather as background");

        translationBuilder.add(CamoRotationOverlay.ROTATEABLE_FALSE, "The targetted camo cannot be rotated");
        translationBuilder.add(CamoRotationOverlay.ROTATEABLE_TRUE, "The targetted camo can be rotated");
    }

    private void addConfigTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(CommonConfig.TRANSLATION_FIREPROOF_BLOCKS, "Fireproof blocks");

        translationBuilder.add(ServerConfig.TRANSLATION_ALLOW_BLOCK_ENTITIES, "Allow BlockEntities");
        translationBuilder.add(ServerConfig.TRANSLATION_ENABLE_INTANGIBILITY, "Enable intangibility feature");
        translationBuilder.add(ServerConfig.TRANSLATION_INTANGIBLE_MARKER, "Intangibility marker item");
        translationBuilder.add(ServerConfig.TRANSLATION_ONE_WAY_WINDOW_OWNABLE, "One-Way Window ownability");
        translationBuilder.add(ServerConfig.TRANSLATION_CONSUME_CAMO_ITEM, "Consume camo item");
        translationBuilder.add(ServerConfig.TRANSLATION_GLOWSTONE_LIGHT_LEVEL, "Glowstone Light Level");

        translationBuilder.add(ClientConfig.TRANSLATION_SHOW_GHOST_BLOCKS, "Show ghost blocks");
        translationBuilder.add(ClientConfig.TRANSLATION_ALT_GHOST_RENDERER, "Use alternative placement preview renderer");
        translationBuilder.add(ClientConfig.TRANSLATION_FANCY_HITBOXES, "Fancy hitboxes");
        translationBuilder.add(ClientConfig.TRANSLATION_DETAILED_CULLING, "Detailed culling");
        translationBuilder.add(ClientConfig.TRANSLATION_USE_DISCRETE_UV_STEPS, "Use discrete UV steps");
        translationBuilder.add(ClientConfig.TRANSLATION_CON_TEX_MODE, "Connected textures mode");
        translationBuilder.add(ClientConfig.TRANSLATION_CAMO_MESSAGE_VERBOSITY, "Disallowed camo message verbosity");
        translationBuilder.add(ClientConfig.TRANSLATION_FORCE_AO_ON_GLOWING_BLOCKS, "Force ambient occlusion on glowing framed blocks");
        translationBuilder.add(ClientConfig.TRANSLATION_SHOW_ALL_RECIPE_PERMUTATIONS_IN_EMI, "Show all Framing Saw recipe permutations in EMI");
        translationBuilder.add(ClientConfig.TRANSLATION_SOLID_FRAME_MODE, "Solid frame mode");
        translationBuilder.add(ClientConfig.TRANSLATION_SHOW_BUTTON_PLATE_OVERLAY, "Show button and pressure plate type overlay");
        translationBuilder.add(ClientConfig.TRANSLATION_SHOW_SPECIAL_CUBE_OVERLAY, "Show special cube type overlay");
        translationBuilder.add(ClientConfig.TRANSLATION_STATE_LOCK_MODE, "State lock overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_TOGGLE_WATERLOG_MODE, "Toggle waterloggable overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_TOGGLE_Y_SLOPE_MODE, "Toggle Y slope overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_REINFORCEMENT_MODE, "Reinforcement overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_PRISM_OFFSET_MODE, "Prism offset overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_SPLIT_LINES_MODE, "Collapsible block split lines overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_ONE_WAY_WINDOW_MODE, "One-Way Window overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_FRAME_BACKGROUND_MODE, "Item Frame Background overlay: Display mode");
        translationBuilder.add(ClientConfig.TRANSLATION_CAMO_ROTATION_MODE, "Camo Rotation overlay: Display mode");
    }

    private void add(Component key, String value, TranslationBuilder translationBuilder)
    {
        ComponentContents contents = key.getContents();
        if (contents instanceof TranslatableContents translatable)
        {
            translationBuilder.add(translatable.getKey(), value);
        }
        else
        {
            translationBuilder.add(key.getString(), value);
        }
    }
}
