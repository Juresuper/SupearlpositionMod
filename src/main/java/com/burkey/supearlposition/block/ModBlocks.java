package com.burkey.supearlposition.block;

import com.burkey.supearlposition.block.custom.endergy_reactor.EndergyReactorControl;
import com.burkey.supearlposition.block.custom.endergy_reactor.EndergyReactorCoreBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
    public static BlockBase pearlactorPlate = new BlockBase(Material.ANVIL,"pearlactor_plate").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static EndergyReactorControl endergyReactorControlBlock= new EndergyReactorControl(Material.ANVIL,"endergy_reactor_control_block").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockBase endergyReactorCore = new EndergyReactorCoreBlock(Material.GLASS, "endergy_reactor_core").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                pearlactorPlate,
                endergyReactorCore,
                endergyReactorControlBlock
        );

    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                pearlactorPlate.createItemBlock(),
                endergyReactorCore.createItemBlock(),
                endergyReactorControlBlock.createItemBlock()
        );
    }

    public static void registerModels() {
        pearlactorPlate.registerItemModel(Item.getItemFromBlock(pearlactorPlate));
        endergyReactorCore.registerItemModel(Item.getItemFromBlock(endergyReactorCore));
        endergyReactorControlBlock.registerItemModel(Item.getItemFromBlock(endergyReactorControlBlock));
    }
}
