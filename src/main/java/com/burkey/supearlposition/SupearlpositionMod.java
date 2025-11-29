package com.burkey.supearlposition;

import com.burkey.supearlposition.block.ModBlocks;
import com.burkey.supearlposition.block.custom.endergy_reactor.TileEndergyReactorControl;
import com.burkey.supearlposition.item.ModItems;
import com.burkey.supearlposition.network.Messages;
import com.burkey.supearlposition.proxy.CommonProxy;
import com.burkey.supearlposition.proxy.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = SupearlpositionMod.MODID,
	name = SupearlpositionMod.NAME,
	version = SupearlpositionMod.VERSION
)
public class SupearlpositionMod {
	public static final String MODID = "supearlposition";
	public static final String NAME = "Supearlposition";
	public static final String VERSION = "1.0";
	
	public static final Logger LOGGER = LogManager.getLogger(MODID);
    @Mod.Instance
    public static SupearlpositionMod instance;
	
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent preinit) {
        proxy.preInit(preinit);
	}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }


    @SidedProxy(serverSide = "com.burkey.supearlposition.proxy.CommonProxy", clientSide = "com.burkey.supearlposition.proxy.ClientProxy")
    public static CommonProxy proxy;
}
