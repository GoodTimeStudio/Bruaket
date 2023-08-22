/*
 * This file is part of Bruaket, licensed under the MIT License (MIT).
 * Copyright (c) 2023. GoodTime Studio
 */

package com.github.goodtimestudio

import com.github.goodtimestudio.init.InitData
import com.github.goodtimestudio.init.InitGroups
import com.github.goodtimestudio.init.InitItems
import net.fabricmc.api.ModInitializer
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object Bruaket : ModInitializer {
	val MOD_ID = "bruaket"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOGGER.info("正在加载桶模组！ Loading Bruaket mod !")
		val initMap = InitData().ItemMap
		InitGroups(initMap).register()
		InitItems(initMap).register()
		LOGGER.info("加载完成！ Loading completed !")
	}
}