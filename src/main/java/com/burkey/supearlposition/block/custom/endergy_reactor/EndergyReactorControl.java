package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.SupearlpositionMod;
import com.burkey.supearlposition.block.BlockBase;
import com.burkey.supearlposition.block.ModBlocks;
import com.burkey.supearlposition.block.custom.tools.MultiBlockTools;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;

public class EndergyReactorControl extends BlockBase implements ITileEntityProvider {

    public static final ResourceLocation ENDERGY_REACTOR_CONTROL = new ResourceLocation(SupearlpositionMod.MODID,"endergy_reactor_control");
    //public static final PropertyDirection FACING = PropertyDirection.create("facing");
    //public static final PropertyEnum<EndergyReactorState>  STATE = PropertyEnum.create("state", EndergyReactorState.class);

    public static final PropertyEnum<EndergyReactorPartIndex> FORMED = PropertyEnum.<EndergyReactorPartIndex>create("formed",EndergyReactorPartIndex.class);
    public static final int GUI_ID = 1;


    public EndergyReactorControl(Material material, String name) {
        super(material, name);
        setHardness(5.0F);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", 1);

        setDefaultState(this.blockState.getBaseState().withProperty(FORMED, EndergyReactorPartIndex.UNFORMED));
    }
    public static boolean isFormedEndergyController(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.endergyReactorControlBlock && state.getValue(FORMED) != EndergyReactorPartIndex.UNFORMED;
    }
    @Override
    public EndergyReactorControl setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    /*@Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float p_getStateForPlacement_4_, float p_getStateForPlacement_5_, float p_getStateForPlacement_6_, int p_getStateForPlacement_7_, EntityLivingBase placer, EnumHand p_getStateForPlacement_9_) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }*/

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FORMED, EndergyReactorPartIndex.VALUES[meta]);
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,FORMED);

    }

    @Override
    public int getMetaFromState(IBlockState meta) {

        return (meta.getValue(FORMED).ordinal());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEndergyReactorControl();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        if(player.isSneaking() && player.getHeldItem(hand).isEmpty()){
            toggleMultiBlock(world, pos, state, player);
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEndergyReactorControl)) {
            return false;
        }
        if(state.getBlock() == ModBlocks.endergyReactorControlBlock && state.getValue(FORMED) != EndergyReactorPartIndex.UNFORMED){
            player.openGui(SupearlpositionMod.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    /*@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if (te instanceof TileEndergyReactorControl) {
            return state.withProperty(STATE, ((TileEndergyReactorControl) te).getState());
        }
        return super.getActualState(state, worldIn, pos);
    }*/

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (!worldIn.isRemote) {
            MultiBlockTools.breakMultiblock(EndergyReactorMultiblock.INSTANCE, worldIn, pos);
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
    public static void toggleMultiBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        // Form or break the multiblock
        if (!world.isRemote) {
            EndergyReactorPartIndex formed = state.getValue(FORMED);
            if (formed == EndergyReactorPartIndex.UNFORMED) {
                if (MultiBlockTools.formMultiblock(EndergyReactorMultiblock.INSTANCE, world, pos)) {
                    player.sendStatusMessage(new TextComponentString(TextFormatting.GREEN + "Made a reactor!"), false);
                } else {
                    player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "Could not form reactor!"), false);
                }
            } else {
                if (!MultiBlockTools.breakMultiblock(EndergyReactorMultiblock.INSTANCE, world, pos)) {
                    player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "Not a valid reactor!"), false);
                }
            }
        }
    }

    @Nullable
    public static BlockPos getControllerPos(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.endergyReactorControlBlock && state.getValue(EndergyReactorControl.FORMED) != EndergyReactorPartIndex.UNFORMED) {
            return pos;
        }
        if (state.getBlock() == ModBlocks.endergyReactorCore && state.getValue(EndergyReactorControl.FORMED) != EndergyReactorPartIndex.UNFORMED) {
            EndergyReactorPartIndex index = state.getValue(EndergyReactorControl.FORMED);
            // This index indicates where in the superblock this part is located. From this we can find the location of the bottom-left coordinate
            BlockPos bottomLeft = pos.add(-index.getDx(), -index.getDy(), -index.getDz());
            for (EndergyReactorPartIndex idx : EndergyReactorPartIndex.VALUES) {
                if (idx != EndergyReactorPartIndex.UNFORMED) {
                    BlockPos p = bottomLeft.add(idx.getDx(), idx.getDy(), idx.getDz());
                    if (isFormedEndergyController(world, p)) {
                        return p;
                    }
                }
            }

        }
        return null;
    }


}
