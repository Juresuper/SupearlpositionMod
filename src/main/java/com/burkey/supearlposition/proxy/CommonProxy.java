package com.burkey.supearlposition.proxy;

import com.burkey.supearlposition.ModEntities;
import com.burkey.supearlposition.SupearlpositionMod;
import com.burkey.supearlposition.block.ModBlocks;
import com.burkey.supearlposition.block.custom.endergy_reactor.TileEndergyReactorControl;
import com.burkey.supearlposition.block.custom.endergy_reactor.TileEndergyReactorCore;
import com.burkey.supearlposition.block.custom.tools.SoundsTools;
import com.burkey.supearlposition.event.EnrichedPearlEvent;
import com.burkey.supearlposition.item.ModItems;
import com.burkey.supearlposition.network.Messages;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Messages.registerMessages("supearlposition");
        ModEntities.init();
    }
    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SupearlpositionMod.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(EnrichedPearlEvent.instance);
        SoundsTools.registerSounds();
    }
    public void postInit(FMLPostInitializationEvent e) {

    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        ModItems.register(event.getRegistry());
        ModBlocks.registerItemBlocks(event.getRegistry());
    }
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        ModBlocks.register(event.getRegistry());
        GameRegistry.registerTileEntity(TileEndergyReactorControl.class,SupearlpositionMod.MODID + "_endergy_reactor_control");
        GameRegistry.registerTileEntity(TileEndergyReactorCore.class,SupearlpositionMod.MODID + "_endergy_reactor_core");

    }

    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
        throw new IllegalStateException("This should only be called from the client side");
    }
    public EntityPlayer getClientPlayer() {
        throw new IllegalStateException("This should only be called from the client side");
    }
}
