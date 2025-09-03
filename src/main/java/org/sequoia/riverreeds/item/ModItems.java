package org.sequoia.riverreeds.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.sequoia.riverreeds.Riverreeds;
import org.sequoia.riverreeds.block.ModBlocks;

public class ModItems {
    public static final Item CATTAIL = register(new Item(new FabricItemSettings().maxCount(64)), "cattail");
    public static final Item REED = register(new Item(new FabricItemSettings().maxCount(64)), "reed");
    public static final Item DRIED_REED = register(new Item(new FabricItemSettings().maxCount(64)), "dried_reed");
    public static final Item CATTAIL_SEED = register(new AliasedBlockItem(ModBlocks.CATTAIL_STEM, new FabricItemSettings().maxCount(64)), "cattail_seed");

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier(Riverreeds.MOD_ID, "item_group"));
    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.CATTAIL))
            .displayName(Text.translatable("itemGroup.riverreeds"))
            .build();

    public static Item register(Item item, String id) {
        Identifier itemID = new Identifier(Riverreeds.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static void initialize(){
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register((entries) -> {
            entries.add(CATTAIL);
            entries.add(REED);
            entries.add(DRIED_REED);
            entries.add(CATTAIL_SEED);
        });
    }
}