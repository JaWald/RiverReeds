package org.sequoia.riverreeds;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item CATTAIL = register(new Item(new FabricItemSettings().maxCount(64)), "cattail");
    public static final Item CATTAIL_SEED = register(new Item(new FabricItemSettings().maxCount(64)), "cattail_seed");
    public static final Item REED = register(new Item(new FabricItemSettings().maxCount(64)), "reed");
    public static final Item DRIED_REED = register(new Item(new FabricItemSettings().maxCount(64)), "dried_reed");


    public static Item register(Item item, String id) {
        // create item identifier
        Identifier itemID = new Identifier(Riverreeds.MOD_ID, id);

        // register item
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ModItems.CATTAIL));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ModItems.CATTAIL_SEED));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ModItems.REED));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ModItems.DRIED_REED));
    }
}