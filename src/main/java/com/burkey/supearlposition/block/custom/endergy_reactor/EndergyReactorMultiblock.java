package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.block.ModBlocks;
import com.burkey.supearlposition.block.custom.tools.IMultiBlockType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EndergyReactorMultiblock implements IMultiBlockType {
    public static EndergyReactorMultiblock INSTANCE = new EndergyReactorMultiblock();
    private final int reactorSize = 2;

    private boolean isBlockPart(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.endergyReactorControlBlock || state.getBlock() == ModBlocks.endergyReactorCore;
    }

    private boolean isValidFormedBlockPart(World world, BlockPos pos, int dx, int dy, int dz) {
        BlockPos p = pos;
        if (isFormedReactorController(world, p)) {
            EndergyReactorPartIndex index = world.getBlockState(p).getValue(EndergyReactorControl.FORMED);
            return index == EndergyReactorPartIndex.getIndex(dx, dy, dz);
        } else if (isFormedReactorPart(world, p)) {
            EndergyReactorPartIndex index = world.getBlockState(p).getValue(EndergyReactorControl.FORMED);
            return index == EndergyReactorPartIndex.getIndex(dx, dy, dz);
        } else {
            // We can already stop here
            return false;
        }
    }

    private boolean isValidUnformedBlockPart(World world, BlockPos pos, int dx, int dy, int dz) {
        if (isUnformedReactorController(world, pos)) {
            return true;
        } else if (isUnformedReactorPart(world, pos)) {
            // We can already stop here
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public BlockPos getBottomLowerLeft(World world, BlockPos pos) {
        if (isBlockPart(world, pos)) {
            IBlockState state = world.getBlockState(pos);
            EndergyReactorPartIndex index = state.getValue(EndergyReactorControl.FORMED);
            return pos.add(-index.getDx(), -index.getDy(), -index.getDz());
        } else {
            return null;
        }
    }

    @Override
    public void unformBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(EndergyReactorControl.FORMED, EndergyReactorPartIndex.UNFORMED), 3);
    }

    @Override
    public void formBlock(World world, BlockPos pos, int dx, int dy, int dz) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(EndergyReactorControl.FORMED, EndergyReactorPartIndex.getIndex(dx, dy, dz)), 3);
    }

    @Override
    public boolean isValidUnformedMultiBlock(World world, BlockPos pos) {
        int cntSuper = 0;
        for (int dx = 0 ; dx < getWidth() ; dx++) {
            for (int dy = 0 ; dy < getHeight() ; dy++) {
                for (int dz = 0 ; dz < getDepth() ; dz++) {
                    BlockPos p = pos.add(dx, dy, dz);
                    if (!isValidUnformedBlockPart(world, p, dx, dy, dz)) {
                        return false;
                    }
                    if (world.getBlockState(p).getBlock() == ModBlocks.endergyReactorControlBlock) {
                        cntSuper++;
                    }
                }
            }
        }
        return cntSuper == 1;
    }

    @Override
    public boolean isValidFormedMultiBlock(World world, BlockPos pos) {
        int cntSuper = 0;
        for (int dx = 0; dx < getWidth(); dx++) {
            for (int dy = 0; dy < getHeight(); dy++) {
                for (int dz = 0; dz < getDepth(); dz++) {
                    BlockPos p = pos.add(dx, dy, dz);
                    if (!isValidFormedBlockPart(world, p, dx, dy, dz)) {
                        return false;
                    }
                    if (world.getBlockState(p).getBlock() == ModBlocks.endergyReactorControlBlock) {
                        cntSuper++;
                    }
                }
            }
        }
        return cntSuper == 1;
    }

    @Override
    public int getWidth() {
        return reactorSize;
    }

    @Override
    public int getHeight() {
        return reactorSize;
    }

    @Override
    public int getDepth() {
        return reactorSize;
    }


    private static boolean isUnformedReactorController(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.endergyReactorControlBlock && state.getValue(EndergyReactorControl.FORMED) == EndergyReactorPartIndex.UNFORMED;
    }

    public static boolean isFormedReactorController(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.endergyReactorControlBlock && state.getValue(EndergyReactorControl.FORMED) != EndergyReactorPartIndex.UNFORMED;
    }

    private static boolean isUnformedReactorPart(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.endergyReactorCore && state.getValue(EndergyReactorControl.FORMED) == EndergyReactorPartIndex.UNFORMED;
    }

    private static boolean isFormedReactorPart(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.endergyReactorCore && state.getValue(EndergyReactorControl.FORMED) != EndergyReactorPartIndex.UNFORMED;
    }
}
