package com.burkey.supearlposition.item;

import com.burkey.supearlposition.projectile.EnrichedPearlEntity;
import com.burkey.supearlposition.projectile.MeltdownBlobEntity;
import ibxm.Player;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.Random;

public class EnrichedPearlItem extends ItemBase{
    public EnrichedPearlItem(String name) {
        super(name);
        this.maxStackSize = 64;

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer PlayerIn, EnumHand HandIn) {
        ItemStack lvt_4_1_ = PlayerIn.getHeldItem(HandIn);
        if (!PlayerIn.capabilities.isCreativeMode) {
            lvt_4_1_.shrink(1);
        }

        worldIn.playSound((EntityPlayer)null, PlayerIn.posX, PlayerIn.posY, PlayerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote) {
            EnrichedPearlEntity enrichedPearl = new EnrichedPearlEntity(worldIn, PlayerIn);

            enrichedPearl.shoot(PlayerIn, PlayerIn.rotationPitch, PlayerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(enrichedPearl);

        }

        PlayerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, lvt_4_1_);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
        final Random random = new Random();

        if(itemStack.getItem() instanceof EnrichedPearlItem){

            if(itemStack.getCount() > 10){
                itemStack.shrink(1);

                EnrichedPearlEntity enrichedPearl = new EnrichedPearlEntity(world, (EntityLivingBase) entity);
                //potionentity.setXRot(-20.0F);
                enrichedPearl.shoot(-2.0 + random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble(), 2.0 + random.nextDouble() + random.nextDouble(), -2.0 + random.nextDouble() + random.nextDouble() + random.nextDouble() + random.nextDouble(), 0.75F, 8.0F);
                world.spawnEntity(enrichedPearl);
            }
        }
        super.onUpdate(itemStack, world, entity, p_77663_4_, p_77663_5_);
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 160;
    }
}
