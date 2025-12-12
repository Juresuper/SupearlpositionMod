package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.SupearlpositionMod;
import com.burkey.supearlposition.block.BlockBase;
import com.burkey.supearlposition.block.ModBlocks;
import com.burkey.supearlposition.block.custom.tools.MultiBlockTools;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static com.burkey.supearlposition.block.custom.endergy_reactor.EndergyReactorControl.FORMED;

public class EndergyReactorCoreBlock extends BlockBase implements ITileEntityProvider {
    public static final ResourceLocation ENDERGY_REACTOR_CORE = new ResourceLocation(SupearlpositionMod.MODID, "endergy_reactor_core");

    public EndergyReactorCoreBlock(Material material, String name) {
        super(material,name);
        setHardness(3.0F);
        setResistance(9.0F);
        setHarvestLevel("pickaxe", 1);
        setDefaultState(blockState.getBaseState().withProperty(FORMED, EndergyReactorPartIndex.UNFORMED));

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEndergyReactorCore();
    }


    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() == Items.STICK) {
            EndergyReactorControl.toggleMultiBlock(world, pos, state, player);
            return true;
        }
        // Only work if the block is formed
        if (state.getBlock() == ModBlocks.endergyReactorCore && state.getValue(FORMED) != EndergyReactorPartIndex.UNFORMED) {
            // Find the controller
            BlockPos controllerPos = EndergyReactorControl.getControllerPos(world, pos);
            if (controllerPos != null) {
                IBlockState controllerState = world.getBlockState(controllerPos);
                return controllerState.getBlock().onBlockActivated(world, controllerPos, controllerState, player, hand, facing, hitX, hitY, hitZ);
            }
        }
        return false;
    }


    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote) {
            MultiBlockTools.breakMultiblock(EndergyReactorMultiblock.INSTANCE, world, pos);
        }
        super.onBlockHarvested(world, pos, state, player);
    }


    @Override
    public boolean isFullCube(IBlockState state) {
        if (state.getValue(FORMED) == EndergyReactorPartIndex.UNFORMED) {
            return super.isFullCube(state);
        } else {
            return false;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FORMED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FORMED, EndergyReactorPartIndex.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FORMED).ordinal());
    }
}
