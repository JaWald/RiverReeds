package org.sequoia.riverreeds.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.HayBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.sequoia.riverreeds.Riverreeds;

import static org.sequoia.riverreeds.item.ModItems.ITEM_GROUP_KEY;

public class ModBlocks {
    public static final CropBlock CATTAIL_STEM = registerWithoutItem("cattail_stem",
            new CattailStemBlock(FabricBlockSettings.create()
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)));

    public static final CropBlock CATTAIL_CROP = registerWithoutItem("cattail_crop",
            new CattailCropBlock(FabricBlockSettings.create()
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)));

    public static final HayBlock THATCH_BLOCK = register("thatch_block",
            new ThatchBlock(FabricBlockSettings.create()
                    .nonOpaque()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.GRASS)));

    public static final ThatchSlab THATCH_SLAB = register("thatch_slab",
            new ThatchSlab(FabricBlockSettings.create()
                    .nonOpaque()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.GRASS)));

    public static final ThatchStairs THATCH_STAIRS = register("thatch_stairs",
            new ThatchStairs(THATCH_BLOCK.getDefaultState(), FabricBlockSettings
                    .create()
                    .nonOpaque()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.GRASS)));

    private static <T extends Block> T register(String path, T block) {
        Registry.register(Registries.BLOCK, Identifier.of(Riverreeds.MOD_ID, path), block);
        Registry.register(Registries.ITEM, Identifier.of(Riverreeds.MOD_ID, path), new BlockItem(block, new Item.Settings()));
        return block;
    }

    private static <T extends Block> T registerWithoutItem(String path, T block) {
        Registry.register(Registries.BLOCK, Identifier.of(Riverreeds.MOD_ID, path), block);
        return block;
    }

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register((entries) -> {
            entries.add(THATCH_BLOCK.asItem());
            entries.add(THATCH_SLAB.asItem());
            entries.add(THATCH_STAIRS.asItem());
        });
    }
}
