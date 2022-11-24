package com.goodtime.bruaket.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class NetherBarrel extends Barrel{

    public NetherBarrel(String registerName, float hardness) {
        super(registerName, hardness);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
