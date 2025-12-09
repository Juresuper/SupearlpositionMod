package com.burkey.supearlposition.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import java.util.UUID;

public class EnrichedPearlEntity extends EntityThrowable {
    public EnrichedPearlEntity(World world) {
        super(world);
    }
    private Random random;
    private boolean isInMeltdown;
    private MeltdownBlobEntity parentBlob;
    private UUID parentUUID;
    public EnrichedPearlEntity(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
        isInMeltdown = false;
    }
    public EnrichedPearlEntity(World world, MeltdownBlobEntity blob, boolean Meltdown) {
        super(world);
        parentBlob = blob;
        isInMeltdown = Meltdown;
        this.posX = blob.posX;
        this.posY = blob.posY;
        this.posZ = blob.posZ;
        this.parentUUID = blob.getUniqueID();
    }

    public EnrichedPearlEntity(World world, double v, double v1, double v2) {
        super(world, v, v1, v2);
        isInMeltdown = false;
    }

    public static void registerFixesSnowball(DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "Snowball");
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte b) {
        if (b == 3) {
            for(int lvt_2_1_ = 0; lvt_2_1_ < 8; ++lvt_2_1_) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY, this.posZ, (double)0.0F, (double)0.0F, (double)0.0F, new int[0]);
            }
        }

    }

    @Override
    public void onUpdate() {
        random = new Random();
        float f1 = 0.99F;
        if (isInMeltdown) {
            double chaos = 0.1D;

            this.motionX += (random.nextDouble() - 0.5D) * chaos;
            this.motionY += (random.nextDouble() - 0.5D) * chaos;
            this.motionZ += (random.nextDouble() - 0.5D) * chaos;


            if (parentBlob != null) {
                if(this.ticksExisted % 40 == 0){
                    this.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                }
                if(this.ticksExisted == 200){
                    isInMeltdown = false;
                }
                this.setNoGravity(true);
                double dx = posX - parentBlob.posX;
                double dy = posY - parentBlob.posY;
                double dz = posZ - parentBlob.posZ;
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (dist > 1.0) {
                    double pull = 0.1;
                    motionX -= (dx / dist) * pull;
                    motionY -= (dy / dist) * pull;
                    motionZ -= (dz / dist) * pull;
                }
                this.motionX *= (double)f1;
                this.motionY *= (double)f1;
                this.motionZ *= (double)f1;
            }
        }else{
            this.setNoGravity(false);
        }

        super.onUpdate();
    }

    protected void onImpact(RayTraceResult traceResult) {
        Entity hitEntity = traceResult.entityHit;
        if(hitEntity != parentBlob){
            if (traceResult.entityHit != null) {
                int lvt_2_1_ = 0;
                if (traceResult.entityHit instanceof EntityBlaze) {
                    lvt_2_1_ = 3;
                }
                if (!(traceResult.entityHit instanceof MeltdownBlobEntity)) {
                    if (traceResult.entityHit instanceof EntityLivingBase) {
                        teleportEntity(traceResult.entityHit.getEntityWorld(), (EntityLivingBase) traceResult.entityHit);
                    }
                }


                traceResult.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)lvt_2_1_);
            }


            if (!this.world.isRemote && !(hitEntity instanceof MeltdownBlobEntity)) {
                this.world.setEntityState(this, (byte)3);
                this.setDead();
            }
        }


        if(traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            teleportBlock(this.world,traceResult.getBlockPos());
            this.setDead();

        }

    }

    private void teleportEntity(World world, EntityLivingBase livingBase){
        if (!world.isRemote) {
            double posx = livingBase.posX;
            double posy = livingBase.posY;
            double posz = livingBase.posZ;
            if(!(livingBase instanceof EntityLiving)) return;

            for(int lvt_11_1_ = 0; lvt_11_1_ < 16; ++lvt_11_1_) {
                double lvt_12_1_ = livingBase.posX + (livingBase.getRNG().nextDouble() - (double)0.5F) * (double)16.0F;
                double lvt_14_1_ = MathHelper.clamp(livingBase.posY + (double)(livingBase.getRNG().nextInt(16) - 8), (double)0.0F, (double)(world.getActualHeight() - 1));
                double lvt_16_1_ = livingBase.posZ + (livingBase.getRNG().nextDouble() - (double)0.5F) * (double)16.0F;
                if (livingBase.isRiding()) {
                    livingBase.dismountRidingEntity();
                }

                if (livingBase.attemptTeleport(lvt_12_1_, lvt_14_1_, lvt_16_1_)) {
                    world.playSound((EntityPlayer)null, posx, posy, posz, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    livingBase.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    break;
                }
            }

        }
    }
    private void teleportBlock(World world, BlockPos blockPos){
        IBlockState iblockstate = world.getBlockState(blockPos);
        Block block = iblockstate.getBlock();
        if(!(block instanceof ITileEntityProvider) && (block.getBlockHardness(iblockstate,world,blockPos) >= 0)) {
            for(int i = 0; i < 1000; ++i) {
                BlockPos blockpos = blockPos.add(world.rand.nextInt(16) - world.rand.nextInt(16), world.rand.nextInt(8) - world.rand.nextInt(8), world.rand.nextInt(16) - world.rand.nextInt(16));
                if (world.isAirBlock(blockpos)) {
                    if (world.isRemote) {
                        for (int j = 0; j < 128; ++j) {
                            double d0 = world.rand.nextDouble();
                            float f = (world.rand.nextFloat() - 0.5F) * 0.2F;
                            float f1 = (world.rand.nextFloat() - 0.5F) * 0.2F;
                            float f2 = (world.rand.nextFloat() - 0.5F) * 0.2F;
                            double d1 = (double) blockpos.getX() + (double) (blockPos.getX() - blockpos.getX()) * d0 + (world.rand.nextDouble() - (double) 0.5F) + (double) 0.5F;
                            double d2 = (double) blockpos.getY() + (double) (blockPos.getY() - blockpos.getY()) * d0 + world.rand.nextDouble() - (double) 0.5F;
                            double d3 = (double) blockpos.getZ() + (double) (blockPos.getZ() - blockpos.getZ()) * d0 + (world.rand.nextDouble() - (double) 0.5F) + (double) 0.5F;
                            world.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, (double) f, (double) f1, (double) f2, new int[0]);
                        }
                    } else {
                        world.setBlockState(blockpos, iblockstate, 2);
                        world.setBlockToAir(blockPos);
                    }

                    return;
                }
            }
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("meltdown",isInMeltdown);
        return compound;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if(this.parentBlob != null){
            compound.setUniqueId("parent",this.parentBlob.getUniqueID());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        compound.getBoolean("meltdown");
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if(compound.hasUniqueId("parent")){
            parentUUID = compound.getUniqueId("parent");
        }
    }
}
