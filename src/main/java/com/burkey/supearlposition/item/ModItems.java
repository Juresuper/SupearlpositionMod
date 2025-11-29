package com.burkey.supearlposition.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemBase enrichedPearlItem = new EnrichedPearlItem("enriched_pearl").setCreativeTab(CreativeTabs.MATERIALS);

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                enrichedPearlItem
        );
    }

    public static void registerModels() {
        enrichedPearlItem.registerItemModel();
    }


}
