package com.burkey.supearlposition.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class MeltdownBlobEntity extends Entity {
    public int explosionPower = 1;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    private Random random;
    private double centerx,centery,centerz;
    private int pearlCount = 0;
    private boolean spawnPearls = true;


    public MeltdownBlobEntity(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        random = new Random();
        this.accelerationX = random.nextGaussian() * 0.02D;
        this.accelerationY = random.nextGaussian() * 0.02D;
        this.accelerationZ = random.nextGaussian() * 0.02D;
        this.centerx = posX;
        this.centery = posY;
        this.centerz = posZ;

    }

    public MeltdownBlobEntity(World worldIn, double x, double y, double z, int pearlCount)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);

        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        random = new Random();
        this.accelerationX = random.nextGaussian() * 0.02D;
        this.accelerationY = random.nextGaussian() * 0.02D;
        this.accelerationZ = random.nextGaussian() * 0.02D;
        this.centerx = x;
        this.centery = y;
        this.centerz = z;
        this.pearlCount = pearlCount;

    }
    public void onUpdate()
    {

        random = new Random();

        if (this.world.isRemote || this.world.isBlockLoaded(new BlockPos(this)))
        {

            super.onUpdate();
            List<Entity> entities = this.getEntities();
            int AlivePearlCount = 0;
            for (Entity entity : entities){
                if(entity instanceof EnrichedPearlEntity){
                    AlivePearlCount++;
                }
            }
            this.spawnPearls = AlivePearlCount < 50;
            if(pearlCount > 0 && spawnPearls){
                EnrichedPearlEntity enrichedPearl = new EnrichedPearlEntity(world, this, true);
                world.spawnEntity(enrichedPearl);
                pearlCount--;
                System.out.println(pearlCount);
            }
            ++this.ticksInAir;
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();

            if (this.isInWater())
            {
                for (int i = 0; i < 4; ++i)
                {
                    float f1 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
                }

                f = 0.8F;
            }
            double chaos = 0.2D;

            this.motionX += (random.nextDouble() - 0.5D) * chaos;
            this.motionY += (random.nextDouble() - 0.5D) * chaos;
            this.motionZ += (random.nextDouble() - 0.5D) * chaos;

            this.motionX *= (double)f;
            this.motionY *= (double)f;
            this.motionZ *= (double)f;
            this.world.spawnParticle(this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);

            double distX = posX - centerx;
            double distY = posY - centery;
            double distZ = posZ - centerz;
            double dist =  Math.sqrt(distX * distX + +distY * distY + distZ * distZ);

            if(dist > 5){
                double pull = 0.05; // gentle pullback
                motionX -= (distX / dist) * pull;
                motionY -= (distY / dist) * pull;
                motionZ -= (distZ / dist) * pull;

            }
        }
        else
        {
            this.setDead();
        }
    }

    protected EnumParticleTypes getParticleType()
    {
        return EnumParticleTypes.SMOKE_NORMAL;
    }
    protected float getMotionFactor()
    {
        return 0.95F;
    }
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
        compound.setTag("power", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
        compound.setInteger("life", this.ticksAlive);
        compound.setInteger("pearlCount", this.pearlCount);
        compound.setTag("center", this.newDoubleNBTList(new double[] {this.centerx, this.centery, this.centerz}));


    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        if (compound.hasKey("power", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("power", 6);

            if (nbttaglist.tagCount() == 3)
            {
                this.accelerationX = nbttaglist.getDoubleAt(0);
                this.accelerationY = nbttaglist.getDoubleAt(1);
                this.accelerationZ = nbttaglist.getDoubleAt(2);
            }
        }
        if(compound.hasKey("center", 9)){
            NBTTagList nbttaglist = compound.getTagList("center", 6);

            this.centerx = nbttaglist.getDoubleAt(0);
            this.centery = nbttaglist.getDoubleAt(1);
            this.centerz = nbttaglist.getDoubleAt(2);
        }

        this.ticksAlive = compound.getInteger("life");
        this.pearlCount = compound.getInteger("pearlCount");

        if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3)
        {
            NBTTagList nbttaglist1 = compound.getTagList("direction", 6);
            this.motionX = nbttaglist1.getDoubleAt(0);
            this.motionY = nbttaglist1.getDoubleAt(1);
            this.motionZ = nbttaglist1.getDoubleAt(2);
        }
        else
        {
            this.setDead();
        }
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public float getCollisionBorderSize()
    {
        return 1.0F;
    }

    public float getBrightness()
    {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    @Override
    protected void entityInit() {

    }

    public List<Entity> getEntities() {
        double r = 20.0;
        AxisAlignedBB box = new AxisAlignedBB(
                posX - r, posY - r, posZ - r,
                posX + r, posY + r, posZ + r
        );
        return world.getEntitiesWithinAABB(Entity.class, box);
    }





    public static void registerFixesLargeFireball(DataFixer fixer)
    {
        EntityFireball.registerFixesFireball(fixer, "Fireball");
    }

}
