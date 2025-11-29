package com.burkey.supearlposition.proxy;

import com.burkey.supearlposition.block.custom.endergy_reactor.EndergyReactorControlContainer;
import com.burkey.supearlposition.block.custom.endergy_reactor.GuiEndergyReactor;
import com.burkey.supearlposition.block.custom.endergy_reactor.TileEndergyReactorControl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEndergyReactorControl) {
            return new EndergyReactorControlContainer(player.inventory, (TileEndergyReactorControl) te);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEndergyReactorControl) {
            TileEndergyReactorControl containerTileEntity = (TileEndergyReactorControl) te;
            return new GuiEndergyReactor(containerTileEntity, new EndergyReactorControlContainer(player.inventory, containerTileEntity));
        }
        return null;
    }
}
