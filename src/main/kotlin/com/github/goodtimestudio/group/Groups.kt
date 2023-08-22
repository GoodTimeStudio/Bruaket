/*
 * This file is part of Bruaket, licensed under the MIT License (MIT).
 * Copyright (c) 2023. GoodTime Studio
 */

package com.github.goodtimestudio.group

import com.github.goodtimestudio.Bruaket
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier


class Groups {
    val GROUP_MAIN: RegistryKey<ItemGroup> =
        RegistryKey.of(
            RegistryKeys.ITEM_GROUP,
            Identifier(Bruaket.MOD_ID, "group_main")
        )
}