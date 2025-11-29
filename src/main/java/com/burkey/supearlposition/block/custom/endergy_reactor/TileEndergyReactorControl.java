package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.block.ModBlocks;
import com.burkey.supearlposition.item.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import scala.collection.parallel.ParIterableLike;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.common.ForgeVersion.getResult;

public class TileEndergyReactorControl extends TileEntity implements ITickable {


    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
    public static final int INPUT_SLOT = 2;
    public static final int OUTPUT_SLOTS = 1;

    //CONFIGURABLE
    public static final int MAX_PROGRESS = 10;

    public static final int MIN_PEARL_COUNT = 100;
    public static final int REACTOR_CYCLE_LENGTH = 80;
    public static final int MAX_PEARL_COUNT = 100000;



    private int progress = 0;
    private int pearl_count = 0;
    private int clientProgress = -1;
    private int clientPearlCount = -1;
    private int clientExcessFuel = -1;
    private int clientReactorCycleCount = -1;



    private EndergyReactorState state = EndergyReactorState.OFF;

    public int getReactor_cycle_count() {
        return reactor_cycle_count;
    }

    public void setReactor_cycle_count(int reactor_cycle_count) {
        this.reactor_cycle_count = reactor_cycle_count;
    }

    private int reactor_cycle_count = 0;

    private double conversion_rate = 0.05;


    private int fuel_produced = 0;

    public int getExcess_fuel() {
        return excess_fuel;
    }

    public void setExcess_fuel(int excess_fuel) {
        this.excess_fuel = excess_fuel;
    }

    private int excess_fuel = 0;

    private ItemStackHandler inputHandler = new ItemStackHandler(INPUT_SLOT){

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() == Items.ENDER_PEARL || stack.getItem() == ModItems.enrichedPearlItem;
        }
        @Override
        protected void onContentsChanged(int slot) {
            TileEndergyReactorControl.this.markDirty();
        }
    };
    private ItemStackHandler outputHandler = new ItemStackHandler(OUTPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TileEndergyReactorControl.this.markDirty();
        }
    };
    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(inputHandler, outputHandler);
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    @Override
    public void update() {
        if(!world.isRemote){
            if(progress > 0){
                progress--;
                if(progress <= 0){
                    attemptConsume();
                }
                markDirty();
            }else{
                startConsume();
            }
            if(pearl_count >= MIN_PEARL_COUNT){
                setState(EndergyReactorState.WORKING);
                if(reactor_cycle_count > 0){
                    reactor_cycle_count--;
                markDirty();
                }else{
                    runReactor();
                }
            }
        }
    }

    private void runReactor(){
        fuel_produced = (int) (pearl_count * conversion_rate);
        //pearl_count -= fuel_produced;

        int spaceAvailable = checkSpace();
        int fuelToInsert = Math.min(fuel_produced, spaceAvailable);
        excess_fuel = 0;

        excess_fuel += fuel_produced - fuelToInsert;
        if (fuelToInsert > 0) {
            ItemStack stack = new ItemStack(ModItems.enrichedPearlItem, fuelToInsert);
            if (insertOutput(stack, false)) {
                markDirty();
            }
        }
        if (spaceAvailable <= 0) {
            convertExcess();
        }

        reactor_cycle_count = REACTOR_CYCLE_LENGTH;
        markDirty();
    }
    private int checkSpace(){
        ItemStack currentOutput = outputHandler.getStackInSlot(0);
        int spaceAvailable = 0;
        if (currentOutput.isEmpty()) {
            spaceAvailable = 64;
        } else if (currentOutput.getItem() == ModItems.enrichedPearlItem) {
            spaceAvailable = currentOutput.getMaxStackSize() - currentOutput.getCount();
        }
        return spaceAvailable;
    }
    private void convertExcess(){

        pearl_count += excess_fuel * 5;

        markDirty();
    }
    private void attemptConsume(){
        for(int i = 0; i < INPUT_SLOT; i++){
            ItemStack stack = inputHandler.getStackInSlot(i);
            if(!stack.isEmpty()){
                markDirty();
            }
        }
    }
    private boolean insertOutput(ItemStack output, boolean simulate) {
        for (int i = 0 ; i < OUTPUT_SLOTS ; i++) {
            ItemStack remaining = outputHandler.insertItem(i, output, simulate);
            if (remaining.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void startConsume(){
        for(int i = 0; i < INPUT_SLOT; i++){
            ItemStack stack = inputHandler.getStackInSlot(i);
            if(!stack.isEmpty()){
                if(stack.getItem() == Items.ENDER_PEARL){
                    pearl_count += 1;
                }else if(stack.getItem() == ModItems.enrichedPearlItem){
                    pearl_count += 5;
                }
                inputHandler.extractItem(i, 1, false);

                progress = MAX_PROGRESS;

                markDirty();
            }
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("items")){
            inputHandler.deserializeNBT((NBTTagCompound) compound.getCompoundTag("items"));
        }
        progress =  compound.getInteger("progress");
        pearl_count = compound.getInteger("pearl_count");
        excess_fuel = compound.getInteger("excess_fuel");
        reactor_cycle_count = compound.getInteger("reactor_cycle_count");


    }



    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", inputHandler.serializeNBT());
        compound.setInteger("progress", progress);
        compound.setInteger("pearl_count", pearl_count);
        compound.setInteger("excess_fuel", excess_fuel);
        compound.setInteger("reactor_cycle_count", reactor_cycle_count);


        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTag = super.getUpdateTag();
        nbtTag.setInteger("state", state.ordinal());
        return nbtTag;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        int stateIndex = packet.getNbtCompound().getInteger("state");

        if (world.isRemote && stateIndex != state.ordinal()) {
            state = EndergyReactorState.VALUES[stateIndex];
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.endergyReactorControlBlock || state.getValue(EndergyReactorControl.FORMED) == EndergyReactorPartIndex.UNFORMED) {
            return false;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.endergyReactorControlBlock || state.getValue(EndergyReactorControl.FORMED) == EndergyReactorPartIndex.UNFORMED) {
            return null;
        }

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedHandler);
        }
        return super.getCapability(capability, facing);
    }


    public int getPearl_count() {
        return pearl_count;
    }

    public void setPearl_count(int pearl_count) {
        this.pearl_count = pearl_count;
    }

    public int getClientProgress() {
        return clientProgress;
    }

    public void setClientProgress(int clientProgress) {
        this.clientProgress = clientProgress;
    }

    public int getClientPearlCount() {
        return clientPearlCount;
    }

    public void setClientPearlCount(int clientPearlCount) {
        this.clientPearlCount = clientPearlCount;
    }

    public int getClientExcessFuel() {
        return clientExcessFuel;
    }

    public void setClientExcessFuel(int clientExcessFuel) {
        this.clientExcessFuel = clientExcessFuel;
    }

    public int getClientReactorCycleCount() {
        return clientReactorCycleCount;
    }

    public void setClientReactorCycleCount(int clientReactorCycleCount) {
        this.clientReactorCycleCount = clientReactorCycleCount;
    }
    public EndergyReactorState getState() {
        return state;
    }

    public void setState(EndergyReactorState state) {
        if (this.state != state) {
            this.state = state;
            markDirty();
            IBlockState blockState = world.getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, blockState, blockState, 3);
        }
    }

}
