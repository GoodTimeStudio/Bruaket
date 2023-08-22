/*
 * This file is part of Bruaket, licensed under the MIT License (MIT).
 * Copyright (c) 2023. GoodTime Studio
 */

package com.github.goodtimestudio.init

import com.github.goodtimestudio.Bruaket
import com.github.goodtimestudio.group.Groups
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text

class InitGroups(itemMap:HashMap<String, Item>) {
    private val ITEM_MAP = itemMap

    private fun addItemToGroup(group: RegistryKey<ItemGroup>) {
        ITEM_MAP.forEach { (name, item) ->
            ItemGroupEvents.modifyEntriesEvent(group).register { content: FabricItemGroupEntries ->
                content.add(item)
            }
        }
    }

    fun register() {
        Bruaket.LOGGER.info("Registering Groups ${Groups().GROUP_MAIN.value}")
        Registry.register(Registries.ITEM_GROUP, Groups().GROUP_MAIN, FabricItemGroup.builder()
            .displayName(Text.translatable("${Bruaket.MOD_ID}.group.main"))
            .build()
        )
    }

    init {
        addItemToGroup(Groups().GROUP_MAIN)
    }
}