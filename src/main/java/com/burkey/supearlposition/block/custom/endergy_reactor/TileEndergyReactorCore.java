package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileEndergyReactorCore extends TileEntity {
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.endergyReactorCore || state.getValue(EndergyReactorControl.FORMED) == EndergyReactorPartIndex.UNFORMED) {
            return false;
        }

        BlockPos controllerPos = EndergyReactorControl.getControllerPos(world, pos);
        if (controllerPos != null) {
            TileEntity te = world.getTileEntity(controllerPos);
            if (te instanceof TileEndergyReactorControl) {
                return te.hasCapability(capability, facing);
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.endergyReactorCore || state.getValue(EndergyReactorControl.FORMED) == EndergyReactorPartIndex.UNFORMED) {
            return null;
        }

        BlockPos controllerPos = EndergyReactorControl.getControllerPos(world, pos);
        if (controllerPos != null) {
            TileEntity te = world.getTileEntity(controllerPos);
            if (te instanceof TileEndergyReactorControl) {
                return te.getCapability(capability, facing);
            }
        }
        return super.getCapability(capability, facing);
    }
}
