package org.sequoia.riverreeds.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.sequoia.riverreeds.Riverreeds;

public class ModBlocks {
    public static final CropBlock CATTAIL_CROP = register("cattail_crop",
            new CattailCropBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)));

    private static <T extends Block> T register(String path, T block) {
        Registry.register(Registries.BLOCK, Identifier.of(Riverreeds.MOD_ID, path), block);
        return block;
    }

    public static void initialize(){}
}
