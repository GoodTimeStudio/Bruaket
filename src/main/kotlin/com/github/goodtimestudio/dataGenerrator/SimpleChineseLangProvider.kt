/*
 * This file is part of Bruaket, licensed under the MIT License (MIT).
 * Copyright (c) 2023. GoodTime Studio
 */

package com.github.goodtimestudio.dataGenerrator

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class SimpleChineseLangProvider constructor(dataGenerator: FabricDataOutput) : FabricLanguageProvider(dataGenerator, "zh_cn") {
    var dg = dataGenerator
    override fun generateTranslations(translationBuilder: TranslationBuilder) {

    }
}