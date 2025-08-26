package org.sequoia.riverreeds;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import org.sequoia.riverreeds.block.ModBlocks;
import org.sequoia.riverreeds.item.ModItems;

public class Riverreeds implements ModInitializer {
    public static final String MOD_ID = "riverreeds";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();

        CompostingChanceRegistry.INSTANCE.add(ModItems.CATTAIL, 0.2f);
        FuelRegistry.INSTANCE.add(ModItems.CATTAIL, 5 * 20);
    }
}
