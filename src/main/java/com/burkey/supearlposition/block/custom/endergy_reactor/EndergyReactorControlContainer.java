package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.network.Messages;
import com.burkey.supearlposition.network.PacketSyncPearls;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class EndergyReactorControlContainer extends Container {
    private TileEndergyReactorControl te;

    private static final int PROGRESS_ID=0;
    private static final int PEARL_COUNT_ID=1;
    private static final int REACTOR_CYCLE_PROGRESS_ID=2;
    private static final int EXCESS_FUEL_ID=3;

    public EndergyReactorControlContainer(IInventory playerInventory, TileEndergyReactorControl te) {
        this.te = te;

        // This container references items out of our own inventory (the 9 slots we hold ourselves)
        // as well as the slots from the player inventory so that the user can transfer items between
        // both inventories. The two calls below make sure that slots are defined for both inventories.
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory


        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 47 + row * 18;
            int y = 231; // one slot is 12 pixels
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 47 + col * 18;
                int y = row * 18 + 173;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int x = 9;
        int y = 25;


        int slotIndex = 0;
        addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, 47, 117)); x+=18;
        addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, 192, 117)); x+=18;

        //addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));

        x=117;
        addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y)); x+=18;
        //addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y)); x+=18;
        //addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));

        // Add our own slots
        /*int slotIndex = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            x += 18;
        }*/
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileEndergyReactorControl.INPUT_SLOT) {
                if (!this.mergeItemStack(itemstack1, TileEndergyReactorControl.INPUT_SLOT, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileEndergyReactorControl.INPUT_SLOT, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if(te.getProgress() != te.getClientProgress()
                || te.getPearl_count() != te.getClientPearlCount()
                || te.getReactor_cycle_count() != te.getClientReactorCycleCount()
                || te.getExcess_fuel() != te.getClientExcessFuel()
        ){
            te.setClientProgress(te.getProgress());
            te.setClientPearlCount(te.getPearl_count());
            te.setClientReactorCycleCount(te.getReactor_cycle_count());
            te.setClientExcessFuel(te.getExcess_fuel());
            for(IContainerListener listener : this.listeners){
                if(listener instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP)listener;
                    Messages.INSTANCE.sendTo(new PacketSyncPearls(te.getPearl_count(),te.getReactor_cycle_count(),te.getProgress(),te.getExcess_fuel()),player);

                }
                listener.sendWindowProperty(this, PEARL_COUNT_ID, this.te.getPearl_count());
                listener.sendWindowProperty(this, PROGRESS_ID, this.te.getProgress());
                listener.sendWindowProperty(this,REACTOR_CYCLE_PROGRESS_ID, this.te.getReactor_cycle_count());
                listener.sendWindowProperty(this,EXCESS_FUEL_ID, this.te.getExcess_fuel());

                }

            }

    }

    @Override
    public void updateProgressBar(int id, int data) {
        if(id == PROGRESS_ID){
            te.setClientProgress(data);
        }else if(id == PEARL_COUNT_ID){
            te.setPearl_count(data);
        }else if(id == REACTOR_CYCLE_PROGRESS_ID){
            te.setReactor_cycle_count(data);
        }else if(id == EXCESS_FUEL_ID){
            te.setExcess_fuel(data);
        }
    }

    public void syncNumOfPearls(int numOfPearls, int progress, int reactorCycleCount, int excessFuel){
            te.setClientPearlCount(numOfPearls);
            te.setClientReactorCycleCount(reactorCycleCount);
            te.setClientExcessFuel(excessFuel);
            te.setClientProgress(progress);
    }
}
