package com.goodtime.bruaket.blocks;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.entity.bruaket.IBarrelTile;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author Java0
 */

public abstract class Barrel extends BlockContainer {
    String name;

    // 方块的自定义属性，描述方块的朝向
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>()
    {
        public boolean apply(@Nullable EnumFacing p_apply_1_)
        {
            return p_apply_1_ != EnumFacing.UP;
        }
    });

    // 方块的自定义属性，描述该方块的工作情况
    public static final PropertyBool WORKING = PropertyBool.create("working");

    public Barrel(String registerName, float hardness) {
        super(Material.ROCK);
        name = registerName;
        this.setHardness(hardness);
        this.setRegistryName(Bruaket.MODID, registerName);
        this.setCreativeTab(Bruaket.CREATIVE_TAB);
        this.setUnlocalizedName(Bruaket.MODID + "." + registerName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(WORKING, Boolean.FALSE));
        register();
    }


    private void register() {

        ForgeRegistries.BLOCKS.register(this);

        ForgeRegistries.ITEMS.register(new ItemBlock(this).setRegistryName(name));

        modelRegister();
    }


    private void modelRegister() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Objects.requireNonNull(this.getRegistryName()), "inventory"));
        ModelLoader.setCustomStateMapper(this, (new StateMap.Builder()).ignore(WORKING).build());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }


    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(WORKING, Boolean.FALSE);
    }

    //所使用的TileEntity
    @Override
    public abstract TileEntity createNewTileEntity(World worldIn, int meta);

    //玩家放下方块后所执行的操作（尚待研究）
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        IBlockState newState = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
        worldIn.setBlockState(pos,newState);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IBarrelTile) {
                if (playerIn.isSneaking()) {
                    ((IBarrelTile) tileentity).dropAllItems();
                } else {
                    ((IBarrelTile) tileentity).dropLastItem();
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IBarrelTile) {
                ((IBarrelTile) tileentity).dropTalisman();
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IBarrelTile) {
            IBarrelTile barrel = (IBarrelTile) tileentity;
            if (barrel.getItems() != null) {
                barrel.dropTalisman();
                barrel.dropAllItems();
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int tickRate(World worldIn) {
        return 4;
    }

    /*TODO*/
    //检测红石信号
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        //附近方块是否带有红石信号
        boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IBarrelTile) {
        }
    }

    //以模型的方式渲染该方块
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /*TODO*/
    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {

        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getIndex();
        if (!state.getValue(WORKING)) {
            i |= 8;
        }
        return i;
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, WORKING);
    }

}
