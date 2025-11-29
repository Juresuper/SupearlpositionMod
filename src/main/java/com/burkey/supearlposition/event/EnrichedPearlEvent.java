package com.burkey.supearlposition.event;

import com.burkey.supearlposition.block.custom.endergy_reactor.EndergyReactorControl;
import com.burkey.supearlposition.block.custom.endergy_reactor.TileEndergyReactorControl;
import com.burkey.supearlposition.item.EnrichedPearlItem;
import com.burkey.supearlposition.projectile.EnrichedPearlEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class EnrichedPearlEvent {
    public static final EnrichedPearlEvent instance = new EnrichedPearlEvent();

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event){
        if (event.phase != TickEvent.Phase.END) return;
        World world =  event.world;
        if (world.getTotalWorldTime() % 20 == 0){

            for(TileEntity tileEntity : world.loadedTileEntityList){
                if(tileEntity instanceof IInventory && !(tileEntity instanceof TileEndergyReactorControl)){
                    int pearlCount = 0;
                    for(int i = 0; i < ((IInventory)tileEntity).getSizeInventory(); i++){
                        ItemStack stack = ((IInventory)tileEntity).getStackInSlot(i);
                        if(stack.getItem() instanceof EnrichedPearlItem){
                            pearlCount += stack.getCount();
                            if(pearlCount > 10){
                                for(int j = 0; j < pearlCount; j++){
                                    Random random = new Random();
                                    stack.shrink(1);
                                    EnrichedPearlEntity enrichedPearl = new EnrichedPearlEntity(world, tileEntity.getPos().getX() , tileEntity.getPos().getY() +0.5F, tileEntity.getPos().getZ());
                                    enrichedPearl.shoot(-2.0 + random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble(), 2.0 + random.nextDouble() + random.nextDouble(), -2.0 + random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble(), 0.75F, 8.0F);
                                    world.spawnEntity(enrichedPearl);
                                }
                            }



                        }
                    }
                }
            }
        };

    }
}
