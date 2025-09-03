package org.sequoia.riverreeds;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.sequoia.riverreeds.block.ModBlocks;
import org.sequoia.riverreeds.item.ModItems;

public class Riverreeds implements ModInitializer {
    public static final String MOD_ID = "riverreeds";
    private static final RegistryKey<PlacedFeature> CATTAIL_STEM_PLACED =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(MOD_ID, "cattail_stem"));

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();

        CompostingChanceRegistry.INSTANCE.add(ModItems.CATTAIL, 0.2f);
        FuelRegistry.INSTANCE.add(ModItems.CATTAIL, 5 * 20);

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        BiomeKeys.FOREST,
                        BiomeKeys.SWAMP,
                        BiomeKeys.RIVER),
                GenerationStep.Feature.VEGETAL_DECORATION,
                CATTAIL_STEM_PLACED
        );
    }
}
