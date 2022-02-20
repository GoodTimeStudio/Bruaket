package com.goodtime.bruaket.blocks;

import com.goodtime.bruaket.TIleEntity.TileEntityBucket;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SimpleBucket extends BlockContainer {

    public SimpleBucket(Material materialIn) {
        super(materialIn);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBucket();
    }
}
