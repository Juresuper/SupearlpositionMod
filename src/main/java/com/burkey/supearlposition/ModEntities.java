package com.burkey.supearlposition;

import com.burkey.supearlposition.item.EnrichedPearlItem;
import com.burkey.supearlposition.item.ModItems;
import com.burkey.supearlposition.projectile.EnrichedPearlEntity;
import com.burkey.supearlposition.projectile.MeltdownBlobEntity;
import com.burkey.supearlposition.renderer.MeltdownBlobRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {
    public static void init(){
        int id = 1;
        registerProjectile("enrichedpearlentity",id++,EnrichedPearlEntity.class, ModItems.enrichedPearlItem);
        EntityRegistry.registerModEntity(new ResourceLocation(SupearlpositionMod.MODID, "meltdownblobentity"), MeltdownBlobEntity.class, "meltdownblobentity", id++,
                SupearlpositionMod.instance, 64, 1, true);

    }

    private static void registerProjectile(String name, int id, Class<? extends Entity> entity, Item item) {
        EntityRegistry.registerModEntity(new ResourceLocation(name), entity, name, id, SupearlpositionMod.instance, 64, 10, true);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        RenderingRegistry.registerEntityRenderingHandler(EnrichedPearlEntity.class, new RenderSnowball<EnrichedPearlEntity>(Minecraft.getMinecraft().getRenderManager(), ModItems.enrichedPearlItem, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(MeltdownBlobEntity.class, new RenderSnowball<>(Minecraft.getMinecraft().getRenderManager(), null, Minecraft.getMinecraft().getRenderItem()));
    }


}
