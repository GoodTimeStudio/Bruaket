package com.goodtime.bruaket.blocks;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.entity.TileEntityBarrel;
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
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Barrel extends BlockContainer {

    String name;

     // FACING  方块的自定义属性，描述放置方块时玩家的朝向
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.UP);

     // ENABLED  方块的自定义属性，描述该方块的开关情况
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");

    public Barrel(String registerName) {
        super(Material.ROCK);
        name = registerName;
        this.setRegistryName(Bruaket.MODID, registerName);
        this.setCreativeTab(Bruaket.CREATIVE_TAB);
        this.setUnlocalizedName(Bruaket.MODID+"." + registerName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(ENABLED, Boolean.TRUE));
        register();
    }

    private void register() {

        ForgeRegistries.BLOCKS.register(this);

        ForgeRegistries.ITEMS.register(new ItemBlock(this).setRegistryName(name));

        modelRegister();

        //GameRegistry.registerTileEntity(TileEntityEnderHopper.class, MODID + ":enderhopper");
    }


    private void modelRegister(){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
        ModelLoader.setCustomStateMapper(
                this, (new StateMap.Builder()).ignore(ENABLED).build()
        );
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

   /* @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = facing.getOpposite();

        if (enumfacing == EnumFacing.UP)
        {
            enumfacing = EnumFacing.DOWN;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(ENABLED, Boolean.TRUE);
    }*/

    //所使用的TileEntity
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBarrel();
    }

    //玩家放下方块后所执行的操作（尚待研究）
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntityBarrel tileentity = (TileEntityBarrel) worldIn.getTileEntity(pos);

        if (stack.hasDisplayName()) {
            if (tileentity != null) {
                tileentity.setCustomName(stack.getDisplayName());
            }
        }

        if(placer instanceof EntityPlayer) {
            tileentity.setOwner(placer.getUniqueID());
        }
    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityBarrel)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBarrel)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    //以模型的方式渲染该方块
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getIndex();

        if (!state.getValue(ENABLED))
        {
            i |= 8;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, ENABLED);
    }

}
