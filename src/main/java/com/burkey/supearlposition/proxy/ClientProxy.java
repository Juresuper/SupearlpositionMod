package com.burkey.supearlposition.proxy;

import com.burkey.supearlposition.ModEntities;
import com.burkey.supearlposition.SupearlpositionMod;
import com.burkey.supearlposition.block.ModBlocks;
import com.burkey.supearlposition.item.ItemBase;
import com.burkey.supearlposition.item.ModItems;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy{
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(SupearlpositionMod.MODID + ":" + id, "inventory"));
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.registerModels();
        ModBlocks.registerModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ModEntities.initModels();
    }

    @Override
    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule){
        return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
    }
    @Override
    public EntityPlayer getClientPlayer(){ return Minecraft.getMinecraft().player; }


}
