package com.goodtime.bruaket.blocks;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.entity.TileEntityBucket;
import com.goodtime.bruaket.init.ModelMapper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;

public class Bucket extends BlockContainer {

    String name;

    public Bucket(Material materialIn, String registerName) {
        super(materialIn);
        name = registerName;
        this.setRegistryName(Bruaket.MODID, registerName);
        this.setCreativeTab(Bruaket.CREATIVE_TAB);
        this.setUnlocalizedName(Bruaket.MODID+"." + registerName);
        ModelMapper.registerBlockRender(this);
        ForgeRegistries.BLOCKS.register(this);
        registerItemBlock();
    }

    private void registerItemBlock(){
        ItemBlock item = new ItemBlock(this);
        item.setRegistryName(Bruaket.MODID, name);
        ForgeRegistries.ITEMS.register(item);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBucket();
    }
}
